package fjs.jde.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import as.jde.graph.Method;

public class DependencyRelationship extends Relationship{
	//依赖关系
	
	private ArrayList<String> local;//依赖出现的地方，有三种：返回值类型，参数类型，方法体
	private Map<Method,String> relationP;//方法+属性名
	private ArrayList<String> dependencyClassName;//存储所有有依赖关系的类名
	private ArrayList<String> depandMultiplicity;//去除重复的类名，并用count属性记录多重数
	
	public DependencyRelationship() {
		relationP = new HashMap<Method,String>(); 
		local=new ArrayList<String>();
		dependencyClassName=new ArrayList<String>();
		depandMultiplicity=new ArrayList<String>();
	}
	
	public void addDepandMultiplicity(String depandMultiplicity) {
		this.depandMultiplicity.add(depandMultiplicity);
	}
	public ArrayList<String> getDepandMultiplicity() {
		return depandMultiplicity;
	}

	public void setDepandMultiplicity(ArrayList<String> depandMultiplicity) {
		this.depandMultiplicity = depandMultiplicity;
	}
	public void addDependencyClassName(String dependencyClassName) {
		this.dependencyClassName.add(dependencyClassName);
	}
	public ArrayList<String> getDependencyClassName() {
		return dependencyClassName;
	}

	public void setDependencyClassName(ArrayList<String> dependencyClassName) {
		this.dependencyClassName = dependencyClassName;
	}

	
	public void addRelationP(Method method,String dependencyClassName) {
		this.relationP.put(method, dependencyClassName);
	}
	public void addLocal(String local) {
		this.local.add(local);
	}
	public ArrayList<String> getLocal() {
		return local;
	}
	public void setLocal(ArrayList<String> local) {
		this.local = local;
	}
	public Map<Method, String> getRelationP() {
		return relationP;
	}
	public void setRelationP(Map<Method, String> relationP) {
		this.relationP = relationP;
	}
	
	

	
}
