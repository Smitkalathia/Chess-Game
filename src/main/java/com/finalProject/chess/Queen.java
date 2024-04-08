package com.finalProject.chess;

public class Queen extends Piece{
    public Queen(int x, int y, String color){
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
        // UP RIGHT
        y = column;
        x = row;
        while(y < 7 & x < 7){
            y += 1;
            x += 1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // DOWN RIGHT
        y = column;
        x = row;
        while(y < 7 & x > 0){
            y += 1;
            x -= 1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // DOWN LEFT
        y = column;
        x = row;
        while(y > 0 & x > 0){
            y -= 1;
            x -= 1;
            if(board[x][y]==null){
                possibleMoves.add(new Integer[]{x,y});
            } else if(board[x][y].color.equals(color)){
                break;
            } else{
                possibleMoves.add(new Integer[]{x,y});
                break;
            }
        }
        // UP LEFT
        y = column;
        x = row;
        while(y > 0 & x < 7){
            y -= 1;
            x += 1;
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
