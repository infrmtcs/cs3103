package crawler;

public enum SearchEngine {
    GOOGLE ("www.google.com.sg/search?q=%s"),
    BING   ("www.bing.com/search?q=%s"),
    YAHOO  ("sg.search.yahoo.com/search?p=%s");
    
    String rawUrl;
    
    private SearchEngine(String rawUrl) {
        this.rawUrl = rawUrl;
    }
}