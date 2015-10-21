package storage;

public class CrawlerResult {
	public String url;
	public String html;
	public double latency;
	
	public CrawlerResult(String url, String html, double latency) {
		this.url = url;
        this.html = html;
		this.latency = latency;
	}
}
