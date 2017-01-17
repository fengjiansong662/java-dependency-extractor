package fjs.jde.model;

public class A2A {
	private Integer id;
	private String commitID;
	private String nextCommitID;
	private String commitName;
	private String releaseName;
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	private double value;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	public String getNextCommitID() {
		return nextCommitID;
	}
	public void setNextCommitID(String nextCommitID) {
		this.nextCommitID = nextCommitID;
	}
	public String getCommitName() {
		return commitName;
	}
	public void setCommitName(String commitName) {
		this.commitName = commitName;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
