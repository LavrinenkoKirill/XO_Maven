
import org.suai.Model.Board;

import static org.junit.Assert.*;

public class BoardTest {

    @org.junit.Test
    public void checkMove() {
        Board brd = new Board(3);
        brd.doXMove(1,1);
        assertFalse(brd.checkMove(1,1));
    }

    @org.junit.Test
    public void doXMove() {
        Board brd = new Board(3);
        brd.doXMove(1,1);
        assertTrue(brd.getCell(1,1).isX());
    }

    public void doOMove() {
        Board brd = new Board(3);
        brd.doXMove(1,1);
        assertTrue(brd.getCell(1,1).isX());
    }


    @org.junit.Test
    public void isWin() {
        Board brd = new Board(3);
        brd.doXMove(0,0);
        brd.doOMove(1,1);
        brd.doXMove(0,1);
        brd.doOMove(2,1);
        brd.doXMove(0,2);
        assertEquals(Board.X_WIN,brd.isWin());
    }

    @org.junit.Test
    public void checkFreeCells() {
        Board brd = new Board(3);
        for (int i = 0; i < brd.getBoardSize(); i++){
            for (int j = 0; j < brd.getBoardSize(); j++){
                brd.doXMove(i,j);
            }
        }
        assertFalse(brd.CheckFreeCells());
    }

    @org.junit.Test
    public void getCell() {
        Board brd = new Board(5);
        assertNull(brd.getCell(5, 1));
    }

    @org.junit.Test
    public void getBoardSize() {
        Board brd = new Board(10);
        assertEquals(10,brd.getBoardSize());
    }

}