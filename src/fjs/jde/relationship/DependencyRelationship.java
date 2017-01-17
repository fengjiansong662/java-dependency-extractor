package fjs.jde.relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import as.jde.graph.Method;

public class DependencyRelationship extends Relationship{
	//������ϵ
	
	private ArrayList<String> local;//�������ֵĵط��������֣�����ֵ���ͣ��������ͣ�������
	private Map<Method,String> relationP;//����+������
	private ArrayList<String> dependencyClassName;//�洢������������ϵ������
	private ArrayList<String> depandMultiplicity;//ȥ���ظ�������������count���Լ�¼������
	
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
