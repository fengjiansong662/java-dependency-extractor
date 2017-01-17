package fjs.jde.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import as.jde.graph.Method;

public class Package {
	
	private Integer pkgId;
	private Integer size;
	private String sourcePath;//除包名外的物理地址
	private String allPath;//总物理路径
	public String getAllPath() {
		return allPath;
	}
	public void setAllPath(String allPath) {
		this.allPath = allPath;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public Integer getSize() {
		return size;
	}
	public Integer getPkgId() {
		return pkgId;
	}
	public void setPkgId(Integer pkgId) {
		this.pkgId = pkgId;
	}
	private String simpleName;
	private String pathName;//上一层的包路径
	private ArrayList<Clazz> clazzs;
	private List<String>  inheritancePkgs;
	public Map<String,Integer> inheritanceEdge;
	public Map<String,Integer> dependencyEdge;
	private List<String>  dependencyPkgs;

/*	public Map<String, Integer> getInheritanceEdge() {
		return inheritanceEdge;
	}
	public void setInheritanceEdge(Map<String, Integer> inheritanceEdge) {
		this.inheritanceEdge = inheritanceEdge;
	}
	public Map<String, Integer> getDependencyEdge() {
		return dependencyEdge;
	}
	public void setDependencyEdge(Map<String, Integer> dependencyEdge) {
		this.dependencyEdge = dependencyEdge;
	}*/
	private String path;//全路径
	private String commitID;
	private String releaseName;
	private double p2pValue;
	public double getP2pValue() {
		return p2pValue;
	}
	public void setP2pValue(double p2pValue) {
		this.p2pValue = p2pValue;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	private Map<String,JavaFile> files;
	public Map<String,JavaFile> getFiles() {
		return files;
	}
	public void addFile(JavaFile file) {
		files.put(file.getFileName(), file);
	}
	public void setFiles(Map<String,JavaFile> files) {
		this.files = files;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Package() {
		clazzs = new ArrayList<Clazz>();
		inheritancePkgs=new ArrayList<String>();
		dependencyPkgs=new ArrayList<String>();
		inheritanceEdge  = new HashMap<String,Integer>(); 
		dependencyEdge  = new HashMap<String,Integer>(); 
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	public List<Clazz> getClazzs() {
		return clazzs;
	}
	public void addClazz(Clazz clazz) {
		clazzs.add(clazz);
	}
	public void setClazzs(ArrayList<Clazz> clazzs) {
		this.clazzs = clazzs;
	}
	public List<String> getInheritancePkgs() {
		return inheritancePkgs;
	}
	public void setInheritancePkgs(List<String> inheritancePkgs) {
		this.inheritancePkgs = inheritancePkgs;
	}
	public void addInheritancePkg(String inheritancePkg) {
		this.inheritancePkgs.add(inheritancePkg);
	}
	public void addDependencyPkg(String dependencyPkg) {
		this.dependencyPkgs.add(dependencyPkg);
	}
	public List<String> getDependencyPkgs() {
		return dependencyPkgs;
	}
	public void setDependencyPkgs(List<String> dependencyPkgs) {
		this.dependencyPkgs = dependencyPkgs;
	}
	
}
