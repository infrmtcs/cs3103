package storage;

public class URLStored {
	public static final int FALSE = 0;
	public static final int TRUE = 1;
	
	String url = null;
	int visited = FALSE;
	int id = -1;

	public URLStored(){}
	
	public URLStored(int id, String url, int visited) {
		this.id = id;
		this.url = url;
		this.visited = visited;
	}
	
	public URLStored(String url, int visited) {
		this.url = url;
		this.visited = visited;
	}
	
	public String getURL(){
		return url;
	}
	
	public int getVisited(){
		return visited;
	}
	
	public int getId(){
		return id;
	}

	public void setURL(String url){
		this.url = url;
	}
	
	public void setVisited(int visited){
		this.visited = visited;
	}
	
	public void setId(int id){
		this.id = id;
	}
}
