package com.finalProject.chess;

public class Knight extends Piece{

    public Knight(int row, int column, String color){
        super(row, column, color);
    }

    @Override
    public void move(Piece[][] board) {
        // down left
        int y = column;
        int x = row;
        if(y > 0 & x > 1){
            y -= 1;
            x -= 2;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // down right
        y = column;
        x = row;
        if(y < 7 & x > 1){
            y += 1;
            x -= 2;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // left down
        y = column;
        x = row;
        if(y > 1 & x > 0){
            y -= 2;
            x -= 1;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // right down
        y = column;
        x = row;
        if(y < 6 & x > 0){
            y += 2;
            x -= 1;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // right up
        y = column;
        x = row;
        if(y < 6 & x < 7){
            y += 2;
            x += 1;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // up right
        y = column;
        x = row;
        if(y < 7 & x < 6){
            y += 1;
            x += 2;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // left up
        y = column;
        x = row;
        if(y > 1 & x < 7){
            y -= 2;
            x += 1;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
        // up left
        y = column;
        x = row;
        if(y > 0 & x < 6){
            y -= 1;
            x += 2;
            if(board[x][y]==null) {
                possibleMoves.add(new Integer[]{x, y});
            }else {
                if(!board[x][y].color.equals(color)){
                    possibleMoves.add(new Integer[]{x, y});
                }
            }
        }
//        if(color.equals("white") & enabled){
//            possibleMoves = blockMove(board, color, possibleMoves);
//        } else if(color.equals("black") & enabled){
//            possibleMoves = blockMove(board, color, possibleMoves);
//        }
    }
}
