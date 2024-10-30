package org.example.semantic;

import com.sun.net.httpserver.HttpServer;
import org.example.semantic.services.ActiviteHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SemanticApplication {

	public static void main(String[] args) {
		try {
			String fusekiEndpoint = "http://localhost:3030/projet"; // Change this URL to your Fuseki endpoint

			HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
			server.createContext("/activites", new ActiviteHandler(fusekiEndpoint)); // Register the ActiviteHandler

			server.setExecutor(null);
			server.start();
			System.out.println("Server is running on port 9000");
		} catch (IOException e) {
			System.err.println("Error starting server: " + e.getMessage());
		}
	}
}