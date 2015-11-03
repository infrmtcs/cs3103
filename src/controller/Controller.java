package controller;

import gui.ExecutionGUI;
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
    private ExecutionGUI execWindow;
    
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
	
	private String removeTag(String target) {
	    return target.replaceAll("(?i)<[^>]*>", " ").replaceAll("\\s+", " ").trim();
	}
	
	private Boolean googleSuggestion(CrawlerResult result) {
        if (result.url.searchEngine == SearchEngine.GOOGLE) {
            int start = result.html.indexOf("<span class=\"spell\">Showing results for</span>");
            if (start != -1) {
                start = result.html.indexOf("</span>", start) + "</span>".length();
                int end = result.html.indexOf("<span class=\"spell_orig\">Search instead for");
                if (end != -1) {
                    String target = result.html.substring(start, end);
                    setBestAnswer(removeTag(target));
                    return true;
                }
            }
        }
        return false;
	}
	
	private long removeComma(String target) {
        return Long.valueOf(target.replace(",", ""));
	}
	
	private long getSearchCount(CrawlerResult result) {
	    if (result.url.searchEngine == SearchEngine.GOOGLE) {
	        int start = result.html.indexOf("<div id=\"resultStats\">");
	        start = result.html.indexOf("About ", start) + "About ".length();
	        int end = result.html.indexOf(" results", start);
	        String target = result.html.substring(start, end);
	        return removeComma(target);
	    } else if (result.url.searchEngine == SearchEngine.BING) {
	        int start = result.html.indexOf("<span class=\"sb_count\"");
	        int end = result.html.indexOf("</span>", start) + "</span>".length();
	        String target = result.html.substring(start, end);
            target = removeTag(target);
            end = target.indexOf(" results");
            target = target.substring(0, end);
	        return removeComma(target);
        } else if (result.url.searchEngine == SearchEngine.YAHOO) {
	        int end = result.html.lastIndexOf(",000 results</span>") + ",000".length();
	        int start = result.html.lastIndexOf('>', end) + 1;
	        String target = result.html.substring(start, end);
	        return removeComma(target);
	    }
	    return 0;
    }
	
    private void handleResult(CrawlerResult result) {
        if (googleSuggestion(result)) {
            return;
        }
        System.out.println(getSearchCount(result) + " " + result.url.searchEngine);
    }
    
	private Thread createCrawlRequest(Candidate next) {
        return new Thread(new Runnable() {
            public void run() {
                if (next != null) {
                    CrawlerResult result = storage.retrieveRowTable(next.url.path);
                    if (result == null) {
                        result = crawler.crawl(next.url);
                        if (result != null) {
                            storage.insertRowTable(result);
                            execWindow.realTimeDisplay(result);
                            handleResult(result);
                        }
                    } else {
                        execWindow.realTimeDisplay(result);
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

	public Controller(GUI window, ExecutionGUI execWindow) {
        this.window = window;
        this.execWindow = execWindow;
    }
}
