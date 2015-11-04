package controller;

import gui.ExecutionGUI;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    String current;
    String alt;
    
    public Candidate(double score, URL url, String current, String alt) {
        this.score = score;
        this.url = url;
        this.current = current;
        this.alt = alt;
    }
}

public class Controller {
    private static long TIMEOUT = 500; // 500ms = 0.5s
    private static long FAST_END = 5;
    private static int LIMIT = 100;
    
    private HashSet<String> prepositions = new HashSet<String>(Arrays.asList(new String[] {
        "on", "in", "at", "since", "for", 
        "with", "before", "to", "past", "by", 
        "over", "up", "down", "above", "below", 
        "through", "into", "about", "off", "from", 
        "under"
    }));

    private ConcurrentHashMap<String, QueryResult> answers = new ConcurrentHashMap<String, QueryResult>();
    private PriorityBlockingQueue<Candidate> pageRank = new PriorityBlockingQueue<Candidate>(1, Candidate.comparator);
    
    private Storage storage = new Storage();
    private Crawler crawler = new Crawler();
    
    private ExecutionGUI execWindow;
    
    private QueryResult best = new QueryResult("");
    private int counter = 0;
    private int waiting = 0;
    
    private void setBestAnswer(String answer) {
        if (best.bestAnswer == answer) {
            return;
        }
        best.bestAnswer = answer;
        counter = 0;
        String[] words = answer.split("\\s+", ' ');
        int pos = -1;
        for (int i = words.length - 1; i >= 0; --i) {
            if (prepositions.contains(words[i])) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return;
        }
        for (String prep: prepositions) {
            words[pos] = prep;
            setupSeed(String.join(" ", words));
        }
    }

	private void setupSeed(String input) {
	    answers.put(input, new QueryResult(input));
	    pageRank.addAll(Arrays.asList(new Candidate[] {
            new Candidate(1.0, new URL(SearchEngine.GOOGLE, input, true), best.bestAnswer, input)
//          new Candidate(1.0, new URL(SearchEngine.GOOGLE, input, false), best.bestAnswer, input)
//          new Candidate(1.0, new URL(SearchEngine.BING, input, false), best.bestAnswer, input)
//	        new Candidate(1.0, new URL(SearchEngine.YAHOO, input), best.bestAnswer, input)
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
	    long score = 0;
	    if (result.url.searchEngine == SearchEngine.GOOGLE) {
	        int start = result.html.indexOf("<div id=\"resultStats\">");
	        start = result.html.indexOf("About ", start) + "About ".length();
	        int end = result.html.indexOf(" results", start);
	        String target = result.html.substring(start, end);
	        score = removeComma(target);
	    } else if (result.url.searchEngine == SearchEngine.BING) {
            int end = result.html.lastIndexOf(",000 results</span>") + ",000".length();
            int start = result.html.lastIndexOf('>', end) + 1;
            String target = result.html.substring(start, end);
            score = removeComma(target);
        } else if (result.url.searchEngine == SearchEngine.YAHOO) {
	        int end = result.html.lastIndexOf(",000 results</span>") + ",000".length();
	        int start = result.html.lastIndexOf('>', end) + 1;
	        String target = result.html.substring(start, end);
	        score = removeComma(target);
	    }
	    return score;
    }
	
    private void handleResult(CrawlerResult result, String alt) {
        waiting = 0;
        if (googleSuggestion(result)) {
            return;
        }
        long score = getSearchCount(result);
        System.out.println(result.url.path + " " + score);
        answers.get(alt).scores.offer(new Double(score));
    }
    
	private Thread createCrawlRequest(final Candidate next) {
        return new Thread(new Runnable() {
            public void run() {
                if (next != null) {
                    CrawlerResult result = storage.retrieveRowTable(next.url.path);
                    if (result == null) {
                        result = crawler.crawl(next.url);
                        if (result != null) {
                            storage.insertRowTable(result);
                        }
                    }
                    if (result != null) {
                        handleResult(result, next.alt);
                        execWindow.realTimeDisplay(result);
                    }
                }
            }
        });
	}
	
	private void startCrawling() {
	    int last = 0;
	    waiting = -1000000;
	    while (counter < LIMIT && waiting < FAST_END) {
	        ++counter;
	        ++waiting;
            System.out.println(counter);
	        Candidate next;
            try {
                do {
                    next = pageRank.poll(TIMEOUT, TimeUnit.MILLISECONDS);
                    if (next == null) break;
                } while (next.current != best.bestAnswer);
                if (next != null) {
                    last = counter;
                    Thread thread = createCrawlRequest(next);
                    thread.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
	    }
	}
	
	public QueryResult[] query(String input, ExecutionGUI execWindow) {
	    pageRank.clear();
	    answers.clear();
		this.execWindow = execWindow;
	    setBestAnswer(input);
	    startCrawling();
	    QueryResult[] ansArr = new QueryResult[answers.size()];
	    answers.values().toArray(ansArr);
	    Arrays.sort(ansArr, new Comparator<QueryResult>() {
	        Comparator<Double> doubleCmp = Comparator.naturalOrder();
	        public int compare(QueryResult left, QueryResult right) {
	            return -doubleCmp.compare((Double)left.getScore(), (Double)right.getScore());
	        }
	    });
	    
	    return ansArr;
	}
}
