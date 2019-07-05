package com.dag.king.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dag.king.model.User;
import com.dag.king.repository.impl.ResultsRepositoryImpl;
import com.dag.king.repository.impl.TokensRepositoryOtherImpl;
import com.dag.king.repository.impl.UsersRepositoryImpl;
import com.dag.king.service.ResultsService;
import com.dag.king.service.TokensService;
import com.dag.king.service.UsersService;
import com.dag.king.service.impl.ResultsServiceImpl;
import com.dag.king.service.impl.TokensServiceImpl;
import com.dag.king.service.impl.UsersServiceImpl;
import com.dag.king.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Controller implements HttpHandler {
	private static Logger logger = Logger.getLogger("Controller");

	final private static String SESSION_KEY = "sessionkey";
	final private static String LOGIN = "login";
	final private static String SCORE = "score";
	final private static String HIGHSCORELIST = "highscorelist";
	final private static String GET = "GET";
	final private static String POST = "POST";

	private UsersService usersService;
	private TokensService tokensService;
	private ResultsService resultsService;

	public Controller() {
		usersService = new UsersServiceImpl(new UsersRepositoryImpl());
		tokensService = new TokensServiceImpl(new TokensRepositoryOtherImpl());
		resultsService = new ResultsServiceImpl(new ResultsRepositoryImpl());
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {

		URI uri = httpExchange.getRequestURI();
		String method = httpExchange.getRequestMethod().toUpperCase();
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Request [" + method + "] " + uri);
		}
		
		String[] uriTmp = uri.getPath().split("/");

		OutputStream stream = null;

		// @formatter:off
		/**
		 * Request: GET /<userid>/login Response: <sessionkey>
		 * 
		 * Request: POST /<levelid>/score?sessionkey=<sessionkey> Request body:
		 * <score> Response: (nothing)
		 * 
		 * Request: GET /<levelid>/highscorelist Response: CSV of
		 * <userid>=<score>
		 * 
		 */
		// @formatter:on

		if (uriTmp != null && uriTmp.length == 3) {
			try {
				stream = httpExchange.getResponseBody();
				if (GET.equals(method) && LOGIN.equals(uriTmp[2])) {
					login(uriTmp[1], httpExchange, stream);
				} else if (POST.equals(method) && SCORE.equals(uriTmp[2])) {
					int score = Utils.readInputContent(httpExchange.getRequestBody());
					score(uriTmp[1], score, httpExchange, stream);
				} else if (GET.equals(method) && HIGHSCORELIST.equals(uriTmp[2])) {
					highscorelist(uriTmp[1], httpExchange, stream);
				}
				else {
					httpExchange.sendResponseHeaders(500, 0);
				}
			} catch (Exception ex) {
				httpExchange.sendResponseHeaders(500, 0);
				if (logger.isLoggable(Level.SEVERE)) {
					logger.log(Level.SEVERE, "error processing request", ex);
				}
			} finally {
				if (stream != null)
					stream.flush();
			}

		}

		httpExchange.close();
	}

	
	/**
	 * return score list for given level
	 * 
	 * @param levelIdString
	 * @param httpExchange
	 * @param stream
	 * @throws IOException
	 */
	private void highscorelist(String levelIdString, HttpExchange httpExchange, OutputStream stream) throws IOException {
		int levelId = Utils.getIntegerParam(levelIdString);
		if (levelId < 0) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid level [ " + levelIdString + "] hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
			return;
		}

		String csv = resultsService.getScoreList(levelId);
		byte[] buf = csv.getBytes();
		httpExchange.sendResponseHeaders(200, buf.length);
		stream.write(buf);
	}

	/**
	 * Stores new score for given user at given level
	 * 
	 * @param levelIdString
	 * @param score
	 * @param httpExchange
	 * @param stream
	 * @throws IOException
	 */
	private void score(String levelIdString, int score, HttpExchange httpExchange, OutputStream stream)
			throws IOException {
		if (score < 0) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid score, hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
			return;
		}

		// we will extract token from params
		String query = httpExchange.getRequestURI().getQuery();
		String[] params = query != null ? query.split("=") : null;
		if (params == null || params.length != 2 || !SESSION_KEY.equals(params[0])) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid url query[ " + query + "] hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
			return;
		}

		String token = params[1];
		int levelId = Utils.getIntegerParam(levelIdString);
		if (levelId < 0) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid level [ " + levelIdString + "] hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
			return;
		}

		User user = tokensService.getUserFromToken(token);

		if (user == null) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("token invalid or expired [ " + token + "] hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
			return;
		}
		
		resultsService.store(levelId, user, score);
		
		httpExchange.sendResponseHeaders(200, 0);
	}

	
	/**
	 * returns a valid token for given user
	 * 
	 * @param userIdString
	 * @param httpExchange
	 * @param stream
	 * @throws IOException
	 */
	public void login(String userIdString, HttpExchange httpExchange, OutputStream stream) throws IOException {

		int userId = Utils.getIntegerParam(userIdString);
		if (userId >= 0) {
			User user = usersService.login(userId);

			String token = tokensService.getTokenForUser(user);

			byte[] buf = token.getBytes();
			httpExchange.sendResponseHeaders(200, buf.length);
			stream.write(buf);

		} else {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("invalid userId [ " + userIdString + "] hacker??");
			}
			httpExchange.sendResponseHeaders(500, 0);
		}
	}

}
