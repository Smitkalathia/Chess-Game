package com.finalProject.chess;

import java.util.ArrayList;
import java.util.List;

abstract class Piece {
    int row, column;

    String color;
    List<Integer[]> possibleMoves = new ArrayList<>();
    static boolean whiteKingCheck = false;
    static boolean blackKingCheck = false;
    boolean enabled;
    public Piece(int row, int column, String color){
        this.row = row;
        this.column = column;
        this.color = color;
        this.enabled = color.equals("white");
    }
    public abstract void move(Piece[][] board);

    public List<Integer[]> blockMove(Piece[][] board, String color, List<Integer[]> possibleMoves){
        List<Integer[]> validMoves = new ArrayList<>();
        for(Piece[] row: board){
            for(Piece piece: row){
                if(piece!=null){
                    if(piece.color.equals(color) & piece.getClass().getSimpleName().equals("King")){
                        validMoves = ((King)piece).checkIfSafeForTheKing(board, possibleMoves, board[this.row][this.column]);
                        break;
                    }
                }
            }
            break;
        }
        return validMoves;
    }

    public void checkForCheck(Piece[][] board){
        for(Piece[] row: board){
            for(Piece piece: row){
                if(piece!=null){
                    if(color.equals("black")){

                        if((piece.color.equals("white") & piece.getClass().getSimpleName().equals("King")) & !Piece.whiteKingCheck) {
                            List<Integer[]> move = new ArrayList<>();
                            move.add(new Integer[]{this.row, this.column});
                            if(((King)piece).checkIfSafeForTheKing(board, move, board[this.row][this.column])==null){
                                Piece.whiteKingCheck = true;
                            } else if(Piece.blackKingCheck  & piece.getClass().getSimpleName().equals("King")){
                                Piece.blackKingCheck = false;
                            }
                        }
                    } else{
                        if((piece.color.equals("black") & piece.getClass().getSimpleName().equals("King")) & !Piece.blackKingCheck) {
                            List<Integer[]> move = new ArrayList<>();
                            move.add(new Integer[]{this.row, this.column});
                            if(((King)piece).checkIfSafeForTheKing(board, move, board[this.row][this.column])==null){
                                Piece.blackKingCheck = true;
                            } else if(Piece.whiteKingCheck  & piece.getClass().getSimpleName().equals("King")){
                                Piece.whiteKingCheck = false;
                            }
                        }
                    }
                }
            }
        }
    }
//    public void makeMove(Piece[][] board, Integer[] move){
//
//        board[move[0]][move[1]] = board[row][column];
//        board[row][column] = null;
//        row = move[0];
//        column = move[1];
//        if(this.getClass().getSimpleName().equals("Pawn")){
//            ((Pawn)this).first_move = false;
//        }
//        checkForCheck(board);
//    }

}
