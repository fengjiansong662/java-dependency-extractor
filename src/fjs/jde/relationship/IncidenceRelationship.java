package fjs.jde.relationship;

import java.util.ArrayList;

public class IncidenceRelationship extends Relationship {
//关联关系
	private ArrayList<String> localLine;//所在行号
	private ArrayList<String> element;//属性名
	private ArrayList<String> incidenceClassName;
	private ArrayList<String> incidenceMultiplicity;
	
	public IncidenceRelationship() {
		localLine =new ArrayList<String>(); 
		element=new ArrayList<String>();
		incidenceClassName=new ArrayList<String>();
		incidenceMultiplicity=new ArrayList<String>();
	}
	public void addIncidenceMultiplicity(String incidenceMultiplicity) {
		this.incidenceMultiplicity.add(incidenceMultiplicity);
	} 
	
	public ArrayList<String> getIncidenceMultiplicity() {
		return incidenceMultiplicity;
	}
	public void setIncidenceMultiplicity(ArrayList<String> incidenceMultiplicity) {
		this.incidenceMultiplicity = incidenceMultiplicity;
	}
	public void addIncidenceClassName(String incidenceClassName) {
		this.incidenceClassName.add(incidenceClassName);
	} 
	
	public void addLocalLine(String localLine) {
		this.localLine.add(localLine);
	}
	public ArrayList<String> getLocalLine() {
		return localLine;
	}

	public void setLocalLine(ArrayList<String> localLine) {
		this.localLine = localLine;
	}

	public void addElement(String element) {
		this.element.add(element);
	}
	public ArrayList<String> getElement() {
		return element;
	}

	public void setElement(ArrayList<String> element) {
		this.element = element;
	}

	public ArrayList<String> getIncidenceClassName() {
		return incidenceClassName;
	}

	public void setIncidenceClassName(ArrayList<String> incidenceClassName) {
		this.incidenceClassName = incidenceClassName;
	}
	
}
