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
    private static long FAST_END = 10; // 500ms = 0.5s
    private static int LIMIT = 100;
    
    private PriorityBlockingQueue<Candidate> pageRank = new PriorityBlockingQueue<Candidate>(1, Candidate.comparator);
    
    private Storage storage = new Storage();
    private Crawler crawler = new Crawler();
    
    private GUI window;
    
    private QueryResult best = new QueryResult("");
    private int counter = 0;
    
    private void setBestAnswer(String answer) {
        if (best.bestAnswer != answer) {
            best.bestAnswer = answer;
            counter = 0;
            setupSeed(answer);
        }
    }

	private void setupSeed(String input) {
	    pageRank.addAll(Arrays.asList(new Candidate[] {
	        new Candidate(1.0, new URL(SearchEngine.GOOGLE, input)),
	        new Candidate(1.0, new URL(SearchEngine.BING, input)),
	        new Candidate(1.0, new URL(SearchEngine.YAHOO, input))
	    }));
	}
	
	private Thread createCrawlRequest(Candidate next) {
        return new Thread(new Runnable() {
            private void handleResult(CrawlerResult result) {
                storage.insertRowTable(result);
                if (result.url.searchEngine == SearchEngine.GOOGLE) {
                    int start = result.html.indexOf("<span class=\"spell\">Showing results for</span>");
                    if (start != -1) {
                        start = result.html.indexOf("</span>", start) + "</span>".length();
                        int end = result.html.indexOf("<span class=\"spell_orig\">Search instead for");
                        String target = result.html.substring(start, end);
                        target = target.replaceAll("(?i)<[^>]*>", " ").replaceAll("\\s+", " ").trim();
                        setBestAnswer(target);
                    }
                    System.err.println(result.url.path);
                } else if (result.url.searchEngine == SearchEngine.BING) {
                    
                } else if (result.url.searchEngine == SearchEngine.YAHOO) {
                    
                }
            }
            
            public void run() {
                if (next != null) {
                    CrawlerResult result = storage.retrieveRowTable(next.url.path);
                    if (result == null) {
                        result = crawler.crawl(next.url);
                        if (result != null) {
                            handleResult(result);
                        }
                    }
                }
            }
        });
	}
	
	private void startCrawling() {
	    int last = 0;
	    while (counter < LIMIT) {
	        ++counter;
            System.out.println(counter);
	        Candidate next;
            try {
                next = pageRank.poll(TIMEOUT, TimeUnit.MILLISECONDS);
                if (next != null) {
                    last = counter;
                    Thread thread = createCrawlRequest(next);
                    thread.start();
                } else if (counter - last >= FAST_END) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
	    }
	}
	
	public void query(String input) {
	    setBestAnswer(input);
	    startCrawling();
        window.printOutput(best);
	}

	public Controller(GUI window) {
        this.window = window;
    }
}
