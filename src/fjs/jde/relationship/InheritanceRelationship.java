package fjs.jde.relationship;

import java.util.ArrayList;

import as.jde.graph.Method;

public class InheritanceRelationship extends Relationship{
//�̳й�ϵ
	private ArrayList<Method> overWriteMethod;
	private ArrayList<Method> overRideMethod;
	
	public InheritanceRelationship(){
		overWriteMethod=new ArrayList<Method>();
		overRideMethod=new ArrayList<Method>();
	}
	
	
	
}
