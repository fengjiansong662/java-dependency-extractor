package fjs.jde.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import as.jde.graph.Method;

public class Relationship {
	private String clazz1;
	private List<String> clazz2;
	private String relationshipType;
	public Relationship() {
		clazz2=new ArrayList<String>();
	}
	public String getClazz1() {
		return clazz1;
	}
	public void setClazz1(String clazz1) {
		this.clazz1 = clazz1;
	}
	public void addClazz2(String clazz2){
		this.clazz2.add(clazz2);
	}
	public List<String> getClazz2() {
		return clazz2;
	}
	public void setClazz2(List<String> clazz2) {
		this.clazz2 = clazz2;
	}
	
}
