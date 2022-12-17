import org.suai.Model.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
public class CellTest {

    @org.junit.Test
    public void isFREE() {
        Cell cell = new Cell();
        assertTrue(cell.isFREE());
    }

    @org.junit.Test
    public void isX() {
        Cell cell = new Cell();
        cell.state = Board.X;
        assertTrue(cell.isX());
    }

    @org.junit.Test
    public void isO() {
        Cell cell = new Cell();
        cell.state = Board.O;
        assertTrue(cell.isO());
    }

    @org.junit.Test
    public void isF() {
        Cell cell = new Cell();
        cell.state = Board.F;
        assertTrue(cell.isF());
    }

    @org.junit.Test
    public void isValidCell() {
        Cell cell = new Cell();
        assertFalse(cell.isValidCell(20,20));
    }
}