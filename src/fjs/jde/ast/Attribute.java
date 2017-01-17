package fjs.jde.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import as.jde.graph.Method;

import fjs.jde.model.Clazz;
import fjs.jde.relationship.IncidenceRelationship;

public class Attribute {
	
	private int [] lineInfo=new int[2];
	public String name;
	private int startLine;
	private int endLine;
	private String attrType;
	public String getAttrType() {
		return attrType;
	}
	public void setAttrType(String attrType) {
		this.attrType = attrType;
	}
	private Clazz clazz;
	private Method method;
	public Clazz getClazz() {
		return clazz;
	}
	public void setClazz(Clazz clazz) {
		this.clazz = clazz;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	private List<Attribute> attributes;
	public Attribute(String name,String attrType,int startLine,int endLine){
		this.attrType=attrType;
		this.name=name;
		this.startLine=startLine;
		this.endLine=endLine;
	}
	public Attribute(String name,String attrType,Clazz clazz){
		this.name=name;
		this.attrType=attrType;
		this.clazz=clazz;
	}
	public int getStartLine() {
		return startLine;
	}
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}
	public int getEndLine() {
		return endLine;
	}
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public Attribute(String name,String attrType,Method method){
		this.name=name;
		this.attrType=attrType;
		this.method=method;
	}
	public List<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public int[] getLineInfo() {
		return lineInfo;
	}
	public void setLineInfo(int i, int j) {
		lineInfo[0]=this.startLine;
		lineInfo[1]=this.endLine;
		
	}
	public List<String> getDistinctName() {
		List<String> attributes1=new ArrayList<String>();
		Iterator it = attributes.iterator();
		for(int i=0;i<attributes.size();i++){
		String name1=attributes.get(i).getName();
		if(!attributes1.contains(name1)){
			attributes1.add(name1);
		}
		}
		return attributes1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
