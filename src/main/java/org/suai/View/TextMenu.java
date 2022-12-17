package org.suai.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TextMenu {
    protected static Socket socket;
    protected static PrintWriter out;
    protected static BufferedReader in;
    protected static String name;


    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String nam = sc.nextLine();
            socket = new Socket("localhost", 5555);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("name-" + nam);
            new TextMenu(nam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public TextMenu(String nam){
        name = nam;
        out.println("name-" + nam);
        while(true) {
            System.out.println("Input string in type:create-roomName-gameMode to create a room");
            System.out.println("Input:room request to get a full list of available rooms");
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();
            if (command.contains("create")){
                String[] info = command.split("-");
                if (info.length == 3){
                    if (info[2].equals("3") || info[2].equals("5") || info[2].equals("10")) {
                        out.println(name + ":" + command);
                        System.out.println("Wait for the opponent");
                        try {
                            String line = in.readLine();
                            if (line.contains("connect")) {
                                String[] nm = line.split(":");
                                String[] infor = nm[1].split("-");
                                new TextBoard(name,socket,infor[0],Integer.parseInt(infor[1]),infor[2],infor[3]);
                                break;
                            }
                        } catch (IOException ignored) {
                        }
                    }

                }
            }
            else if (command.contains("room request")){
                out.println(name + ":" + command);
                try {
                    String list = in.readLine();
                    System.out.println("Room list");
                    String[] str = list.split(":");
                    String[] rooms = str[1].split(",");
                    for (int i = 0; i < rooms.length; i++){
                        String[] info = rooms[i].split("-");
                        System.out.println(info[0] + " with mode " + info[1] + "x" + info[1]);
                    }
                    String request;
                    while(true) {
                        boolean flag = false;
                        System.out.println("Input string type:roomName");
                        request = sc.nextLine();
                        for (int i = 0; i < rooms.length; i++){
                            String[] info = rooms[i].split("-");
                            if (request.equals(info[0])) flag = true;
                        }
                        if (flag) break;
                    }
                    out.println(name + ":join-" + request);
                    String vv = in.readLine();
                    if (vv.contains("connect")) {
                        String[] nm = vv.split(":");
                        String[] info = nm[1].split("-");
                        new TextBoard(name,socket,info[0],Integer.parseInt(info[1]),info[2],info[3]);
                        break;
                    }
                    else if (vv.contains("refuse")){
                        System.out.println("This room is no longer available");
                    }


                }
                catch (IOException ignored){}
            }

        }



    }

}
