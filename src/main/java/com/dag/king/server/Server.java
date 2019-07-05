package com.dag.king.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

/**
 * Class that implements Http server
 * 
 * @author david
 *
 */
public class Server {
	private static Logger logger = Logger.getLogger("Server");

	private HttpServer httpServer;

	public Server(String hostname, int port, int numberThreads) throws IOException {
		
		logger.info("Server listening at [" + hostname + ":" + port + "] with " + numberThreads + " of threads");

		// we will start server with a backlog of 50% of total 
		httpServer = HttpServer.create(new InetSocketAddress(hostname, port), numberThreads / 2);

		// we set an executor with some threads already started
		// also we will have a queue for waiting threads
		httpServer.setExecutor(new ThreadPoolExecutor(numberThreads, numberThreads, 60, TimeUnit.SECONDS,
				new LinkedBlockingDeque<Runnable>(numberThreads/2)));

		httpServer.createContext("/", new Controller());
	}

	public void start() {
		logger.info("Starting server ...");

		httpServer.start();
	}

	public void stop() {
		logger.info("Stopping server ...");

		httpServer.stop(0);
	}
}
