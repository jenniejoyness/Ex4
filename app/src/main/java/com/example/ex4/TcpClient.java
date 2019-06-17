package com.example.ex4;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient {
    private Socket socket;
    private static TcpClient s_instance = null;
    PrintWriter out;

    public static TcpClient Instance() {
        if (s_instance == null){
            s_instance = new TcpClient();
        }
        return s_instance;
    }

    public void connectToServer(final String ip, final int port) {
        final Thread outThread = new Thread() {
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    System.out.println("connected");
                    out = new PrintWriter(socket.getOutputStream());

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        };
        outThread.start();
    }

    public void sendMesssage(final String message) {
        final Thread outThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Started...");
                try {
                    String messageStr = message + "\r\n";
                    out.println(messageStr);
                    out.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        outThread.start();
    }

    public void disConnect(){
        if (out != null) {
            out.close();
        }
        try {
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
