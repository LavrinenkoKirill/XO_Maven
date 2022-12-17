package org.suai.View;


import org.suai.Model.Board;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GameView extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 600;
    protected static String playerName;
    protected static Socket socket;
    protected static String roomName;
    protected int size;
    protected static String firstPlayer;
    protected static String secondPlayer;


    public GameView(String playerN,Socket sc, String name, int sz, String fp, String sp){
        super("TIC-TAC-TOE");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        playerName = playerN;
        socket = sc;
        roomName = name;
        size = sz;
        firstPlayer = fp;
        secondPlayer = sp;
        Board brd = new Board(size);
        BoardView board = new BoardView(brd,this);
        setContentPane(board);
        addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                JFrame frame = (JFrame)e.getSource();

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to exit the application?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    try {
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        out.println(playerName + ":" + roomName + "-disconnect");
                        socket.close();
                        out.close();
                    }
                    catch (IOException ignored){}

                }
            }
        });
        setVisible(true);
    }




}
