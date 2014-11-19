package aalto.cmii.remote;

import java.io.*;
import java.net.*;

public class ServerConnect {
	Socket server;
	PrintWriter out;
	BufferedReader in;

	public ServerConnect(String TCP_IP, Integer TCP_PORT) throws UnknownHostException, IOException {
		server = new Socket(TCP_IP, TCP_PORT);
		out = new PrintWriter(server.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}

	public void closeConnection() throws IOException {
		server.close();
	}

	public String sendOn() throws IOException {
		out.println("ON");
		return in.readLine();
	}

	public String sendOff() throws IOException {
		out.println("OFF");
		return in.readLine();
	}
}