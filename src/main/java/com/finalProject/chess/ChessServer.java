package com.finalProject.chess;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/chess/{gameID}")
public class ChessServer {
    private static Map<String, ChessBoard> games = new HashMap<>();

    @OnOpen
    public void open(@PathParam("gameID") String gameID, Session session) throws IOException, EncodeException {
        if(!games.containsKey(gameID)) {
            games.put(gameID, new ChessBoard());
        }
        ChessBoard game = games.get(gameID);
        game.addPlayer(session);
    }

    @OnMessage
    public void handleMessage(@PathParam("gameID") String message, String gameID, Session session) throws IOException, EncodeException {

        String[] moveInfo = message.split(",");
        Integer[] position = {Integer.parseInt(moveInfo[0]), Integer.parseInt(moveInfo[1])};
        Integer[] move = {Integer.parseInt(moveInfo[2]), Integer.parseInt(moveInfo[3])};

        ChessBoard gameBoard = games.get(gameID);

        gameBoard.makeTheMove(position, move);

        viewGameState(gameID);
    }

    @OnClose
    public void close(@PathParam("gameID") String gameID, Session session){
        ChessBoard game = games.get(gameID);
        if (game!= null) {
            game.removePlayer(session);
            if(game.isEmpty()) {
                games.remove(gameID);
            }
        }
    }


    private void viewGameState(String gameID) throws IOException {
        ChessBoard currentGame = games.get(gameID);

        String gameState = currentGame.getGameState();
    }

}




