package com.finalProject.chess;

import java.util.*;


public class main {

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

    public static void main(String[] args){
        ChessBoard board = new ChessBoard();
        Piece[][] gameboard = board.getBoard();



        Map<Integer[], List<Integer[]>> moves = board.getNextMoves();
        for(Map.Entry<Integer[], List<Integer[]>> entry: moves.entrySet()){

            System.out.println( gameboard[entry.getKey()[0]][entry.getKey()[0]].getClass().getSimpleName() +" at row: " + entry.getKey()[0] + ", col: " + entry.getKey()[1] + " has moves: ");
            for(Integer[] move: entry.getValue()){
                System.out.println("row: " + move[0] + ", col: " + move[1]);
            }
        }
        printBoard(gameboard);
        System.out.println();
        board.makeTheMove(new Integer[]{1,5}, new Integer[]{2,5});
        printBoard(gameboard);
        moves = board.getNextMoves();
        for(Map.Entry<Integer[], List<Integer[]>> entry: moves.entrySet()){

            System.out.println( gameboard[entry.getKey()[0]][entry.getKey()[0]].getClass().getSimpleName() +" at row: " + entry.getKey()[0] + ", col: " + entry.getKey()[1] + " has moves: ");
            for(Integer[] move: entry.getValue()){
                System.out.println("row: " + move[0] + ", col: " + move[1]);
            }
        }
        printBoard(gameboard);
    }
}
