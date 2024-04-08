package com.finalProject.chess;

public class Pawn extends Piece{
    boolean first_move;
    public Pawn(int row, int column, String color){
        super(row, column, color);
        first_move = true;
    }

    @Override
    public void move(Piece[][] board) {

        if(color.equals("white")){
            if(first_move & board[row + 1][column]==null & board[row + 2][column]==null){
                possibleMoves.add(new Integer[]{row + 2, column});
                possibleMoves.add(new Integer[]{row + 1, column});
            } else if(row < 7){
                if(board[row + 1][column]==null){
                    possibleMoves.add(new Integer[]{row + 1, column});
                }
            }

            if(row < 7 & column < 7){
                if(board[row + 1][column + 1] != null){
                    if(board[row + 1][column + 1].color.equals("black")){
                        possibleMoves.add(new Integer[]{row + 1, column + 1});
                    }
                }
            }

            if(row < 7 & column > 0){
                if(board[row + 1][column - 1] != null){
                    if(board[row + 1][column - 1].color.equals("black")){
                        possibleMoves.add(new Integer[]{row + 1, column - 1 });
                    }
                }
            }

        } else {

            if(first_move & board[row - 1][column]==null & board[row - 2][column]==null){
                possibleMoves.add(new Integer[]{row - 2, column});
                possibleMoves.add(new Integer[]{row - 1, column});
            } else if(row > 0){
                if(board[row - 1][column]==null){
                    possibleMoves.add(new Integer[]{row - 1, column});
                }
            }

            if(row > 0 & column < 7){
                if(board[row - 1][column + 1] != null){
                    if(board[row - 1][column + 1].color.equals("white")){
                        possibleMoves.add(new Integer[]{row - 1, column + 1});
                    }
                }
            }

            if(row > 0 & column > 0){
                if(board[row - 1][column - 1] != null){
                    if(board[row - 1][column - 1].color.equals("white")){
                        possibleMoves.add(new Integer[]{row - 1, column - 1});
                    }
                }
            }

            if(color.equals("white") & enabled){
                possibleMoves = blockMove(board, color, possibleMoves);
            } else if(color.equals("black") & enabled){
                possibleMoves = blockMove(board, color, possibleMoves);
            }
        }
    }
}
