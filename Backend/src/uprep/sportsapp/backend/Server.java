package uprep.sportsapp.backend;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


class RequestServer {

	int port;

	public RequestServer(int port) {
		this.port = port;
	}

	public void start() throws IOException {
		InetSocketAddress addr = new InetSocketAddress(this.port);
		HttpServer server = HttpServer.create(addr, 0);

		server.createContext("/", new MyHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port " + this.port );
	}
}

class MyHandler implements HttpHandler {
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			System.out.println("recieved get request");
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			Headers requestHeaders = exchange.getRequestHeaders();
			Set<String> keySet = requestHeaders.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				List<String> values = requestHeaders.get(key);
				String s = key + " = " + values.toString() + "\n";
				responseBody.write(s.getBytes());
			}
			responseBody.write(new String(exchange.getRequestURI() + "\n").getBytes());
			responseBody.write(new String("good knight mon").getBytes());
			responseBody.close();
		}
		//TODO add http post handling for posting scores 
	}
}


public class Server
{
	public static void main(String[] args) throws IOException
	{
		//TODO add exception handling instead of passing it on to the vm. 
		RequestServer server = new RequestServer(8080); // Listen on port 8080
		server.start();
	}
}