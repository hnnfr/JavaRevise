package com.hnn.jetty.junit;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

import javax.xml.ws.ProtocolException;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestWebClientUseStubConnection {

	@BeforeClass
	public static void initialize() {
		URL.setURLStreamHandlerFactory(new StubStreamHandlerFactory());
	}
	
	@Test
	public void testGetContentOk() throws MalformedURLException {
		WebClient client = new WebClient(); 
		String result = client.getContent(new URL("http://localhost")); 
		assertEquals("It works", result);
	}
}

class StubStreamHandlerFactory implements URLStreamHandlerFactory {

	@Override
	public URLStreamHandler createURLStreamHandler(String protocol) {
		return new StubHttpURLStreamHandler();
	}
	
}

class StubHttpURLStreamHandler extends URLStreamHandler {

	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return new StubHttpURLConnection(u);
	}
	
}

class StubHttpURLConnection extends HttpURLConnection {

	private boolean isInput = true; 
	
	protected StubHttpURLConnection(URL url) {
		super(url); 
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if (!isInput) {
			throw new ProtocolException("Cannot read from URLConnection if doInput=false (call setDoInput(true))"); 
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(new String("It works").getBytes());
		return bais;
	}

	@Override
	public void disconnect() {}

	@Override
	public boolean usingProxy() {
		return false;
	}

	@Override
	public void connect() throws IOException {}
	
}