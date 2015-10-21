package storage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class StorageTest {
    @Test
    public void test() {
        Storage storage = new Storage(true);
        ArrayList<CrawlerResult> res;
        CrawlerResult ans;
        
        storage.dropTable();
        storage.createTable();
        res = storage.retrieveTable();
        assertTrue("Storage is empty", res.isEmpty());

        ans = new CrawlerResult("http://www.google.com.sg/dat", "<html>Duong Thanh Dat</html>", 0.2);
        storage.insertRowTable(ans);
        res = storage.retrieveTable();
        assertTrue("Now there is 1", res.size() == 1);
        
        ans = new CrawlerResult("http://www.bing.com/duong", "<html>infrmtcs</html>", 0.3);
        storage.insertRowTable(ans);
        res = storage.retrieveTable();
        assertTrue("Now there are 2", res.size() == 2);
        
        ans = storage.retrieveRowTable("http://www.bing.com/duong");
        assertEquals("URL", "http://www.bing.com/duong", ans.url);
        assertEquals("HTML", "<html>infrmtcs</html>", ans.html);
        assertEquals("Latency", 0.3, ans.latency, 0.0001);
        
        storage.dropTable();
    }

}
