import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientConnection {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;


    private String myTopic = null;

    private static Pattern rolePattern = Pattern.compile("((send)|(recieve))( )(.+)(\\n)");
    private static Pattern msgPattern = Pattern.compile("(message)( )(\\d+)(\\n)");


    public ClientConnection(Socket socket){
        this.socket = socket;
        new Thread(this::work).start();
    }

    private void work(){

        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(in));

            String text = reader.readLine();;
            Matcher matcher = rolePattern.matcher(text);
            if( matcher.find()) {
                myTopic = matcher.group(1);
                if (matcher.group(3)=="send") Server.putTopicSender(matcher.group(1),this);
                else if (matcher.group(3)=="recieve") Server.putTopicReciever(matcher.group(3),this);
            }




            reader.close();
            //writer.close();
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("чёто не так");
        }
        finally {
            Server.removeFromTopic(myTopic,this);
            //Thread.currentThread().interrupt();
        }

    }

    public void send(String msg){

    }


}