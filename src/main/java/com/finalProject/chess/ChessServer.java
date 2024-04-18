package com.finalProject.chess;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONArray;
import org.json.JSONObject;


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
        game.addPlayer(session);
        sessions.put(session.getId(), session);

        Map<Integer[], List<Integer[]>> nextMoves = game.getNextMoves();
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

        session.getBasicRemote().sendText(jsonMoves.toString());

    }

    @OnMessage
    public void handleMessage(@PathParam("gameID") String message, String gameID, Session session) throws IOException, EncodeException {

        String[] moveInfo = message.split(",");
        Integer[] position = {Integer.parseInt(moveInfo[0]), Integer.parseInt(moveInfo[1])};
        Integer[] move = {Integer.parseInt(moveInfo[2]), Integer.parseInt(moveInfo[3])};

        ChessBoard gameBoard = games.get(gameID);



        gameBoard.makeTheMove(session, position, move);

        viewGameState(gameID, session);
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


    private void viewGameState(String gameID, Session excludeSession) throws IOException {
        ChessBoard currentGame = games.get(gameID);

        String gameState = currentGame.getGameState();
        Map<Integer[], List<Integer[]>> nextMoves = currentGame.getNextMoves();

        JSONObject gameInfo = new JSONObject();
        gameInfo.put("state", gameState);
        gameInfo.put("moves", nextMoves);

        for(Session session : games.get(gameID).getPlayers()) {
            if(excludeSession == null || !session.getId().equals(excludeSession.getId())) {
                session.getBasicRemote().sendText(gameState);
            }
        }
    }

    public static Session getSession(String sessionID){
        return sessions.get(sessionID);
    }

}




