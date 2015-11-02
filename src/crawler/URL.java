package crawler;

public class URL {
    String path;
	String host;
	String query;
	
	public URL(String url) {
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
	
	public URL(SearchEngine searchEngine, String keyword) {
        this(String.format(searchEngine.rawUrl, keyword.replace(' ', '+')));
    }
}