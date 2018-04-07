package com.webcheckers.model;

import java.util.logging.Logger;

/**
 * Move validator controller consumes a CheckersGame, Move, and Player
 * and determines if the Move is a valid one
 */
public class MoveValidator {

    private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());

    private Player player;
    private Piece.Color playerColor;


    /**
     * MoveValidator constructor for seeding info needed for the algorithm
     *
     * @param color  - active player's color
     * @param player - Player whose turn it is
     */
    public MoveValidator(Player player, Piece.Color color) {

        this.player = player;
        this.playerColor = color;

        LOG.fine(String.format("MoveValidator initialized for Player [%s]", player.getName()));
//        LOG.finest(String.format("matrix is: %s", this.matrix));
    }

    /**
     * Entrypoint to move validation algorithm - kicks off the process
     *
     * @return - true if the move is valid
     */
    public boolean validateMove(Space[][] matrix, Move move) {

        LOG.fine(String.format("Validating move for Player [%s]", player.getName()));

        logBoardMatrix(matrix);
        logMoveCoordinates(move);
        logMoveStates(matrix, move);

        return validateMoveByStep(matrix, move);
    }

    /**
     * Facade of move validation steps
     *
     * @return - boolean if the given move is a valid one or not
     */
    private boolean validateMoveByStep(Space[][] matrix, Move move) {

        return isMoveDiagonal(move) &&
                (isMoveSingleSpace(move) || isMoveJump(move)) &&
                isMoveInRightDirection(matrix, move) &&
                isEndSpaceOpen(matrix, move);
    }

    /**
     * Logs the matrix board for debug
     */
    private static void logBoardMatrix(Space[][] matrix) {
        // Trace log our board matrix
        for (Space[] row : matrix) {
            StringBuilder rowStates = new StringBuilder();

            for (Space space : row) {
                rowStates.append(space.getState() + " ");
            }

            LOG.finest(rowStates.toString());
        }
    }

    /**
     * Logs the move as a pair of coordinates
     *
     * @param move - Move being made
     */
    private void logMoveCoordinates(Move move) {
        // "RED Player [username] wants to move from <0,1> to <1,2>"
        LOG.finest(String.format("%s Player [%s] wants to move from %s",
                playerColor,
                player.getName(),
                move.toString()));
    }

    /**
     * Logs the current state of affairs at the two spaces involved in the move
     */
    private void logMoveStates(Space[][] matrix, Move move) {
        Space startSpace = getSpace(matrix, move.getStart());
        Space endSpace = getSpace(matrix, move.getEnd());

        LOG.finest(String.format("Starting position state is [%s]", startSpace.getState()));
        LOG.finest(String.format("End position state is [%s]", endSpace.getState()));

    }


    /**
     * MOVE VALIDATION STEPS
     */

    /**
     * Given a move is the end position open
     *
     * @param move - Move being made
     * @return - true if the space being moved to is a valid, unoccupied space
     */
    private boolean isEndSpaceOpen(Space[][] matrix, Move move) {
        Space endSpace = getSpace(matrix, move.getEnd());

        return endSpace.isOpen();
    }

    /**
     * Determines if the piece is moved in the right direction
     *
     * @param move - Move being made
     * @return - True, if it does, false otherwise
     */
    private boolean isMoveInRightDirection(Space[][] matrix, Move move) {
        Piece piece = getSpace(matrix, move.getStart()).getPiece();

        // If the piece is a king then they can move bi-directionally
        if (piece != null && piece.getType() == Piece.Type.KING) {
            return true;
        }

        // row for white player must increase
        // row for red player must decrease

        int startRow = move.getStartRow();
        int endRow = move.getEndRow();

        switch (playerColor) {
            case RED:
                return (endRow < startRow);
            case WHITE:
                return (endRow > startRow);
        }

        return false;
    }

    /**
     * Checks to see if we are only moving one space away
     *
     * @param move - Move being made
     * @return boolean - true if the move is moving one space away from start position, false otherwise
     */
    private boolean isMoveSingleSpace(Move move) {
        int deltaY = Math.abs(move.getStartRow() - move.getEndRow());
        int deltaX = Math.abs(move.getStartCell() - move.getEndCell());

        LOG.finest(String.format("Move distance is %d rows and %d cells", deltaY, deltaX));

        return (deltaY == 1 && deltaX == 1);

    }

    /**
     * All moves must be diagonal, therefore rise==run
     *
     * @param move - Move being made
     * @return boolean - true if the move being made is diagonal from the starting position
     */
    private boolean isMoveDiagonal(Move move) {
        int deltaY = Math.abs(move.getStartRow() - move.getEndRow());
        int deltaX = Math.abs(move.getStartCell() - move.getEndCell());

        return (deltaY == deltaX);
    }

    /**
     * :TODO implement is a valid jump move, For now return false for test
     * purposes assuming no valid jump move will me made
     *
     * @return - true if the move is a jump move, false otherwise
     */
    private boolean isMoveJump(Move move) {
        return move.isAJumpMoveAttempt();
    }

    /**
     * Matrix lookup function - given a position it will return the enumerated state
     *
     * @param pos - end position of the move
     * @return SpaceState - current state of the position being moved to
     */
    private Space getSpace(Space[][] matrix, Position pos) {
        return matrix[pos.getRow()][pos.getCell()];
    }
}
