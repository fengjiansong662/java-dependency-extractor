package fjs.jde.model;

import java.util.ArrayList;
import java.util.List;

public class JavaInterface {
	private List<String> superClassName;
	public String name;
	public JavaInterface() {
		superClassName=new ArrayList<String>();
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<String> getSuperClassName() {
		return superClassName;
	}
	public void setSuperClassName(List<String> superClassName) {
		this.superClassName = superClassName;
	}
	public void addSuperClassName(String qualifiedName) {
		this.superClassName.add(qualifiedName);
	}
}
