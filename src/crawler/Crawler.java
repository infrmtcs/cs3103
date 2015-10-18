package crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class CrawlerResult {
	String queryUrl;
	String htmlContent;
	
	public CrawlerResult(String queryUrl, String htmlContent) {
		this.queryUrl = queryUrl;
		this.htmlContent = htmlContent;
	}
}

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
	private ConcurrentLinkedQueue<CrawlerResult> result;
	
	
	private String getHttpRequest(Url url) {
		String req = String.format("GET %s HTTP/1.0\r\nHost: %s\r\nConnection: Keep-Alive\r\n\r\n", url.query, url.host);
		return req;
	}
	
	public void run() {
		try {
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
			result.offer(new CrawlerResult(url.path, html));
			
			input.close();
			output.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	public Connector(String url, ConcurrentLinkedQueue<CrawlerResult> result) {
		this.url = new Url(url);
		this.result = result;
	}
}

public class Crawler {
	private ConcurrentLinkedQueue<CrawlerResult> result = new ConcurrentLinkedQueue<CrawlerResult>();
	private static final String GOOGLE = "www.google.com.sg/search?q=%s"; 
	private static final String BING = "www.bing.com/search?q=%s";
	private static final String YAHOO = "sg.search.yahoo.com/search?p=%s";
	
	public void crawl(String url) {
		Thread thread = new Thread(new Connector(url, result));
		thread.start();
	}
	
	public void crawlSearchEngine(String engine, String keyword) {
		keyword = keyword.replace(' ', '+');
		crawl(String.format(engine, keyword));
	}

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.crawlSearchEngine(YAHOO, "infrmtcs");
		
		while (crawler.result.isEmpty()) {
			System.err.println("waiting");
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		System.err.println(crawler.result.peek().htmlContent);
	}
}
