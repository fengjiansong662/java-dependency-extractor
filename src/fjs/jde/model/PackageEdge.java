package fjs.jde.model;

public class PackageEdge {
	private Integer id;
	private String startP;
	private String endP;
	private int num;
	private String type;
	private double p2pValue;
	public double getP2pValue() {
		return p2pValue;
	}
	public void setP2pValue(double p2pValue) {
		this.p2pValue = p2pValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	private String releaseName;
	private String commitID;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStartP() {
		return startP;
	}
	public void setStartP(String startP) {
		this.startP = startP;
	}
	public String getEndP() {
		return endP;
	}
	public void setEndP(String endP) {
		this.endP = endP;
	}
}
