package com.finalProject.chess;

import jakarta.websocket.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class main {

    public static String movesToJson(ChessBoard game, String moveToMake){
        Map<Integer[], List<Integer[]>> nextMoves = game.getNextMoves();

        JSONObject output = new JSONObject();

        HashMap<String,String> map = new HashMap<>();
        map.put("1,2", "2,3");
        JSONObject move1 = new JSONObject(map);
        output.put("moveToMake", move1);

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

    public static void printBoard(Piece[][] board){

        Map<String, Character> map = new HashMap<>();
        map.put("Rook", 'R');
        map.put("King", 'K');
        map.put("Knight", 'N');
        map.put("Bishop", 'B');
        map.put("Queen", 'Q');
        map.put("Pawn", 'p');
        for(Piece[] row: board){
            for(Piece piece: row){
                if(piece==null){
                    System.out.print(" -- ");
                } else {
                    int x = piece.column;
                    int y = piece.row;
                    String color = piece.color;
                    if(color.equals("white")){
                        System.out.print(" w" + map.get(piece.getClass().getSimpleName()) + " ");
                    } else {
                        System.out.print(" b" + map.get(piece.getClass().getSimpleName()) + " ");
                    }

                }
            }
            System.out.println();
        }
    }

    private static Integer[] stringToIntArray(String s) {
        String[] parts = s.split(",");
        Integer[] intArray = new Integer[2];
        for (int i = 0; i < 2; i++) {
            intArray[i] = Integer.parseInt(parts[i]);
        }
        return intArray;
    }

    public static void main(String[] args){
        Session session = ChessServer.getSession("sessionID");

        ChessBoard board = new ChessBoard();
        Piece[][] gameboard = board.getBoard();
        System.out.println(movesToJson(board, "{\"1,2\" : \"2,3\"}"));
//        Map<Integer[], List<Integer[]>> moves = board.getNextMoves();
//        for(Map.Entry<Integer[], List<Integer[]>> entry: moves.entrySet()){
//
//            System.out.println( gameboard[entry.getKey()[0]][entry.getKey()[0]].getClass().getSimpleName() +" at row: " + entry.getKey()[0] + ", col: " + entry.getKey()[1] + " has moves: ");
//            for(Integer[] move: entry.getValue()){
//                System.out.println("row: " + move[0] + ", col: " + move[1]);
//            }
//        }
//        printBoard(gameboard);
//        System.out.println();
//        board.makeTheMove(session, new Integer[]{1,5}, new Integer[]{2,5});
//        printBoard(gameboard);
//        moves = board.getNextMoves();
//        for(Map.Entry<Integer[], List<Integer[]>> entry: moves.entrySet()){
//
//            System.out.println( gameboard[entry.getKey()[0]][entry.getKey()[0]].getClass().getSimpleName() +" at row: " + entry.getKey()[0] + ", col: " + entry.getKey()[1] + " has moves: ");
//            for(Integer[] move: entry.getValue()){
//                System.out.println("row: " + move[0] + ", col: " + move[1]);
//            }
//        }
//        printBoard(gameboard);

//        String message = "{\"1,2\" : \"2,3\"}";
//        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
//
//        String key = jsonObject.keySet().iterator().next();  // Get the first key
//        String value = jsonObject.get(key).getAsString();
//        Integer[] position = stringToIntArray(key);
//        Integer[] move = stringToIntArray(value);
//
//        // Output the results
//        System.out.println("Position: " + java.util.Arrays.toString(position));
//        System.out.println("Move: " + java.util.Arrays.toString(move));
    }
}


