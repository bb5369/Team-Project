package com.webcheckers.model;

import java.util.logging.Logger;

import static com.webcheckers.model.CheckersBoardHelper.getSpace;
import static com.webcheckers.model.CheckersBoardHelper.formatBoardString;
import static com.webcheckers.model.CheckersBoardHelper.getSpacePieceColor;

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
    private static final int JUMP_DIFF = 2;

    /**
     * Entrypoint to move validation algorithm - kicks off the process
     *
     * @return - true if the move is valid
     */
    public static boolean validateMove(Space[][] board, Move move) {

        LOG.fine(String.format("Validating move for Player [%s]", move.getPlayerName()));

        logMoveCoordinates(move);

        LOG.finest(formatBoardString(board));
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

                        for(int row = -2; row <= 2; row += 1){

                            for(int col = -2; col <= 2; col += 1){

                                int destRow = i + row;
                                int destCell = j + col;

                                LOG.info(String.format("Checking space <%d, %d>", destRow, destCell));

                                if (destRow < 0 || destCell < 0) {
                                    LOG.info("Row or Cell is less than 0.");
                                    continue;
                                }

                                if (destRow >= CheckersBoardBuilder.ROWS || destCell >= CheckersBoardBuilder.CELLS) {
                                    LOG.info("Row or Cell is greater than the number of rows or cells.");
                                    continue;
                                }

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
     * Determines whether or not the given player has any pieces on the board
     *
     * @return - true if the player has pieces, false otherwise
     */
    public static boolean playerHasPieces(Space[][] board, Piece.Color color){
        // for each row
        for(int i = 0; i < 8; i++) {
            // for each cell
            for (int j = 0; j < 8; j++) {

                if (board[i][j].isOccupied() && board[i][j].getPiece().getColor() == color) {
                    return true;
                }
            }
        }
        return false;
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
        //LOG.finest(String.format("Validate     └─ isDiagonal() -  %s", move.isDiagonal()));
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
        if (piece.getType() == Piece.Type.KING) {
            conditionTruth = true;
        } else {

            // White players start at top, move down board
            // Red players start at bottom, move up board

            int startRow = move.getStartRow();
            int endRow = move.getEndRow();

            if(move.getPieceColor() == Piece.Color.RED)
                conditionTruth = (endRow < startRow);
            else
                conditionTruth = (endRow > startRow);

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
            Space space = getSpace(board, move.getMidpoint());
            Piece piece = getSpace(board, move.getStart()).getPiece();
            if (space.isOccupied()) {
                if (!space.getPiece().getColor().equals(piece.getColor())) {
                    conditionTruth = true;
                }
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
        boolean conditionTruth;

        Space start = getSpace(board, move.getStart());

        conditionTruth = start.getPiece().getColor() == move.getPieceColor();

        return conditionTruth;

    }

    /**
     * Checks if there are any possible jump moves for any piece owned by a player
     * (ownership is checked through Color of piece)
     * @param board
     * @return boolean - true if there is a jump move, false otherwise
     */
    public static boolean areJumpsAvailableForPlayer(Space[][] board, Piece.Color playerColor){
        for (int row = 0; row < board.length; row++) {
            for (int cell = 0; cell < board[row].length; cell++) {
                if (canJump(board, row, cell, playerColor))
                    return true;
            }
        }
        return false;
    }

    //Called after a move is made, so the starting postion should be the ending row and cell

    /**
     * This MoveValidator entry-point assumes the given position is the result of a multi-jump
     * It determines if the multi-jump can continue
     * @param board
     * @param pos
     * @param piece
     * @return
     */
    public static boolean canContinueJump(Space[][]board, Position pos, Piece.Color piece){
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
            Move testMove;
            Position start = new Position(row, cell);
            if (cell + 2 < board[row].length) {
                if (row + 2 < board.length) {
                    testMove = new Move(start, new Position(row + JUMP_DIFF, cell + JUMP_DIFF));
                    testMove.setPieceColor(getSpacePieceColor(board, testMove.getStart()));
                    if (canJumpValidation(board,testMove))
                        return true;
                }
                if (row - 2 >= 0) {
                    testMove = new Move(start, new Position(row - JUMP_DIFF, cell + JUMP_DIFF));
                    testMove.setPieceColor(getSpacePieceColor(board, testMove.getStart()));
                    if (canJumpValidation(board,testMove))
                        return true;
                }
            }
            if (cell - 2 >= 0) {
                if (row + 2 < board.length) {
                    testMove = new Move(start, new Position(row + JUMP_DIFF, cell - JUMP_DIFF));
                    testMove.setPieceColor(getSpacePieceColor(board, testMove.getStart()));
                    if (canJumpValidation(board,testMove))
                        return true;
                }
                if (row - 2 >= 0) {
                    testMove = new Move(start, new Position(row - JUMP_DIFF, cell - JUMP_DIFF));
                    testMove.setPieceColor(getSpacePieceColor(board, testMove.getStart()));
                    if (canJumpValidation(board,testMove))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean canJumpValidation(Space[][] board, Move move)
    {
        return isMoveJumpingAPiece(board, move) && isEndSpaceOpen(board, move) && isMoveInRightDirection(board, move);
    }
}
