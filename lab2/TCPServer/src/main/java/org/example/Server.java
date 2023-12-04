package org.example;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    public static HashMap<String, BlockingQueue<byte[]>> queueMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1035);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            new Thread(() -> {
                try {
                    worker(clientSocket);
                } catch (IOException e) {
                    System.out.println("клиент дропнулся");;
                }
            }).start();
        }
    }

    private static void worker(Socket clientSocket) throws IOException {

        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        try {
            StringBuilder confBuilder = new StringBuilder();
            int byteCode;
            while ((byteCode = inputStream.read()) != -1 && (char) byteCode != '\n') {
                confBuilder.append((char) byteCode);
            }

            String conf = confBuilder.toString();
            String role = conf.split(" ")[0];
            String topic = conf.split(" ")[1];

            System.out.println("роль: " + role + " тема: " + topic);

            if (!queueMap.containsKey(topic)) {
                System.out.println("Создана тема: " + topic);
                queueMap.put(topic, new LinkedBlockingQueue<>());
            }

            if (role.equals("receive")) {
                recieve(topic,outputStream);
            }

            while (true) {
                StringBuilder messageBuilder = new StringBuilder();
                byte bit;
                while ((bit = (byte) inputStream.read()) != -1 && (char) bit != '\n') {
                    messageBuilder.append((char) bit);
                }

                String message = messageBuilder.toString();
                int countBytes = Integer.parseInt(message.trim().split(" ")[1]);
                byte[] bytes = new byte[countBytes];

                inputStream.read(bytes, 0, countBytes);

                System.out.println("Сообщение " + new String(bytes) + " добавлено в тему "+ topic);
                queueMap.get(topic).put(bytes);
            }
        } catch (Exception e) {
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        }
        finally {
            System.out.println("клиент дропнулся");
        }
    }

    private static void recieve(String topic, OutputStream outputStream) throws IOException, InterruptedException {
        while (true) {
            byte[] bytes = queueMap.get(topic).take();
            String title = "message " + bytes.length + '\n';
            outputStream.write(title.getBytes());
            outputStream.write(bytes);
            outputStream.flush();
        }
    }
}