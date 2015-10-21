package crawler;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

import storage.CrawlerResult;

class Url {
	String path;
	String host;
	String query;
	
	public Url(String url) {
		path = url;
		if (url.startsWith("http")) {
			int colon = url.indexOf(':');
			// We're ignoring http:// in http://foo.bar/
			url = url.substring(colon + 3); 
		}
		
		int slash = url.indexOf('/');
		if (slash != -1) {
			// After we deleted http://
			// Host is the part before the first /
			host = url.substring(0, slash);
			// Query is the rest
			query = url.substring(slash);
		} else {
			// No queries here => query should be empty.
			host = url;
			query = "";
		}
	}
}

class Connector implements Runnable {
	private static final int HTTP_PORT = 80;
	
	private Url url;
	private LinkedBlockingQueue<CrawlerResult> result;
	
	
	private String getHttpRequest(Url url) {
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
			result.offer(new CrawlerResult(url.path, html, latency));
			
			input.close();
			output.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public Connector(String url, LinkedBlockingQueue<CrawlerResult> result) {
		this.url = new Url(url);
		this.result = result;
	}
}

public class Crawler {
	public LinkedBlockingQueue<CrawlerResult> result = new LinkedBlockingQueue<CrawlerResult>();
	public static final String GOOGLE = "www.google.com.sg/search?q=%s"; 
	public static final String BING = "www.bing.com/search?q=%s";
	public static final String YAHOO = "sg.search.yahoo.com/search?p=%s";
	
	public void crawl(String url) {
		Thread thread = new Thread(new Connector(url, result));
		thread.start();
	}
	
	public void crawlSearchEngine(String engine, String keyword) {
//	    convert from "this phrase" to "this+phrase"
		keyword = keyword.replace(' ', '+');
		crawl(String.format(engine, keyword));
	}
}
