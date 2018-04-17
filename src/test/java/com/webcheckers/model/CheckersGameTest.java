package com.webcheckers.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-tier")
public class CheckersGameTest {
    private Player red, white;
    private CheckersGame game;
    @BeforeEach
    public void setUp(){
        red = new Player("red");
        white = new Player("White");
        game = new CheckersGame(red, white);
    }

    @Test
    public void players(){
        assertEquals(red, game.getPlayerActive());
        assertEquals(red, game.getPlayerRed());
        assertEquals(white, game.getPlayerWhite());
    }

    @Test
    public void test_gets(){
        assertEquals(game.getState(), CheckersGame.State.IN_PLAY);
        assertEquals(game.getPlayerColor(red), Piece.Color.RED);
        assertEquals(game.getPlayerColor(white), Piece.Color.WHITE);
        assertEquals(red, game.getPlayerActive());
        CheckersBoardBuilder builder = CheckersBoardBuilder.aStartingBoard();
        Space[][] board = game.getBoard();
        assertEquals(board.length, builder.getBoard().length);
        assertEquals(CheckersBoardHelper.formatBoardString(board), CheckersBoardHelper.formatBoardString(builder.getBoard()));
        assertTrue(game.getTurn().isMyTurn(red));
    }

    @Test
    public void test_resigning(){
        assertFalse(game.isResigned());
        assertTrue(game.resignGame());
        assertTrue(game.isResigned());
    }

    @Test
    public void test_submitTurn(){
        Message test2 = new Message("It is not your turn", Message.MessageType.error);
        assertEquals(test2.toString(), game.submitTurn(white).toString());
        Move move = new Move(new Position(5,2), new Position(4,3));
        game.getTurn().validateMove(move);
        Message test = new Message("Turn has been finalized", Message.MessageType.info);
        assertEquals(test.toString(),game.submitTurn(red).toString());
        test = new Message(Turn.NO_MOVES_MSG, Message.MessageType.error);
        assertEquals(test.toString(), game.submitTurn(white).toString());
        move = new Move(new Position(2,5), new Position(3,4));
        game.getTurn().validateMove(move);
        game.submitTurn(white);
        assertEquals(red, game.getPlayerActive());
    }

    @Test
    public void test_fail(){
        Move move = new Move(new Position(5,2), new Position(4,3));
        game.getTurn().validateMove(move);
        assertFalse(game.resignGame());
        assertNull(game.getPlayerColor(new Player("OwO")));
    }

    @Test
    public void gameOver(){
        Player redPlayer = new Player("endGame");
        CheckersGame gameOver = new CheckersGame(redPlayer, white);
        Move move = new Move(new Position(1,0), new Position(3,2));
        gameOver.getTurn().validateMove(move);
        assertFalse(gameOver.isWon());
        assertEquals(Message.MessageType.info, gameOver.submitTurn(redPlayer).getType());
        assertEquals(gameOver.getPlayerRed(), gameOver.getWinner());
        assertEquals(gameOver.getPlayerWhite(), gameOver.getLoser());
        assertEquals(CheckersGame.State.WON, gameOver.getState());
        assertTrue(gameOver.isWon());
    }

    @Test
    public void unableToMove(){
        Player red = new Player("noMoreMoves");
        CheckersGame gameTest = new CheckersGame(red, white);
        Move move = new Move(new Position(2,1), new Position(1,0));
        Move move2 = new Move(new Position(1,2), new Position(2,3));
        gameTest.getTurn().validateMove(move);
        gameTest.submitTurn(red);
        gameTest.getTurn().validateMove(move2);
        gameTest.submitTurn(white);
        assertTrue(gameTest.isWon());
    }

    @Test
    public void changeActivePlayer_endGame() {
    	// This uses TestCheckersBoards to give us a board that will end in one turn
        red = new Player("singleJumpToEnd");

        game = new CheckersGame(red, white);

        Move endingMove = new Move(
                TestCheckersBoards.RED_PAWN_POSITION,
                // We are jumping up and to the right
                new Position(TestCheckersBoards.RED_PAWN_ROW - 2, TestCheckersBoards.RED_PAWN_CELL + 2));

        Message validatedMoveResponse = game.getTurn().validateMove(endingMove);
        assertEquals(Message.MessageType.info, validatedMoveResponse.getType());

        Message submittedTurnResponse = game.submitTurn(red);
        assertEquals(Message.MessageType.info, submittedTurnResponse.getType());


    }
}
