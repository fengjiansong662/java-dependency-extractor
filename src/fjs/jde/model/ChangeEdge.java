package fjs.jde.model;

public class ChangeEdge {
	private Integer changeEdgeID;
	private String commitID;
	private String releaseName;
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	private int addEdge;
	private int remEdge;
	private int addNEdge;
	public Integer getChangeEdgeID() {
		return changeEdgeID;
	}
	public void setChangeEdgeID(Integer changeEdgeID) {
		this.changeEdgeID = changeEdgeID;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	public int getAddEdge() {
		return addEdge;
	}
	public void setAddEdge(int addEdge) {
		this.addEdge = addEdge;
	}
	public int getRemEdge() {
		return remEdge;
	}
	public void setRemEdge(int remEdge) {
		this.remEdge = remEdge;
	}
	public int getAddNEdge() {
		return addNEdge;
	}
	public void setAddNEdge(int addNEdge) {
		this.addNEdge = addNEdge;
	}
}
