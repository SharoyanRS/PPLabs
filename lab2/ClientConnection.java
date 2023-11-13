package org.example;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class ClientConnection {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;
    PrintWriter writer;

    private String myTopic = null;

    private static Pattern pattern = Pattern.compile("([a-z]+)( )(.+)(\\n)");

    public ClientConnection(Socket socket){
        this.socket = socket;
        new Thread(this::work).start();
    }

    private void work(){

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(in));

            String text;

            while (true){
                text = reader.readLine();
                if (text == "Disconnect") break;

            }

            reader.close();
            writer.close();
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("чёто не так");
        }
        finally {
            Server.removeFromTopic(myTopic,this);
            Thread.currentThread().interrupt();
        }

    }

    public void send(String msg){

    }


}
