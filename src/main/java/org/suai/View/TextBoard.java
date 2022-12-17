package org.suai.View;

import org.suai.Model.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TextBoard {
    protected static String playerName;
    protected static Socket socket;
    protected static String roomName;
    protected int size;
    protected static String firstPlayer;
    protected static String secondPlayer;
    protected Board board;
    protected static PrintWriter out;
    protected static BufferedReader in;
    private static boolean yourMove;


    public TextBoard(String playerN,Socket sc, String name, int sz, String fp, String sp) {
        playerName = playerN;
        socket = sc;
        roomName = name;
        size = sz;
        firstPlayer = fp;
        secondPlayer = sp;
        yourMove = playerName.equals(fp);
        board = new Board(size);
        board.printBoard();
        Thread receive = new Thread(new ClientReceive());
        receive.start();
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException ignored){}

        if (yourMove) doMove();
    }

    private void doMove() {
        if (!yourMove) {
            return;
        }
        else if (board.isWin() != Board.CONTINUE) {
            return;
        }

        else {
            Scanner sc = new Scanner(System.in);
            while(true) {
                System.out.println("Input string like:i-j ,where i is row and j is column");
                String move = sc.nextLine();
                String[] coord = move.split("-");
                try {
                    int x = Integer.parseInt(coord[0]);
                    int y = Integer.parseInt(coord[1]);

                    if (board.checkMove(x, y)) {
                        if (playerName.equals(firstPlayer)) board.doXMove(x,y);
                        else board.doOMove(x,y);
                        out.println(playerName + ":" + roomName + "-" + "move-" + x + "-" + y);
                        yourMove = false;
                        board.printBoard();
                        if (board.isWin() != Board.CONTINUE) {
                            String victory;
                            if (board.isWin() == Board.X_WIN) victory = "Game Over! X VICTORY";
                            else if (board.isWin() == Board.O_WIN) victory = "Game Over! O VICTORY";
                            else victory = "Game over! TIE SCORE";
                            System.out.println(victory);
                        }
                        break;
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Incorrect input");
                    continue;
                }


            }


        }

    }

    public class ClientReceive implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String opponentMove = in.readLine();
                    if (opponentMove.contains("move")) {
                        System.out.println(opponentMove);
                        String[] info = opponentMove.split(":");
                        String[] move = info[1].split("-");

                        if (playerName.equals(firstPlayer)) board.doOMove(Integer.parseInt(move[2]),Integer.parseInt(move[3]));
                        else board.doXMove(Integer.parseInt(move[2]),Integer.parseInt(move[3]));
                        board.printBoard();
                        if (board.isWin() != Board.CONTINUE) {
                            String victory;
                            if (board.isWin() == Board.X_WIN) victory = "Game Over! X VICTORY";
                            else if (board.isWin() == Board.O_WIN) victory = "Game Over! O VICTORY";
                            else victory = "Game over! TIE SCORE";
                            System.out.println(victory);
                        }
                        yourMove = true;
                        doMove();
                    }
                    else if (opponentMove.contains("disconnect")){

                    }
                }
            }
            catch (IOException ignored) {}

        }
    }



}
