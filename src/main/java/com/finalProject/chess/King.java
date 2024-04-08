package com.finalProject.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    boolean check;
    public King(int x, int y, String color){
        super(x, y, color);
        check = false;
    }

    @Override
    public void move(Piece[][] board) {
        // down left
        if(row > 0 & column > 0){
            if(board[row - 1][column - 1]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row - 1][column - 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row - 1, column - 1});
                }
            }
        }
        // Move down right
        if(row > 0 & column < 7){
            if(board[row - 1][column + 1]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row - 1][column + 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row - 1, column + 1});
                }
            }
        }
        // Move up left
        if(row < 7 & column > 0){
            if(board[row + 1][column - 1]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row + 1][column - 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row + 1, column - 1});
                }
            }
        }
        // Move up right
        if(row < 7 & column < 7){
            if(board[row + 1][column + 1]==null) {
                possibleMoves.add(new Integer[]{row + 1, column + 1});
            }else {
                if(!board[row + 1][column + 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row + 1, column + 1});
                }
            }
        }
        // move up
        if(row < 7){
            if(board[row + 1][column]==null) {
                possibleMoves.add(new Integer[]{row + 1, column});
            }else {
                if(!board[row + 1][column].color.equals(color)){
                    possibleMoves.add(new Integer[]{row + 1, column});
                }
            }
        }
        // move down
        if(row > 0){
            if(board[row - 1][column]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row - 1][column].color.equals(color)){
                    possibleMoves.add(new Integer[]{row - 1, column});
                }
            }
        }
        // move left
        if(column > 0){
            if(board[row][column - 1]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row][column - 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row, column - 1});
                }
            }
        }
        // move right
        if(column < 7){
            if(board[row][column + 1]==null) {
                possibleMoves.add(new Integer[]{row - 1, column});
            }else {
                if(!board[row][column + 1].color.equals(color)){
                    possibleMoves.add(new Integer[]{row, column + 1});
                }
            }
        }
    }

    public List<Integer[]> checkIfSafeForTheKing(Piece[][] board, List<Integer[]> possibleMoves, Piece piece){
        List<Integer[]> validMoves = new ArrayList<>();
        int initialRow = piece.row;
        int initialCol = piece.column;
        for(Integer[] move: possibleMoves){
            List<Integer[]> enemyMoves = new ArrayList<>();
            Piece tempHolder = board[move[0]][move[1]];
            board[move[0]][move[1]] = board[initialRow][initialCol];
            board[initialRow][initialCol] = null;
            for(Piece[] row: board){
                for(Piece figure: row){
                    if(figure != null){
                        if(!figure.color.equals(color)){
                            figure.move(board);
                            enemyMoves.addAll(figure.possibleMoves);
                            figure.possibleMoves = new ArrayList<>();
                        }
                    }
                }
            }
            board[move[0]][move[1]] = tempHolder;
            board[initialRow][initialCol] = piece;
            if(enemyMoves.contains(new Integer[]{row, column})){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

}
