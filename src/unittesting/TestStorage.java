package unittesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import storage.Storage;
import storage.URLStored;

public class TestStorage {

	@Test
	public void testInsertRowMethod() {
		String url = "www.google.com";
		URLStored urlStored = new URLStored(1, url, URLStored.TRUE);
		
		Storage storage = new Storage();
		storage.createTable();
		storage.insertRowTable(urlStored);
		
		URLStored checking = storage.retrieveRowTable(urlStored);
		ArrayList<URLStored> list = storage.retrieveTable();
		
		assertEquals(checking.getId(), 1);
		assertEquals(checking.getURL(), url);
		assertEquals(checking.getVisited(), URLStored.TRUE);
		
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getId(), urlStored.getId());
		
		storage.dropTable();
	}
	
	@Test
	public void testRetrieveRow(){
		
	}

}
