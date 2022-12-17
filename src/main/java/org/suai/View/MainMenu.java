package org.suai.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class MainMenu extends JFrame {
    protected static Socket socket;
    protected static PrintWriter out;
    protected static BufferedReader in;
    protected static String name;


    public MainMenu(String nam) {
        super("TIC-TAC-TOE");
        name = nam;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        JPanel contents = new JPanel();
        JLabel name = new JLabel(" TIC-TAC-TOE ");
        name.setFont(new Font("Serif", Font.PLAIN, 40));
        name.setPreferredSize(new Dimension(300, 100));
        JButton button1 = new JButton("CREATE NEW GAME");
        button1.setPreferredSize(new Dimension(500, 100));
        JButton button2 = new JButton("JOIN THE GAME");
        button2.setPreferredSize(new Dimension(500, 100));
        contents.add(name);
        contents.add(button1);
        contents.add(button2);
        ActionListener actionListener = new TestActionListener();
        button1.addActionListener(actionListener);
        ActionListener roomListener = new TestRoomListener();
        button2.addActionListener(roomListener);
        setContentPane(contents);
        setVisible(true);

    }


    public class TestActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
            JFrame chooseMenu = new JFrame();
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            JPanel contents = new JPanel();
            JLabel name = new JLabel("Choose game mode");
            name.setFont(new Font("Serif", Font.PLAIN, 50));
            name.setPreferredSize(new Dimension(300, 100));
            JButton button1 = new JButton("3X3");
            button1.setPreferredSize(new Dimension(500, 100));
            JButton button2 = new JButton("5X5");
            button2.setPreferredSize(new Dimension(500, 100));
            JButton button3 = new JButton("10X10");
            button3.setPreferredSize(new Dimension(500, 100));
            ActionListener first = new FirstButtonListener();
            button1.addActionListener(first);
            ActionListener second = new SecondButtonListener();
            button2.addActionListener(second);
            ActionListener third = new ThirdButtonListener();
            button3.addActionListener(third);
            contents.add(name);
            contents.add(button1);
            contents.add(button2);
            contents.add(button3);
            setContentPane(contents);
            setVisible(true);
        }
    }

    public class FirstButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            setVisible(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            JLabel name = new JLabel("Input room name");
            name.setFont(new Font("Serif", Font.PLAIN, 20));
            name.setPreferredSize(new Dimension(300, 100));
            JPanel contents = new JPanel();
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(100, 25));
            contents.add(txt);
            JButton button = new JButton("Create");
            button.setPreferredSize(new Dimension(100, 100));
            ActionListener push = new PushTextListener(3,txt);
            button.addActionListener(push);
            contents.add(button);
            contents.add(name);
            setContentPane(contents);
            setVisible(true);
        }

    }

    public class SecondButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            setVisible(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            JLabel name = new JLabel("Input room name");
            name.setFont(new Font("Serif", Font.PLAIN, 20));
            name.setPreferredSize(new Dimension(300, 100));
            JPanel contents = new JPanel();
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(100, 25));
            contents.add(txt);
            JButton button = new JButton("Create");
            button.setPreferredSize(new Dimension(100, 100));
            ActionListener push = new PushTextListener(5,txt);
            button.addActionListener(push);
            contents.add(button);
            contents.add(name);
            setContentPane(contents);
            setVisible(true);
        }
    }

    public class ThirdButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
            setVisible(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            JLabel name = new JLabel("Input room name");
            name.setFont(new Font("Serif", Font.PLAIN, 20));
            name.setPreferredSize(new Dimension(100, 100));
            JPanel contents = new JPanel();
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(100, 25));
            contents.add(txt);
            JButton button = new JButton("Create");
            button.setPreferredSize(new Dimension(100, 100));
            ActionListener push = new PushTextListener(10,txt);
            button.addActionListener(push);
            contents.add(button);
            contents.add(name);
            setContentPane(contents);
            setVisible(true);
        }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String nam = sc.nextLine();
            socket = new Socket("localhost", 5555);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("name-" + nam);
            new MainMenu(nam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public class TestRoomListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                out.println(name + ":room request");
                String str = in.readLine();
                if (str != null){
                    if (str.startsWith("room list:")) {
                        System.out.println(str);
                        String[] tmp = str.split(":");
                        if (tmp.length < 2){
                            JFrame frame = new JFrame("No rooms");
                            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
                            frame.setSize(600,400);
                            JPanel contents = new JPanel();
                            JLabel name = new JLabel("NO ROOMS YET");
                            name.setFont(new Font("Serif", Font.PLAIN, 30));
                            name.setPreferredSize(new Dimension(300, 100));
                            contents.add(name);
                            frame.add(contents);
                            frame.setVisible(true);
                            return;
                        }
                        String[] rooms = tmp[1].split(",");
                        setVisible(false);
                        dispose();
                        setDefaultCloseOperation(EXIT_ON_CLOSE);
                        setSize(600, 400);
                        JPanel contents = new JPanel();
                        JLabel name = new JLabel("Choose room");
                        contents.add(name);
                        name.setFont(new Font("Serif", Font.PLAIN, 50));
                        name.setPreferredSize(new Dimension(300, 100));
                        for (int i = 0; i < rooms.length; i++){
                            String[] roomInfo = rooms[i].split("-");
                            JButton button = new JButton(roomInfo[0] + ',' + roomInfo[1] + 'x' + roomInfo[1]);
                            ActionListener al = new CreateGameListener(roomInfo[0],Integer.parseInt(roomInfo[1]));
                            button.addActionListener(al);
                            button.setPreferredSize(new Dimension(500, 100));
                            contents.add(button);
                        }
                        setContentPane(contents);
                        setVisible(true);
                    }
                }

            }
            catch (IOException ignored) {
            }
        }
    }

    public class PushTextListener implements ActionListener{
        protected int size;
        protected JTextField jf;
        public PushTextListener(int s,JTextField tf){
            size = s;
            jf = tf;
        }

        public void actionPerformed(ActionEvent e){
            setVisible(false);
            dispose();
            String roomName = jf.getText().toString();
            out.println(name + ":create-" + roomName + "-" + size);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(600, 400);
            JPanel contents = new JPanel();
            JLabel nam = new JLabel(" Wait the opponent ");
            nam.setFont(new Font("Serif", Font.PLAIN, 40));
            nam.setPreferredSize(new Dimension(300, 100));
            contents.add(nam);
            setContentPane(contents);
            setVisible(true);
            try {
                String str = in.readLine();
                if (str.contains("connect")){
                    dispose();
                    setVisible(false);
                    String[] info = str.split(":");
                    String[] roomInfo = info[1].split("-");
                    new GameView(name,socket,roomInfo[0],Integer.parseInt(roomInfo[1]),roomInfo[2],roomInfo[3]);
                }
            }
            catch(IOException ignored){}
        }
    }


    public class CreateGameListener implements ActionListener {
        private String roomName;
        private int size;
        public CreateGameListener(String str, int sz){
            roomName = str;
            size = sz;
        }
        public void actionPerformed(ActionEvent e) {
            try {
                setVisible(false);
                dispose();
                out.println(name + ":join-" + roomName + "-" + size);
                String str = in.readLine();
                if (str.contains("connect")) {
                    String[] info = str.split(":");
                    String[] players = info[1].split("-");
                    new GameView(name,socket, roomName, size, players[2], players[3]);
                }
                else if (str.contains("refuse")){
                    JDialog d = new JDialog();
                    d.setLayout(new FlowLayout());
                    d.setSize(300, 300);
                    d.add(new Label("This room is already full"));
                    d.setVisible(true);
                }
            }
            catch (IOException ignored){}
        }
    }


}
