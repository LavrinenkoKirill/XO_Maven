package org.suai.Model;

public class Cell {
    public int state;


    public Cell(){
        this.state = Board.FREE;
    }

    public boolean isFREE(){
        return this.state == Board.FREE;
    }

    public boolean isX(){
        return this.state == Board.X;
    }

    public boolean isO(){
        return this.state == Board.O;
    }

    public boolean isF(){
        return this.state == Board.F;
    }

    public static boolean isValidCell(int row, int column) {
        return row >= 0 && row < Board.BOARD_SIZE && column >= 0 && column < Board.BOARD_SIZE;
    }



}
