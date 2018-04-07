package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@Tag("Model-tier")
public class PieceTest {

    //private enum type {SINGLE, KING}
    //private enum color {RED, WHITE}

    private Piece.Type singleType = Piece.Type.SINGLE;
    private Piece.Type kingType = Piece.Type.KING;
    private Piece.Color redColor = Piece.Color.RED;
    private Piece.Color whiteColor = Piece.Color.WHITE;

    public Piece singleRed = new Piece(singleType, redColor);
    public Piece kingWhite = new Piece(kingType, whiteColor);


    /*
     * Test that the Piece constructor works
     */
    @Test
    public void createPiece(){
        assertSame(singleRed.getType(), singleType);
        assertSame(singleRed.getColor(), redColor);
    }

    @Test
    public void toTest(){
        assertEquals(singleRed.toString(), "Piece: "
                + ((redColor == Piece.Color.RED) ? "Red " : "White ")
                + ((singleType == Piece.Type.SINGLE) ? "Single" : "King"));

        assertEquals(kingWhite.toString(), "Piece: "
                + ((whiteColor == Piece.Color.RED) ? "Red " : "White ")
                + ((kingType == Piece.Type.SINGLE) ? "Single" : "King"));
    }

    @Test
    public void testClone()
    {
        Piece toClone = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
        Piece clone = toClone.clone();
        assertEquals(toClone, clone);
    }
}
