package controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import storage.CrawlerResult;
import storage.Storage;
import crawler.Crawler;

public class Controller {
    private static long TIMEOUT = 500; // 500ms = 0.5s
    
    Crawler crawler = new Crawler();
    Storage storage = new Storage();
    Scanner scanner = new Scanner(System.in);

	private CrawlerResult getResult() {
	    CrawlerResult result = null;
	    try {
	        result = crawler.result.poll(TIMEOUT, TimeUnit.MILLISECONDS);
	        if (result != null) {
	            storage.insertRowTable(result);
	        }
	    } catch (InterruptedException e) {
	        System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    }
	    return result;
	}
	
	private void query(String input) {
        crawler.crawlSearchEngine(Crawler.GOOGLE, input);
        crawler.crawlSearchEngine(Crawler.BING, input);
        crawler.crawlSearchEngine(Crawler.YAHOO, input);
	}
	
	
	public static void main(String[] args) {
	    Controller controller = new Controller();
	    controller.query("looking forward with");
    }
}
