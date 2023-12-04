package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 1035);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        new Thread(() -> {
            try {
                while (true) {
                    StringBuilder topicBuilder = new StringBuilder();
                    int byteCode;
                    while ((byteCode = inputStream.read()) != -1) {
                        char c = (char) byteCode;
                        if (c == '\n') {
                            break;
                        }
                        topicBuilder.append(c);
                    }
                    String topic = topicBuilder.toString();

                    int countBytes = Integer.parseInt(topic.trim().split(" ")[1]);
                    byte[] bytes = new byte[countBytes];

                    inputStream.read(bytes, 0, countBytes);

                    String message = new String(bytes);

                    System.out.println("Сообщение от сервера: " + message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите роль и тему");
        String temp = scanner.nextLine()+'\n';
        outputStream.write(temp.getBytes());
        outputStream.flush();

        System.out.println("Введите сообщение:");
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            byte[] data = message.getBytes();
            byte[] byteMessage = ("сообщение " + data.length + '\n').getBytes();

            outputStream.write(byteMessage);
            outputStream.write(data);
            outputStream.flush();
        }

        scanner.close();
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}