package crawler;

import java.io.*;
import java.net.*;

import storage.CrawlerResult;

public class Crawler {
	private static final int HTTP_PORT = 80;
	private static final int TIMEOUT = 2000;
	
	private String getHttpRequest(URL url) {
		String req = String.format("GET %s HTTP/1.0\r\nHost: %s\r\nConnection: Keep-Alive\r\n\r\n", url.query, url.host);
		return req;
	}
	
	public CrawlerResult crawl(URL url) {
	    String html = "";
	    double latency = 0;
		try {
		    long startTime = System.nanoTime();
			Socket socket = new Socket(url.host, HTTP_PORT);
			socket.setSoTimeout(TIMEOUT);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = getHttpRequest(url);
			output.writeBytes(request);

			html = "";
			String line;
			while ((line = input.readLine()) != null) {
				html += line + "\r\n";
			}
			
			latency = (double)(System.nanoTime() - startTime) / (1e9);

			input.close();
			output.close();
			socket.close();
		} catch (Exception e) {
			return null;
		}
		return new CrawlerResult(url, html, latency);
	}
}
