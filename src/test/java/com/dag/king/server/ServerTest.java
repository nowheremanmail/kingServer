package com.dag.king.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ServerTest {

	private Server server;

	@Before
	public void setUp() throws Exception {
		server = new Server("localhost", 8080, 10);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
		// sametimes server didn't stop on time, then we wait a second to leave time to finish
		Thread.sleep(1000);
	}

	@Test
	public void testLoginUser() throws IOException {
		Client client = Client.create(new DefaultClientConfig());
		WebResource service = client.resource("http://localhost:8080");
		ClientResponse resp = service.path("1000").path("login").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

		String text = resp.getEntity(String.class);

		assertEquals(200, resp.getStatus());
		assertNotEquals("", text);
	}

	@Test
	public void testInvalidLoginUser() throws IOException {
		Client client = Client.create(new DefaultClientConfig());
		WebResource service = client.resource("http://localhost:8080");
		ClientResponse resp = service.path("x").path("login").accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

		String text = resp.getEntity(String.class);

		assertEquals(500, resp.getStatus());
	}

	@Test
	public void testReLoginUser() throws IOException {
		Client client = Client.create(new DefaultClientConfig());

		WebResource service = client.resource("http://localhost:8080");

		ClientResponse resp1 = service.path("1000").path("login").accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);

		String text1 = resp1.getEntity(String.class);

		assertEquals(200, resp1.getStatus());
		assertNotEquals("", text1);

		ClientResponse resp2 = service.path("1000").path("login").accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		String text2 = resp2.getEntity(String.class);

		assertEquals(200, resp2.getStatus());
		assertNotEquals(text2, text1);
	}

	@Test
	public void testLoginTwoUser() throws IOException {
		Client client = Client.create(new DefaultClientConfig());

		WebResource service = client.resource("http://localhost:8080");

		ClientResponse resp1 = service.path("1000").path("login").accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);

		String text1 = resp1.getEntity(String.class);

		assertEquals(200, resp1.getStatus());
		assertNotEquals("", text1);

		ClientResponse resp2 = service.path("1001").path("login").accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		String text2 = resp2.getEntity(String.class);

		assertEquals(200, resp2.getStatus());
		assertNotEquals(text2, text1);
	}

	@Test
	public void invalidAccessTest() throws IOException {
		Client client = Client.create(new DefaultClientConfig());
		WebResource service = client.resource("http://localhost:8080");

		ClientResponse resp3 = service.path("1").path("score").queryParam("sessionkey", "invalid")
				.accept(MediaType.TEXT_PLAIN).post(ClientResponse.class, "100");

		assertEquals(500, resp3.getStatus());
	}

	@Test
	public void testScoreResultUser() throws IOException {
		Client client = Client.create(new DefaultClientConfig());
		WebResource service = client.resource("http://localhost:8080");

		String token1, token2;
		{
			ClientResponse resp2 = service.path("1").path("highscorelist").accept(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);
			String text2 = resp2.getEntity(String.class);

			assertEquals("", text2);
		}

		{
			ClientResponse resp = service.path("1000").path("login").accept(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);

			token1 = resp.getEntity(String.class);

			assertEquals(200, resp.getStatus());
			assertNotEquals("", token1);
		}

		{
			ClientResponse resp5 = service.path("2000").path("login").accept(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);

			token2 = resp5.getEntity(String.class);

			assertEquals(200, resp5.getStatus());
			assertNotEquals("", token2);
		}
		
		{
			ClientResponse resp3 = service.path("1").path("score").queryParam("sessionkey", token1)
					.accept(MediaType.TEXT_PLAIN).post(ClientResponse.class, "100");

			assertEquals(200, resp3.getStatus());
		}

		{
			ClientResponse resp4 = service.path("1").path("highscorelist").accept(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);

			String csv = resp4.getEntity(String.class);

			assertEquals(200, resp4.getStatus());
			assertEquals("1000=100", csv);
		}
		
		{
			ClientResponse resp6 = service.path("1").path("score").queryParam("sessionkey", token2)
					.accept(MediaType.TEXT_PLAIN).post(ClientResponse.class, "200");

			assertEquals(200, resp6.getStatus());
		}

		{
			ClientResponse resp4 = service.path("1").path("highscorelist").accept(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);

			String csv = resp4.getEntity(String.class);

			assertEquals(200, resp4.getStatus());
			assertEquals("2000=200,1000=100", csv);
		}
	}

}
