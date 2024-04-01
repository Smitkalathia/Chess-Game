package com.example.webchatserver;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/ws/{roomID}")
public class ChatServer {

    // contains a static List of ChatRoom used to control the existing rooms and their users
    private static List<ChatRoom> rooms = new ArrayList<>();
    // you may add other attributes as you see fit



    @OnOpen
    public void open(@PathParam("roomID") String roomID, Session session) throws IOException, EncodeException {

        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server " + roomID + "): Welcome to the chat room. " +
                "Please state your username to begin.\"}");


    }

    @OnClose
    public void close(@PathParam("roomID") String roomID, Session session) throws IOException, EncodeException {
        // do things for when the connection closes
        String userId = session.getId();
        ChatRoom currentRoom = null;
        // iterating through rooms to find the one with the same roomID
        for(ChatRoom room: rooms){
            if(room.getCode().equals(roomID)){
                currentRoom = room;
                break;
            }
        }
        // if not found throw an error
        if(currentRoom==null){
            throw new IOException("The room code is not valid");
        }
        // get the usernames from the current room
        Map<String,String> usernames = currentRoom.getUsers();

        String username = usernames.get(userId);
        currentRoom.removeUser(userId); // delete the user
        // let people from the same chatroom know this person left the server
        for (Session peer : session.getOpenSessions()) {
            if(usernames.containsKey(peer.getId())) {
                peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + username + " left the chat room.\"}");
            }
        }
    }

    @OnMessage
    public void handleMessage(@PathParam("roomID") String roomID, String comm, Session session) throws IOException, EncodeException {

        // handle the messages
        String userId = session.getId();
        ChatRoom currentRoom = null;
        // trying to identify the room by the roomID provided
        for(ChatRoom room: rooms){
            if(room.getCode().equals(roomID)){
                currentRoom = room;
                if (!currentRoom.inRoom(userId)) {
                    currentRoom.setUserName(userId, "");
                }
                break;
            }
        }
        // create new room if the roomID is not in rooms
        if(currentRoom==null){
            currentRoom = new ChatRoom(roomID, userId);
            rooms.add(currentRoom);
        }

        Map<String, String> usernames = currentRoom.getUsers();
        // read the JSON object
        JSONObject jsonMsg = new JSONObject(comm);
        String type = jsonMsg.getString("type");
        String msg = jsonMsg.getString("msg");

        // return room codes if type is set to "codes"
        if (type.equals("codes")){
            session.getBasicRemote().sendText(getAllRoomCodes());
        } else { // otherwise process msg for chat
            if (!usernames.get(userId).isEmpty()) {
                String username = usernames.get(userId);
                for (Session peer : session.getOpenSessions()) {
                    if (usernames.containsKey(peer.getId())) {
                        peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + msg + "\"}");
                    }
                }
            } else {
                currentRoom.setUserName(userId, msg);
                session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server " + roomID + "): Welcome, " + msg + "!\"}");

                for (Session peer : session.getOpenSessions()) {
                    if (!peer.getId().equals(userId) & usernames.containsKey(peer.getId())) {
                        peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server " + roomID + "): " + msg + " joined the chat room.\"}");
                    }
                }
            }
        }
    }
    // function returns all the room codes that exist so far in json format
    private String getAllRoomCodes(){
        List<String> codesList = new ArrayList<>();
        for(ChatRoom room: rooms){
            codesList.add(room.getCode());
        }
        JSONArray jsonArray = new JSONArray(codesList);
        return "{\"type\": \"codes\", \"message\": " + jsonArray + "}";
    }
}