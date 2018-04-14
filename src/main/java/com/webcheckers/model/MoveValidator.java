package com.webcheckers.model;

import javafx.geometry.Pos;

import java.util.logging.Logger;

/**
 * Move validation requires several things:
 * - a board to validate the move against
 * - a player, and the color of their pieces
 * - the move to validate
 *
 * Move validation has two modes of operation:
 * 1. Given a board and a move (start, end, player, color) - It will determine if the move is valid on the board
 *
 * 2. Given a board and a player color it will determine if that player has any moves available
 *
 */
public class MoveValidator {

    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());

    /**
     * Entrypoint to move validation algorithm - kicks off the process
     *
     * @return - true if the move is valid
     */
    public static boolean validateMove(Space[][] board, Move move) {

        LOG.fine(String.format("Validating move for Player [%s]", move.getPlayerName()));

        logMoveCoordinates(move);

        LOG.finest(CheckersBoardBuilder.formatBoardString(board));
        logMove(board, move);

        boolean isMoveValidOnBoard = move.isValid() &&
									isMoveInRightDirection(board, move) &&
									isEndSpaceOpen(board, move) &&
									(move.isSingleSpace() || isMoveJumpingAPiece(board, move)) &&
                                    areWeMovingMyPiece(board, move);

        LOG.fine(String.format("Move validity has been determined to be %s", isMoveValidOnBoard));

        return isMoveValidOnBoard;
    }

    /**
     * Checks to see if there are any available moves for the player color
     *
     * @return true if there are available moves, false otherwise
     */
    public static boolean areMovesAvailableForPlayer(Space[][] board, Player player, Piece.Color color){
        boolean movesLeft = false;

        LOG.fine(String.format("Determining if %s player has any moves left", color));

        boardWalk: {
            // for each row
            for(int i = 0; i < 8; i++){
                // for each cell
                for(int j = 0; j < 8; j++){

                    if(board[i][j].isOccupied() && board[i][j].getPiece().getColor() == color){
                        Position currentPos = new Position(i, j);

                        Move checkMove;

                        for(int row = -1; row < 2; row += 2){

                            for(int col = -1; col < 2; col += 2){

                                int destRow = i + row;
                                int destCell = j + col;

                                if (destRow < 0 || destCell < 0)
                                    continue;

                                if (destRow >= CheckersBoardBuilder.ROWS || destCell >= CheckersBoardBuilder.CELLS)
                                    continue;

                                Position place = new Position(destRow, destCell);
                                checkMove = new Move(currentPos, place, player, color);

                                if(validateMove(board, checkMove)) {
                                    LOG.finest(String.format("Found a valid move! %s", checkMove.toString()));
                                    movesLeft = true;
                                    break boardWalk;
                                }

                            }
                        }
                    }
                }
            }
        }

        String condition = (movesLeft) ? "has" : "does not have";

        LOG.fine(String.format("%s Player %s moves left", color, condition));

        return movesLeft;
    }

    /**
     * Logs the move as a pair of coordinates
     *
     * @param move - Move being made
     */
    private static void logMoveCoordinates(Move move) {
        LOG.finer(String.format("%s Player [%s] wants to move from %s",
                move.getPieceColor(),
                move.getPlayerName(),
                move.toString()));
    }

    /**
     * Logs the current state of affairs at the two spaces involved in the move
     */
    private static void logMove(Space[][] matrix, Move move) {
        Space startSpace = getSpace(matrix, move.getStart());
        Space endSpace = getSpace(matrix, move.getEnd());

        LOG.finest(String.format("Starting position state is [%s] by a %s Piece", startSpace.getState(),
                startSpace.getPiece().getColor()));
        LOG.finest(String.format("End position state is [%s]", endSpace.getState()));


        // I'm so sorry. This is to assist debugging hell
        LOG.finest(String.format("Validate move.isValid() - %s", move.isValid()));
        LOG.finest(String.format("Validate     └─ start.isOnBoard() -  %s", move.getStart().isOnBoard()));
        LOG.finest(String.format("Validate     └─ end.isOnBoard() -  %s", move.getEnd().isOnBoard()));
        LOG.finest(String.format("Validate     └─ isSingleSpace() -  %s", move.isSingleSpace()));
        LOG.finest(String.format("Validate     └─ isJump() -  %s", move.isJump()));
        LOG.finest(String.format("Validate     └─ isDiagonal() -  %s", move.isDiagonal()));
    }

    /**
     * Given a move is the end position open
     *
     * @param move - Move being made
     * @return - true if the space being moved to is a valid, unoccupied space
     */
    private static boolean isEndSpaceOpen(Space[][] matrix, Move move) {
        Space endSpace = getSpace(matrix, move.getEnd());

        boolean conditionTruth = endSpace.isOpen();

        LOG.finest(String.format("Validate isEndSpaceOpen(): %s", conditionTruth));

        return conditionTruth;
    }

    /**
     * Determines if the piece is moved in the right direction
     *
     * @param move - Move being made
     * @return - True, if it does, false otherwise
     */
    private static boolean isMoveInRightDirection(Space[][] matrix, Move move) {
        Piece piece = getSpace(matrix, move.getStart()).getPiece();
        boolean conditionTruth = false;

        // If the piece is a king then they can move bi-directionally
        if (piece != null && piece.getType() == Piece.Type.KING) {
            conditionTruth = true;
        } else {

            // White players start at top, move down board
            // Red players start at bottom, move up board

            int startRow = move.getStartRow();
            int endRow = move.getEndRow();

            switch (move.getPieceColor()) {
                case RED:
                    conditionTruth = (endRow < startRow);
                    break;
                case WHITE:
                    conditionTruth = (endRow > startRow);
                    break;
            }
        }

        LOG.finest(String.format("Validate isMoveInRightDirection(): %s", conditionTruth));

        return conditionTruth;

    }

    /**
     * Determines if a piece is a valid jump move
     *
     * @param board
     * @param move
     * @return - true if the move is a jump move, false otherwise
     */
    private static boolean isMoveJumpingAPiece(Space[][] board, Move move) {
        boolean conditionTruth = false;
        if (move.isJump()) {
            Space space = getSpace(board, Position.midPosition(move.getEnd(), move.getStart()));
            Piece piece = getSpace(board, move.getStart()).getPiece();
            if (space.isOccupied() && !space.getPiece().getColor().equals(piece.getColor())) {
                conditionTruth = true;
            }
        }
        LOG.finest(String.format("Validate areWeMovingMyPiece(): %s", conditionTruth));

        return conditionTruth;
    }

    /**
     * Guard condition to prevent malicious moves sent from browser.
     * We must only move a piece located at the start that we own
     * @param board
     * @param move
     * @return boolean - if the player owns the piece at the start position
     */
    private static boolean areWeMovingMyPiece(Space[][] board, Move move) {
        boolean conditionTruth = false;

        Space start = getSpace(board, move.getStart());

        conditionTruth = start.getPiece().getColor() == move.getPieceColor();

        return conditionTruth;

    }

    /**
     * Checks if there are any possible jump moves
     * @param board
     * @param move
     * @return boolean - true if there is a jump move, false otherwise
     */
    public static boolean forcedJump (Space[][] board, Move move){
        Piece piece = getSpace(board, move.getStart()).getPiece();
        for (int row = 0; row < board.length; row++) {
            for (int cell = 0; cell < board[row].length; cell++) {
                if (canJump(board, row, cell, piece.getColor()))
                    return true;
            }
        }
        return false;
    }

    //Called after a move is made, so the starting postion should be the ending row and cell
    public static boolean isJumpMove(Space[][]board, Position pos, Piece.Color piece){
        boolean condition;
        int row = pos.getRow();
        int cell = pos.getCell();
        condition = canJump(board, row, cell, piece);
        LOG.fine(String.format("Can Mutlt-jump: %b", condition));
        return condition;
    }

    private static boolean canJump(Space[][] board, int row, int cell, Piece.Color piece) {
        Space cur = board[row][cell];
        if (cur.isOccupied() && piece.equals(cur.getPiece().getColor())) {
            Move test;
            Position start = new Position(row, cell);
            if (cell + 2 < board[row].length) {
                if (row + 2 < board.length) {
                    test = new Move(start, new Position(row + 2, cell + 2));
                    test.setPieceColor(getSpace(board, test.getStart()).getPiece().getColor());
                    //test.setPlayer(new Player(move.getPlayerName()));
                    if (isMoveJumpingAPiece(board, test) && isEndSpaceOpen(board, test) && isMoveInRightDirection(board, test))
                        return true;
                }
                if (row - 2 >= 0) {
                    test = new Move(start, new Position(row - 2, cell + 2));
                    test.setPieceColor(getSpace(board, test.getStart()).getPiece().getColor());
                    //test.setPlayer(new Player(move.getPlayerName()));
                    if (isMoveJumpingAPiece(board, test) && isEndSpaceOpen(board, test) && isMoveInRightDirection(board, test))
                        return true;
                }
            }
            if (cell - 2 >= 0) {
                if (row + 2 < board.length) {
                    test = new Move(start, new Position(row + 2, cell - 2));
                    test.setPieceColor(getSpace(board, test.getStart()).getPiece().getColor());
                    //test.setPlayer(new Player(move.getPlayerName()));
                    if (isMoveJumpingAPiece(board, test) && isEndSpaceOpen(board, test) && isMoveInRightDirection(board, test))
                        return true;
                }
                if (row - 2 >= 0) {
                    test = new Move(start, new Position(row - 2, cell - 2));
                    test.setPieceColor(getSpace(board, test.getStart()).getPiece().getColor());
                    //test.setPlayer(new Player(move.getPlayerName()));
                    if (isMoveJumpingAPiece(board, test) && isEndSpaceOpen(board, test) && isMoveInRightDirection(board, test))
                        return true;
                }
            }
        }
        return false;
    }

    /**
         * Board lookup convenience method - given a position it will return the enumerated state
         *
         * @param pos - end position of the move
         * @return SpaceState - current state of the position being moved to
         */
        private static Space getSpace (Space[][]board, Position pos){
            return board[pos.getRow()][pos.getCell()];
        }
}
