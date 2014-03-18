package uprep.sportsapp.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server2 extends WebSocketServer {

	public Server2( int port ) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public Server2( InetSocketAddress address ) {
		super(address);
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		this.sendToAll("new connection: " + handshake.getResourceDescriptor());
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println(conn + " has left the room!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if (isAuthenticated()) {
			System.out.println(conn + ": " + message);
		}
	}

	public void onFragment(WebSocket conn, Framedata fragment) {
		System.out.println("received fragment: " + fragment);
	}

	private boolean isAuthenticated() {
		return false; // TODO - Check authenticated request.
	}

	public static void main(String[] args) throws InterruptedException , IOException {
		WebSocketImpl.DEBUG = true;
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex) {
			port = 8080;
		}
		Server2 s = new Server2(port);
		s.start();
		System.out.println("SportsApp Started: " + s.getPort());

		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in = sysin.readLine();
			s.sendToAll( in );
			if(in.equals("exit")) {
				s.stop();
				break;
			} else if(in.equals("restart")) {
				s.stop();
				s.start();
				break;
			}
		}
	}
	@Override
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
			// some errors like port binding failed may not be assignable to a specific websocket
		}
	}

	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 *             
	 * A use for this would be to broadcast the latest updates automatically the moment they are received.
	 */
	public void sendToAll( String text ) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
			}
		}
	}
}
