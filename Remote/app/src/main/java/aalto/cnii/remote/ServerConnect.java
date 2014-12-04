package aalto.cnii.remote;

import java.io.*;
import java.net.*;

public class ServerConnect {
    private String TCP_IP;
    private Integer TCP_PORT;

    public ServerConnect(String TCP_IP, Integer TCP_PORT) {
        this.TCP_IP = TCP_IP;
        this.TCP_PORT = TCP_PORT;
    }

    public String sendOn() throws IOException {
        return sendToServer("ON");
    }

    public String sendOff() throws IOException {
        return sendToServer("OFF");
    }

    public String sendStatus() throws IOException {
        return sendToServer("STS");
    }

    private String sendToServer(String message) throws IOException {
        Socket server = new Socket(TCP_IP, TCP_PORT);
        PrintWriter out = new PrintWriter(server.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));

        out.println(message);
        String reply =  in.readLine();
        server.close();

        return reply;
    }
}