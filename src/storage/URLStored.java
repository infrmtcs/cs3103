package storage;

public class URLStored {
	String url;
	String html;
	double latency;
	
	public URLStored(String url, String html, double latency) {
		this.url = url;
        this.html = html;
		this.latency = latency;
	}
}
