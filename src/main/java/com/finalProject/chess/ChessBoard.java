package com.finalProject.chess;

import jakarta.websocket.Session;

import java.util.*;

public class ChessBoard {
    private String[] players = new String[2];
    private final Piece[][] board;
    boolean mate = false;
    String turn;
    List<Piece> whitePieces, blackPieces;
    boolean whiteKingCheck = false;
    boolean blackKingCheck = false;


    public ChessBoard(){

        players[0] = null;
        players[1] = null;
        board = new Piece[8][8];
        turn = "white";

        board[0][0] = new Rook(0, 0, "white");
        board[0][1] = new Knight(0, 1, "white");
        board[0][2] = new Bishop(0, 2, "white");
        board[0][3] = new King(0, 3, "white");
        board[0][4] = new Queen(0, 4, "white");
        board[0][5] = new Bishop(0, 5, "white");
        board[0][6] = new Knight(0, 6, "white");
        board[0][7] = new Rook(0, 7, "white");
        for(int i = 0; i < 8; i++){
            board[1][i] = new Pawn(1, i, "white");
        }

        board[7][0] = new Rook(7, 0, "black");
        board[7][1] = new Knight(7, 1, "black");
        board[7][2] = new Bishop(7, 2, "black");
        board[7][3] = new King(7, 3, "black");
        board[7][4] = new Queen(7, 4, "black");
        board[7][5] = new Bishop(7, 5, "black");
        board[7][6] = new Knight(7, 6, "black");
        board[7][7] = new Rook(7, 7, "black");
        for(int i = 0; i < 8; i++){
            board[6][i] = new Pawn(6, i, "black");
        }
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            whitePieces.add(board[0][i]);
            whitePieces.add(board[1][i]);
            blackPieces.add(board[7][i]);
            blackPieces.add(board[6][i]);
        }
    }

    public Piece[][] getBoard(){
        return board;
    }

    public boolean checkMate(){
        List<Piece> pieces;
        if (turn.equals("white")) {
            pieces = whitePieces;
        } else {
            pieces = blackPieces;
        }
        for(Piece piece: pieces){
            piece.move(board);
            if(!piece.possibleMoves.isEmpty()){
                piece.possibleMoves = new ArrayList<>();
                return false;
            }
        }
        mate = true;
        return true;

    }

    public Map<Integer[], List<Integer[]>> getNextMoves() {
        System.out.println("Turn is " + turn);
        Map<Integer[], List<Integer[]>> moves = new HashMap<>();
        List<Piece> pieces;
        if (turn.equals("white")) {
            pieces = whitePieces;
        } else {
            pieces = blackPieces;
        }
        for (Piece piece : pieces) {
            piece.move(board);
            moves.put(new Integer[]{piece.row, piece.column}, piece.possibleMoves);

            piece.possibleMoves = new ArrayList<>();

        }
        return moves;
    }

    public void makeTheMove(Integer[] piecePosition, Integer[] move){

        if(board[move[0]][move[1]]!=null){
            List<Piece> pieces;
            if(board[move[0]][move[1]].color.equals("white")){
                pieces = whitePieces;
            } else {
                pieces = blackPieces;
            }
            for(Piece piece: pieces){
                if(piece.row== move[0] & piece.column == move[1]){
                    pieces.remove(piece);
                    break;
                }
            }
        }

        Piece pieceToMove = board[piecePosition[0]][piecePosition[1]];
        board[move[0]][move[1]] = board[piecePosition[0]][piecePosition[1]];
        board[piecePosition[0]][piecePosition[1]] = null;
        pieceToMove.row = move[0];
        pieceToMove.column = move[1];
        if(pieceToMove.getClass().getSimpleName().equals("Pawn")){
            ((Pawn)pieceToMove).first_move = false;
        }
        pieceToMove.checkForCheck(board);
        if(turn.equals("white")){
            turn = "black";
            for(Piece piece: blackPieces){
                piece.enabled = false;
            }
            for(Piece piece: whitePieces){
                piece.enabled = true;
            }
        } else {
            turn = "white";
            for(Piece piece: blackPieces){
                piece.enabled = true;
            }
            for(Piece piece: whitePieces){
                piece.enabled = false;
            }
        }

        if(checkMate()){
            System.out.println("Game over");
        }
    }

    public String getGameState() {
        String gameState = "";

        Piece[][] board = getBoard();

        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if(piece != null) {
                    gameState += piece.color + piece;
                }
            }
        }

        return gameState;
    }

    public void addPlayer(Session player){
        if(players[0]==null){
            players[0] = player.getId();
        } else {
            players[1] = player.getId();
        }

    }

    public void removePlayer(Session player){
        if(players[0].equals(player.getId())){
            players[0] = null;
        } else {
            players[1] = null;
        }

    }

    public boolean isEmpty(){
        return players[0]==null & players[1]==null;
    }

    public String[] getPlayers(){
        return players;
    }
}
