package storage;

public class URLStored {
	String url = null;
	boolean visited = false;
	int id = -1;

	public URLStored(){}
	
	public URLStored(int id, String url, boolean visited) {
		this.id = id;
		this.url = url;
		this.visited = visited;
	}
	
	public URLStored(String url, boolean visited) {
		this.url = url;
		this.visited = visited;
	}
	
	public String getURL(){
		return this.url;
	}
	
	public boolean getVisited(){
		return this.visited;
	}
	
	public int getId(){
		return id;
	}

	public void setURL(String url){
		this.url = url;
	}
	
	public void setVisted(boolean visited){
		this.visited = visited;
	}
	
	public void setId(int id){
		this.id = id;
	}
}
