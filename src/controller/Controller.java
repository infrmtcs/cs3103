package controller;

import java.util.*;

import storage.Storage;
import crawler.Crawler;

public class Controller {
	Queue<String> urlSeeds = new LinkedList<String>();
	String command = null;
	
	public void getURLSeeds(){
		Storage storage = new Storage();
	}
	
	public void getHtmlFile(){
		Crawler crawler = new Crawler();
	}
	
	public String getControllerCommand(){
		return command;
	}
}
