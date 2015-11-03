package controller;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueryResult {
    public String bestAnswer;
    public ConcurrentLinkedQueue<Double> scores;
    
    public double getScore() {
        if (scores.isEmpty()) {
            return 0;
        } else {
            double res = 1.0;
            for (Double e: scores) {
                res *= e;
            }
            return Math.pow(res, 1.0 / scores.size());
        }
    }
    
    public QueryResult(String bestAnswer) {
        this.bestAnswer = bestAnswer;
        scores = new ConcurrentLinkedQueue<Double>();
    }
}
