package org.suai.Model;

public class Board {
    protected static int BOARD_SIZE;
    protected Cell[][] board;
    public static final int FREE = 0;
    public static final int X = 1;
    public static final int O = 2;
    public static final int F = 3;


    public static final int X_WIN = 0;
    public static final int O_WIN = 1;
    public static final int CONTINUE = 2;
    public static final int TIE = 3;

    public static int X_COUNTER = 0;
    public static int O_COUNTER = 0;

    //  protected int move = X;

    public Board(int size){
        if (size == 3 || size == 5 || size == 10){
            BOARD_SIZE = size;
            board = new Cell[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++){
                for (int j = 0; j < BOARD_SIZE; j++){
                    board[i][j] = new Cell();
                }
            }
        }
        else {
            System.out.println("Incorrect game mode");
        }

    }

    public boolean checkMove(int row, int column){
        if (row < 0 || row > BOARD_SIZE - 1 || column < 0 || column > BOARD_SIZE - 1) {
            System.out.println("Out of bounds");
            return false;
        }
        return board[row][column].isFREE();
    }

    public void doXMove(int row,int column){
        board[row][column].state = X;
        return;
    }
    public void doOMove(int row,int column){
        board[row][column].state = O;
        return;
    }


    public int isWin(){
        if (BOARD_SIZE == 3){
            for (int a = 0; a < 8; a++) {
                switch (a) {
                    case 0:
                        if (board[0][0].isX() && board[0][1].isX() && board[0][2].isX()) return X_WIN;
                        else if (board[0][0].isO() && board[0][1].isO() && board[0][2].isO()) return O_WIN;
                    case 1:
                        if (board[1][0].isX() && board[1][1].isX() && board[1][2].isX()) return X_WIN;
                        else if (board[1][0].isO() && board[1][1].isO() && board[1][2].isO()) return O_WIN;
                    case 2:
                        if (board[2][0].isX() && board[2][1].isX() && board[2][2].isX()) return X_WIN;
                        else if (board[2][0].isO() && board[2][1].isO() && board[2][2].isO()) return O_WIN;
                    case 3:
                        if (board[0][0].isX() && board[1][0].isX() && board[2][0].isX()) return X_WIN;
                        else if (board[0][0].isO() && board[1][0].isO() && board[2][0].isO()) return O_WIN;
                    case 4:
                        if (board[0][1].isX() && board[1][1].isX() && board[2][1].isX()) return X_WIN;
                        else if (board[0][1].isO() && board[1][1].isO() && board[2][1].isO()) return O_WIN;
                    case 5:
                        if (board[0][2].isX() && board[1][2].isX() && board[2][2].isX()) return X_WIN;
                        else if (board[0][2].isO() && board[1][2].isO() && board[2][2].isO()) return O_WIN;
                    case 6:
                        if (board[0][0].isX() && board[1][1].isX() && board[2][2].isX()) return X_WIN;
                        else if (board[0][0].isO() && board[1][1].isO() && board[2][2].isO()) return O_WIN;
                    case 7:
                        if (board[0][2].isX() && board[1][1].isX() && board[2][0].isX()) return X_WIN;
                        else if (board[0][2].isO() && board[1][1].isO() && board[2][0].isO()) return O_WIN;
                }
            }

            if (CheckFreeCells()) return CONTINUE;
            else return TIE;
        }

        else {
            for (int i = 0; i < BOARD_SIZE; i++){
                for (int j = 0; j < BOARD_SIZE; j++){
                    if (j != 0 && j != BOARD_SIZE - 1){
                        if (board[i][j].isX() && board[i][j - 1].isX() && board[i][j + 1].isX()){
                            X_COUNTER++;
                            board[i][j].state = F;
                            board[i][j - 1].state = F;
                            board[i][j + 1].state = F;
                        }

                        if (board[i][j].isO() && board[i][j - 1].isO() && board[i][j + 1].isO()){
                            O_COUNTER++;
                            board[i][j].state = F;
                            board[i][j - 1].state = F;
                            board[i][j + 1].state = F;
                        }

                    }
                    if (i != 0 && i != BOARD_SIZE - 1){
                        if (board[i][j].isX() && board[i - 1][j].isX() && board[i + 1][j].isX()){
                            X_COUNTER++;
                            board[i][j].state = F;
                            board[i - 1][j].state = F;
                            board[i + 1][j].state = F;
                        }

                        if (board[i][j].isO() && board[i - 1][j].isO() && board[i + 1][j].isO()){
                            O_COUNTER++;
                            board[i][j].state = F;
                            board[i - 1][j].state = F;
                            board[i + 1][j].state = F;
                        }
                    }

                    if (i != 0 && i != BOARD_SIZE - 1 && j != 0 && j != BOARD_SIZE - 1){
                        if (board[i - 1][j - 1].isX() && board[i][j].isX() && board[i + 1][j + 1].isX()){
                            X_COUNTER++;
                            board[i - 1][j - 1].state = F;
                            board[i][j].state = F;
                            board[i + 1][j + 1].state = F;
                        }

                        if (board[i - 1][j - 1].isO() && board[i][j].isO() && board[i + 1][j + 1].isO()){
                            O_COUNTER++;
                            board[i - 1][j - 1].state = F;
                            board[i][j].state = F;
                            board[i + 1][j + 1].state = F;
                        }

                        if (board[i][j].isX() && board[i - 1][j + 1].isX() && board[i + 1][j - 1].isX()){
                            X_COUNTER++;
                            board[i][j].state = F;
                            board[i - 1][j + 1].state = F;
                            board[i + 1][j - 1].state = F;
                        }

                        if (board[i][j].isO() && board[i - 1][j + 1].isO() && board[i + 1][j - 1].isO()){
                            O_COUNTER++;
                            board[i][j].state = F;
                            board[i - 1][j + 1].state = F;
                            board[i + 1][j - 1].state = F;
                        }

                    }
                }
            }

            if (CheckFreeCells()) return CONTINUE;
            else {
                if (X_COUNTER > O_COUNTER) return X_WIN;
                else if (X_COUNTER == O_COUNTER) return TIE;
                else return O_WIN;
            }
        }
    }


    public boolean CheckFreeCells(){
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[i][j].isFREE()) return true;
            }
        }
        return false;
    }

    public Cell getCell(int row,int column){
        try {
            if (row < 0 || row > BOARD_SIZE - 1 || column < 0 || column > BOARD_SIZE - 1) {
                throw new Exception ("Out of bounds in getCell");
            }
        }
        catch(Exception e){
            System.out.println("error: " + e.getMessage());
            return null;
        }
        return board[row][column];
    }

    public void printBoard()
    {
        if (BOARD_SIZE == 3)System.out.println("|---||---||---|");
        else if (BOARD_SIZE == 5) System.out.println("|---||---||---||---||---|");
        else if (BOARD_SIZE == 10) System.out.println("|---||---||---||---||---||---||---||---||---||---|");
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[i][j].isFREE()) System.out.print("| " + j + " |");
                else if (board[i][j].isX()) System.out.print("| X |");
                else if (board[i][j].isO())System.out.print("| O |");
                else System.out.print("| F |");
            }
            System.out.print('\n');
        }
        if (BOARD_SIZE == 3)System.out.println("|---||---||---|");
        else if (BOARD_SIZE == 5) System.out.println("|---||---||---||---||---|");
        else if (BOARD_SIZE == 10) System.out.println("|---||---||---||---||---||---||---||---||---||---|");
    }


    public int getBoardSize(){
        return BOARD_SIZE;
    }

    // public boolean isXTurn(){
    //   return move == X;
    // }


}