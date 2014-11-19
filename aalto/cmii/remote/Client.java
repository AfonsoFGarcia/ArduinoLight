package aalto.cmii.remote;

import java.io.IOException;
import java.net.UnknownHostException;

class Client {
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usaga: java Client [SERVER IP] [COMMAND]");
			return;
		}
		
		String TCP_IP = args[0];
		int TCP_PORT = 5000;

		try {
			ServerConnect server = new ServerConnect(TCP_IP, TCP_PORT);
			if(args[1].equals("ON"))
				System.out.println(server.sendOn());
			else if(args[1].equals("OFF"))
				System.out.println(server.sendOff());
			server.closeConnection();
		} catch (UnknownHostException e) {
            System.err.println("Don't know about host " + TCP_IP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                TCP_IP);
            System.exit(1);
        }
	}
}