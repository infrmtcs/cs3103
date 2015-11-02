package controller;

import gui.GUI;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
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
    private static int LIMIT = 10;
    
    private PriorityBlockingQueue<Candidate> pageRank = new PriorityBlockingQueue<Candidate>(11, Candidate.comparator);
    
    private Storage storage = new Storage();
    private Crawler crawler = new Crawler();
    
    private GUI window;

	private void setupSeed(String input) {
	    pageRank.addAll(Arrays.asList(new Candidate[] {
	        new Candidate(1.0, new URL(SearchEngine.GOOGLE, input)),
	        new Candidate(1.0, new URL(SearchEngine.BING, input)),
	        new Candidate(1.0, new URL(SearchEngine.YAHOO, input))
	    }));
	}
	
	private Thread createCrawlRequest() {
        return new Thread(new Runnable() {
            public void run() {
                Candidate next;
                try {
                    while (true) {
                        next = pageRank.poll(TIMEOUT, TimeUnit.MILLISECONDS);
                        if (next != null) {
                            CrawlerResult result = storage.retrieveRowTable(next.url.path);
                            if (result == null) {
                                result = crawler.crawl(next.url);
                                if (result != null) {
                                    storage.insertRowTable(result);
                                    System.err.println(result.url.path);
                                    System.err.println(result.latency);
                                } else {
                                    System.err.println("NULL");
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
	}
	
	private void startCrawling() {
	    for (int turn = 0; turn < LIMIT; ++turn) {
	        Thread thread = createCrawlRequest();
	        thread.start();
	    }
	}
	
	public void query(String input) {
	    setupSeed(input);
	    startCrawling();
        window.printOutput(new QueryResult("looking forward to"));
	}

	public Controller(GUI window) {
        this.window = window;
    }
}
