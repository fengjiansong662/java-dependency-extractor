package as.jde.scm;


public class Commit {

	public String author;
	public String commitID;
	public String time;
	public String newLine;

	public Commit() {
		
	}
	
	public Commit(String commitID, String authorOfCommit) {
		this.commitID = commitID;
		this.author = authorOfCommit;
	}
	
}
