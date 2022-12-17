package org.suai.View;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    protected static ServerSocket serverSocket;
    protected static ArrayList<ClientThread> clients;
    protected static ArrayList<Room> rooms;

    static class ClientThread extends Thread {
        private Socket curSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String name;
        FileWriter writer;

        ClientThread(Socket s) {
            curSocket = s;
            try {
                writer = new FileWriter("serverLog.txt", true);
            }
            catch (IOException ignored){}
        }

        public synchronized void writeToFile(String str){
            try {
                str+='\n';
                writer.write(str);
                writer.flush();
            }
            catch (IOException ignored){};
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(curSocket.getInputStream()));
                out = new PrintWriter(curSocket.getOutputStream(), true);

                while (!curSocket.isClosed()) {
                    String command = in.readLine();
                    System.out.println(command);
                    if (command.contains("name")) {
                        String[] words = command.split("-");
                        name = words[1];
                        writeToFile(name + " connected to the server from address " + curSocket.getInetAddress());
                    }

                    if (command.contains("create")) {
                        String[] words = command.split("-");
                        Room rm = new Room(words[1], Integer.parseInt(words[2]));
                        String[] name = command.split(":");
                        rm.firstPlayer = name[0];
                        rooms.add(rm);
                        writeToFile(name[0] + " created the room " + words[1]);
                    }

                    if (command.contains("room request")) {
                        String roomList = "room list:";
                        for (int i = 0; i < rooms.size(); i++) {
                            if (!rooms.get(i).full) {
                                roomList += rooms.get(i).name + '-' + rooms.get(i).gameMode + ',';
                            }
                        }
                        String[] name = command.split(":");

                        for (int i = 0; i < clients.size(); i++) {
                            if (clients.get(i).name.equals(name[0])) {
                                clients.get(i).out.println(roomList);
                                clients.get(i).out.flush();
                                writeToFile(name[0] + " requested room list");
                            }
                        }
                    }

                    if (command.contains("join")) {
                        String[] name = command.split(":");
                        String[] words = command.split("-");
                        for (int i = 0; i < rooms.size(); i++) {
                            if (rooms.get(i).name.equals(words[1])) {
                                if (rooms.get(i).full) {
                                    for (int j = 0; j < clients.size(); j++) {
                                        if (clients.get(j).name.equals(name[0])) {
                                            clients.get(j).out.println("refuse");
                                            clients.get(j).out.flush();
                                            writeToFile(clients.get(j).name + " refused connection to room " + rooms.get(i).name);
                                        }
                                    }
                                } else {
                                    rooms.get(i).secondPlayer = name[0];
                                    rooms.get(i).full = true;
                                    for (int j = 0; j < clients.size(); j++) {
                                        if (clients.get(j).name.equals(rooms.get(i).firstPlayer) || clients.get(j).name.equals(rooms.get(i).secondPlayer)) {
                                            clients.get(j).out.println("connect:" + rooms.get(i).name + "-" + rooms.get(i).gameMode + "-" + rooms.get(i).firstPlayer + "-" + rooms.get(i).secondPlayer);
                                            clients.get(j).out.flush();
                                            writeToFile(clients.get(j).name + " joined " + rooms.get(i).name);
                                        }
                                    }
                                }

                            }
                        }


                    }


                    if (command.contains("move")) {
                        String[] name = command.split(":");
                        String[] info = name[1].split("-");
                        for (int i = 0; i < rooms.size(); i++) {
                            if (rooms.get(i).name.equals(info[0])) {
                                String receiver;
                                if (name[0].equals(rooms.get(i).firstPlayer)) receiver = rooms.get(i).secondPlayer;
                                else receiver = rooms.get(i).firstPlayer;
                                System.out.println(receiver);
                                for (int j = 0; j < clients.size(); j++) {
                                    if (clients.get(j).name.equals(receiver)) {
                                        clients.get(j).out.println(command);
                                        clients.get(j).out.flush();
                                        writeToFile(command);
                                    }
                                }
                            }
                        }
                    }

                    if (command.contains("disconnect")) {
                        String[] name = command.split(":");
                        String[] info = name[1].split("-");
                        for (int i = 0; i < rooms.size(); i++) {
                            if (rooms.get(i).name.equals(info[0])) {
                                String receiver;
                                if (name[0].equals(rooms.get(i).firstPlayer)) receiver = rooms.get(i).secondPlayer;
                                else receiver = rooms.get(i).firstPlayer;
                                writeToFile(name[0] + " and " + receiver + " disconnected from the server");
                                for (int j = 0; j < clients.size(); j++) {
                                    if (clients.get(j).name.equals(receiver)) {
                                        clients.get(j).out.println(command);
                                        clients.get(j).out.flush();
                                        clients.get(j).curSocket.close();
                                        clients.remove(j);
                                    }
                                    if (clients.get(j).name.equals(name[0])){
                                        clients.get(j).curSocket.close();
                                        clients.remove(j);
                                    };
                                }
                                rooms.remove(i);
                                break;
                            }
                        }

                    }


                }
                in.close();
                out.close();
                curSocket.close();
                System.out.println("Disconnect!");
            } catch (IOException ignored) {}
        }

    }


    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(5555);
            clients = new ArrayList<>();
            rooms = new ArrayList<>();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected!");
                ClientThread cliThread = new ClientThread(clientSocket);
                clients.add(cliThread);
                cliThread.start();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    protected static class Room{
        protected String name;
        protected int gameMode;
        protected boolean full;
        protected String firstPlayer;
        protected String secondPlayer;

        public Room(String str,int size){
            name = str;
            gameMode = size;
            full = false;
        }
    }


}
