package crawler;

public class URL {
    public String path;
    public String host;
    public String query;
	public SearchEngine searchEngine = SearchEngine.NONE; 
	
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
	
	public URL(SearchEngine searchEngine, String keyword, Boolean exact) {
        this(String.format(searchEngine.rawUrl, (exact ? 
            ((searchEngine == SearchEngine.GOOGLE) ? ("%22" + keyword + "%22") : ("\"" + keyword + "\"")) : 
            keyword
        ).replace(' ', '+')));
        this.searchEngine = searchEngine;  
    }
}