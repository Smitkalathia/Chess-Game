package com.finalProject.chess;

public class Rook extends Piece{
    public Rook(int x, int y, String color){
        super(x, y, color);
    }

    @Override
    public void move(Piece[][] board) {
        int y = column;
        int x = row;
        // up
        while(y < 7){
            y +=1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // LEFT
        y = column;
        x = row;
        while(y > 0){
            y -=1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // DOWN
        y = column;
        x = row;
        while(x > 0){
            x -=1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // UP
        y = column;
        x = row;
        while(x < 7){
            x +=1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        if(color.equals("white") & enabled){
            possibleMoves = blockMove(board, color, possibleMoves);
        } else if(color.equals("black") & enabled){
            possibleMoves = blockMove(board, color, possibleMoves);
        }
    }
}
