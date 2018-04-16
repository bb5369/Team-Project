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
    @Disabled
    public void gameOver(){
        //ToDo: create a board to Win a Thing
    }
}
