package storage;

import crawler.URL;

public class CrawlerResult {
	public URL url;
	public String html;
	public double latency;
	
	public CrawlerResult(URL url, String html, double latency) {
		this.url = url;
        this.html = html;
		this.latency = latency;
	}
}
