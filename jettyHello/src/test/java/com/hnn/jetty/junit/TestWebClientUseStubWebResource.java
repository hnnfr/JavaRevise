package com.hnn.jetty.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestWebClientUseStubWebResource {
	
	@BeforeClass
	public static void initialize() throws Exception {
		Server server = new Server(8081); 
		
		
		// XXX there is a problem here, check Jetty docs
		ContextHandler contentNotFoundContext = new ContextHandler(server, "/testGetContentNotFound"); 
		contentNotFoundContext.setHandler(new TestGetContentNotFoundHandler());
		
		ContextHandler contentOkContext = new ContextHandler(server, "/testGetContentOk"); 
		contentOkContext.setHandler(new TestGetContentOkHandler());
		
		server.setStopAtShutdown(true);
		server.start();
	}
	
	@AfterClass
	public static void clean() {
		// shutdown jetty
	}
	
	@Test
	public void testGetContentOk() throws MalformedURLException {
		WebClient client = new WebClient();
		String result = client.getContent(new URL("http://localhost:8081/testGetContentOk")); 
		
		assertEquals("It works", result);
	}
	
	@Test
	public void testGetContentNotFound() throws MalformedURLException {
		WebClient client = new WebClient();
		String result = client.getContent(new URL("http://localhost:8081/testGetContentNotFoud"));
		
		assertNull(result);
	}
}

class TestGetContentOkHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.getWriter().print("It works");
		
		// Inform jetty that this request has now been handled
		baseRequest.setHandled(true);
	}

}

class TestGetContentNotFoundHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
}
