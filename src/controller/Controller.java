package controller;

import gui.GUI;

import java.util.*;
import java.util.concurrent.TimeUnit;

import storage.CrawlerResult;
import storage.Storage;
import crawler.Crawler;
import crawler.SearchEngine;
import crawler.URL;

class Candidate {
    static Comparator<Candidate> comparator = new Comparator<Candidate>() {
        public int compare(Candidate left, Candidate right) {
            if (left.score != right.score) {
                return (left.score > right.score) ? -1 : 1;
            } else {
                return 0;
            }
        }
    };
    
    double score;
    URL url;
    
    public Candidate(double score, URL url) {
        this.score = score;
        this.url = url;
    }
}

public class Controller {
    private static long TIMEOUT = 500; // 500ms = 0.5s
    
    private PriorityQueue<Candidate> pageRank = new PriorityQueue<Candidate>(Candidate.comparator);
    
    private Crawler crawler = new Crawler();
    Storage storage = new Storage();
    
    private GUI window;

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
	
	public void query(String input) {
        pageRank.offer(new Candidate(1.0, new URL(SearchEngine.GOOGLE, input)));
        pageRank.offer(new Candidate(1.0, new URL(SearchEngine.BING, input)));
        pageRank.offer(new Candidate(1.0, new URL(SearchEngine.YAHOO, input)));
        getResult();
        getResult();
        getResult();
        window.printOutput(new QueryResult("looking forward to"));
	}

	public Controller(GUI window) {
        this.window = window;
    }
}
