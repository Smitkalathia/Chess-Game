package com.finalProject.chess;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/chess/{gameID}")
public class ChessServer {
    private static Map<String, ChessBoard> games = new HashMap<>();
    private static Map<String, Session> sessions = new HashMap<>();

    @OnOpen
    public void open(@PathParam("gameID") String gameID, Session session) throws IOException, EncodeException {
        if(!games.containsKey(gameID)) {
            games.put(gameID, new ChessBoard());
        }
        ChessBoard game = games.get(gameID);

        if(!game.isEmpty()){
            game.addPlayer(session);
            sessions.put(session.getId(), session);
        } else {
            game.addPlayer(session);
            sessions.put(session.getId(), session);

            String output = movesToJson(game, null,null);
            session.getBasicRemote().sendText(output);
        }


    }

    public static String movesToJson(ChessBoard game, Integer[] positionToMove, Integer[] moveToMove){
        Map<Integer[], List<Integer[]>> nextMoves = game.getNextMoves();

        JSONObject output = new JSONObject();
        if(positionToMove==null & moveToMove==null){
            output.put("moveToMake", "");
        } else {
            HashMap<String,String> map = new HashMap<>();
            map.put(positionToMove[0] + "," + positionToMove[1], moveToMove[0] + "," + moveToMove[1]);
            JSONObject move1 = new JSONObject(map);
            output.put("moveToMake", move1);
        }
        JSONObject jsonMoves = new JSONObject();
        for (Map.Entry<Integer[], List<Integer[]>> entry : nextMoves.entrySet()) {
            Integer[] position = entry.getKey();
            List<Integer[]> moves = entry.getValue();

            JSONArray jsonArrMoves = new JSONArray();
            for(Integer[] move : moves){
                JSONArray moveArray = new JSONArray();
                moveArray.put(move[0]);
                moveArray.put(move[1]);
                jsonArrMoves.put(moveArray);
            }

            String positionKey = position[0] + "," + position[1];
            jsonMoves.put(positionKey, jsonArrMoves);
        }
        output.put("possibleMoves", jsonMoves);
        return output.toString();
    }

    private static Integer[] stringToIntArray(String s) {
        String[] parts = s.split(",");
        Integer[] intArray = new Integer[2];
        for (int i = 0; i < 2; i++) {
            intArray[i] = Integer.parseInt(parts[i]);
        }
        return intArray;
    }

    @OnMessage
    public void handleMessage(@PathParam("gameID") String gameID, String message, Session session) throws IOException, EncodeException {

        System.out.println(message);


            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();

            String key = jsonObject.keySet().iterator().next();
            String value = jsonObject.get(key).getAsString();
            Integer[] position = stringToIntArray(key);
            Integer[] move = stringToIntArray(value);

            ChessBoard gameBoard = games.get(gameID);

            gameBoard.makeTheMove(position, move);

            String gameInfo = movesToJson(gameBoard, position, move);

            for(Session playerSession : session.getOpenSessions()) {
                if(!playerSession.getId().equals(session.getId())) {
                    playerSession.getBasicRemote().sendText(gameInfo);
                }
            }

    }

    @OnClose
    public void close(@PathParam("gameID") String gameID, Session session){
        ChessBoard game = games.get(gameID);
        if (game != null) {
            game.removePlayer(session);
            if(game.isEmpty()) {
                games.remove(gameID);
            }
        }
    }


    public static Session getSession(String sessionID){

        return sessions.get(sessionID);
    }
}




