package crawler;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

import storage.CrawlerResult;

class Connector implements Runnable {
	private static final int HTTP_PORT = 80;
	
	private URL url;
	private LinkedBlockingQueue<CrawlerResult> result;
	
	
	private String getHttpRequest(URL url) {
		String req = String.format("GET %s HTTP/1.0\r\nHost: %s\r\nConnection: Keep-Alive\r\n\r\n", url.query, url.host);
		return req;
	}
	
	public void run() {
		try {
		    long startTime = System.nanoTime();
			Socket socket = new Socket(url.host, HTTP_PORT);
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = getHttpRequest(url);
			output.writeBytes(request);

			String html = "";
			String line;
			while ((line = input.readLine()) != null) {
				html += line + "\r\n";
			}
			
			double latency = (double)(System.nanoTime() - startTime) / (1e9);
			result.offer(new CrawlerResult(url, html, latency));
			
			input.close();
			output.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public Connector(URL url, LinkedBlockingQueue<CrawlerResult> result) {
		this.url = url;
		this.result = result;
	}
}

public class Crawler {
	public LinkedBlockingQueue<CrawlerResult> result = new LinkedBlockingQueue<CrawlerResult>();
	
	public void crawl(URL url) {
		Thread thread = new Thread(new Connector(url, result));
		thread.start();
	}
}
