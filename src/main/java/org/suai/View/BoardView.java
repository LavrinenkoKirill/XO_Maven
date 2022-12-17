package org.suai.View;


import org.suai.Model.Board;
import org.suai.Model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BoardView extends JButton {
    protected static Board board;
    private static final int PADDING = 16;
    private GameView view;
    private static boolean yourMove;
    protected static PrintWriter out;
    protected static BufferedReader in;

    public BoardView(Board b,GameView vw){
        board = b;
        repaint();
        view = vw;
        this.addActionListener(new MouseListener());
        yourMove = GameView.playerName.equals(GameView.firstPlayer);
        Thread receive = new Thread(new ClientReceive());
        receive.start();
        try {
            out = new PrintWriter(GameView.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(GameView.socket.getInputStream()));
        }
        catch (IOException ignored){}
    }

    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        final int BOX_PADDING = 4;
        final int W = getWidth(), H = getHeight();
        final int DIM = W < H? W : H, BOX_SIZE = (DIM - 2 * PADDING) / board.getBoardSize();
        final int OFFSET_X = (W - BOX_SIZE * board.getBoardSize()) / 2;
        final int OFFSET_Y = (H - BOX_SIZE * board.getBoardSize()) / 2;
        final int CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

        g.setColor(Color.BLACK);
        g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, BOX_SIZE * board.getBoardSize() + 1, BOX_SIZE * board.getBoardSize() + 1);
        g.setColor(Color.WHITE);
        g.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * board.getBoardSize(), BOX_SIZE * board.getBoardSize());
        g.setColor(Color.BLACK);

        for (int x = 0; x < board.getBoardSize(); x++){
            g.drawLine(OFFSET_X + x * BOX_SIZE,OFFSET_Y,OFFSET_X + x * BOX_SIZE,OFFSET_Y + board.getBoardSize() * BOX_SIZE );
        }

        for (int y = 0; y < board.getBoardSize(); y++){
            g.drawLine(OFFSET_X, OFFSET_Y + y * BOX_SIZE,OFFSET_X + board.getBoardSize() * BOX_SIZE, OFFSET_Y + y * BOX_SIZE );
        }


        for (int i = 0; i < board.getBoardSize(); i++){
            int cy = OFFSET_X + i * BOX_SIZE + BOX_PADDING;
            for (int j = 0; j < board.getBoardSize(); j++){
                int cx = OFFSET_Y + j * BOX_SIZE + BOX_PADDING;
                if (board.getCell(j, i).isFREE()) {
                    continue;
                }
                if (board.getCell(j, i).isX()) {
                    g.setColor(Color.BLUE);
                    g.drawLine(cy + 2, cx + 1, cy + 2 + CHECKER_SIZE, cx + 1 + CHECKER_SIZE);
                    g.drawLine(cy + 2 + CHECKER_SIZE,cx + 1, cy + 2,cx + 1 + CHECKER_SIZE);
                }

                if (board.getCell(j, i).isO()) {
                    g.setColor(Color.RED);
                    g.drawOval(cy + 2, cx + 1, CHECKER_SIZE, CHECKER_SIZE);
                }

                if (board.getCell(j,i).isF()){
                    g.setColor(Color.GREEN);
                    g.fillRect(OFFSET_X + i * BOX_SIZE, OFFSET_Y + j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
            //  String player = board.isXTurn()? "X TURN" : "O TURN";
            String player = GameView.playerName;
            if (board.getBoardSize() != 3) {
                player += ", X_COUNTER = " + board.X_COUNTER;
                player += ", O_COUNTER = " + board.O_COUNTER;
            }
            int width = g.getFontMetrics().stringWidth(player);
            g.setColor(Color.BLACK);
            g.drawString(player, W / 2 - width / 2, OFFSET_Y + board.getBoardSize() * BOX_SIZE + 2 + 11);


        }


    }


    private class MouseListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Point m = BoardView.this.getMousePosition();
            if (m != null) {
                MouseClick(m.x, m.y);
            }
        }
    }

    private void MouseClick(int x, int y) {
        if (!yourMove) {
            return;
        }
        else if (board.isWin() != Board.CONTINUE) {
            return;
        }

        else {

            final int W = getWidth(), H = getHeight();
            final int DIM = W < H ? W : H, BOX_SIZE = (DIM - 2 * PADDING) / board.getBoardSize();
            final int OFFSET_X = (W - BOX_SIZE * board.getBoardSize()) / 2;
            final int OFFSET_Y = (H - BOX_SIZE * board.getBoardSize()) / 2;
            x = (x - OFFSET_X) / BOX_SIZE;
            y = (y - OFFSET_Y) / BOX_SIZE;
            if (board.checkMove(y, x)) {
                if (GameView.playerName.equals(GameView.firstPlayer))board.doXMove(y,x);
                else board.doOMove(y,x);
                out.println(GameView.playerName + ":" + GameView.roomName + "-" + "move-" + y + "-" + x);
                yourMove = false;
                if (board.isWin() != Board.CONTINUE) {
                    String victory;
                    if (board.isWin() == Board.X_WIN) victory = "Game Over! X VICTORY";
                    else if (board.isWin() == Board.O_WIN) victory = "Game Over! O VICTORY";
                    else victory = "Game over! TIE SCORE";
                    new PostGameMenu(view, victory);
                }
                repaint();

            } else {
                System.out.println("Incorrect move");
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
                        if (GameView.playerName.equals(GameView.firstPlayer))
                            board.doOMove(Integer.parseInt(move[2]), Integer.parseInt(move[3]));
                        else board.doXMove(Integer.parseInt(move[2]), Integer.parseInt(move[3]));
                        if (board.isWin() != Board.CONTINUE) {
                            String victory;
                            if (board.isWin() == Board.X_WIN) victory = "Game Over! X VICTORY";
                            else if (board.isWin() == Board.O_WIN) victory = "Game Over! O VICTORY";
                            else victory = "Game over! TIE SCORE";
                            new PostGameMenu(view, victory);
                        }
                        repaint();
                        yourMove = true;
                    }
                    else if (opponentMove.contains("disconnect")){
                        GameView.socket.close();
                        in.close();
                        out.close();
                        view.setVisible(false);
                        view.dispose();
                        new DisconnectMenu(view);
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }






}