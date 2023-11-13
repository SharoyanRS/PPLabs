package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private static HashMap<String, ArrayList<ClientConnection>> topicSenders = new HashMap<>();
    private static HashMap<String, ArrayList<ClientConnection>> topicRecievers = new HashMap<>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        
    }

    public static synchronized void putTopicSender(String topic, ClientConnection sender){
        ArrayList<ClientConnection> temp = new ArrayList<>();
        temp.add(sender);
        if (topicSenders.containsKey(topic)) temp.addAll(topicSenders.get(topic));
        topicSenders.put(topic,temp);
    }

    public static synchronized void putTopicReciever(String topic, ClientConnection reciever) {
        ArrayList<ClientConnection> temp = new ArrayList<>();
        temp.add(reciever);
        if (topicRecievers.containsKey(topic)) temp.addAll(topicRecievers.get(topic));
        topicRecievers.put(topic, temp);
    }

    public static synchronized void sendMsgToTopic(String topic, String msg){

    }

    public static synchronized void removeFromTopic(String topic, ClientConnection member){
        if (topicSenders.containsKey(topic)) {
            if ( topicSenders.get(topic).contains(member) ) topicSenders.get(topic).remove(member);
        }
        if (topicRecievers.containsKey(topic)) {
            if ( topicRecievers.get(topic).contains(member) ) topicRecievers.get(topic).remove(member);
        }
    }

}