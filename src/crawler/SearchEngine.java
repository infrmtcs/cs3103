package crawler;

public enum SearchEngine {
    GOOGLE ("www.google.com.sg/search?q=%s", "www.google.com.sg"),
    BING   ("www.bing.com/search?q=%s", "www.bing.com"),
    YAHOO  ("sg.search.yahoo.com/search?p=%s", "sg.search.yahoo.com"),
    NONE   ("", "");
    
    String rawUrl;
    String domain;
    
    private SearchEngine(String rawUrl, String domain) {
        this.rawUrl = rawUrl;
        this.domain = domain;
    }
}