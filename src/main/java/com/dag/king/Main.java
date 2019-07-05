package com.dag.king;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.server.Server;

/**
 * Main class to start server
 * 
 * @author david
 *
 */
public class Main {
	private static Logger logger = Logger.getLogger("Main");

	public static void main(String[] argv) {
		try {
			String hostname = "localhost";
			int port = 8080;
			int threads = 1000;
			
			if (argv.length > 0) {
				threads = Integer.parseInt(argv[0]);
			}
			if (argv.length > 1) {
				port = Integer.parseInt(argv[1]);
			}
			if (argv.length > 2) {
				hostname = argv[2];
			}
			
			Server server = new Server(hostname, port, threads);

			server.start();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "error starting Server", ex);
		}
	}
}
