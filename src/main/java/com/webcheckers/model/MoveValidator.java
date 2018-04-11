package com.webcheckers.model;

import java.util.logging.Logger;

/**
 * Move validation requires several things:
 * - a board to validate the move against
 * - a player, and the color of their pieces
 * - the move to validate
 *
 * Move validator consumes a CheckersGame, Move, and Player
 * and determines if the Move is a valid one
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
     * Logs the move as a pair of coordinates
     *
     * @param move - Move being made
     */
    private static void logMoveCoordinates(Move move) {
        LOG.finest(String.format("%s Player [%s] wants to move from %s",
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
     * :TODO implement is a valid jump move, For now return false for test
     * purposes assuming no valid jump move will me made
     *
     * @return - true if the move is a jump move, false otherwise
     */
    private static boolean isMoveJumpingAPiece(Space[][] board, Move move) {
        boolean conditionTruth = false;

        LOG.finest(String.format("Validate isMoveJumpingAPiece(): %s", conditionTruth));

    	return conditionTruth;
    }

	private static boolean areWeMovingMyPiece(Space[][] board, Move move) {
        boolean conditionTruth = false;

        Space start = getSpace(board, move.getStart());

        conditionTruth = start.getPiece().getColor() == move.getPieceColor();

        LOG.finest(String.format("Validate areWeMovingMyPiece(): %s", conditionTruth));

    	return conditionTruth;
    }
    /**
     * Board lookup convenience method - given a position it will return the enumerated state
     *
     * @param pos - end position of the move
     * @return SpaceState - current state of the position being moved to
     */
    private static Space getSpace(Space[][] matrix, Position pos) {
        return matrix[pos.getRow()][pos.getCell()];
    }

    /**
     * Checks to see if there are any available moves
     *
     * @return true if there are available moves, false otherwise
     */
    private boolean isMoveAvailable(Space[][] matrix){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                // If the space holds a piece of the color of the active player
                if(matrix[i][j].isOccupied()
                        && matrix[i][j].getPiece().getColor() == game.getPlayerColor(player)){
                    LOG.finest("Space is occupied, and the piece is the proper color");
                    Position currentPos = new Position(i, j);
                    Move checkMove;
                    for(int row = -1; row < 2; row += 2){
                        for(int col = -1; col < 2; col += 2){
                            LOG.finest("We're moving");
                            Position place = new Position(i + row, j + col);
                            checkMove = new Move(currentPos, place);
                            if(validateMoveByStep(checkMove))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
