package fjs.jde.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fjs.jde.ast.Attribute;
import fjs.jde.model.Clazz;
import fjs.jde.model.JavaInterface;

import as.jde.graph.Method;
import as.jde.scm.Commit;

public class CallClazzGraph {//调用图
	private Map<String, Clazz> clazzs = new HashMap<String, Clazz>();
	private Map<Clazz,String> elements = new HashMap<Clazz,String>();
	//private Map<Clazz, List<Clazz>> clazzInvokes = new HashMap<Clazz, List<Clazz>>();
	private Commit fCommit;
	private List<Clazz> classes=new ArrayList<Clazz>();
	private List<JavaInterface> interfaces=new ArrayList<JavaInterface>();
	
	public List<Clazz> getClasses() {
		return classes;
	}

	public List<JavaInterface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<JavaInterface> interfaces) {
		this.interfaces = interfaces;
	}

	public void setClasses(List<Clazz> classes) {
		this.classes = classes;
	}

	public CallClazzGraph(Commit commit) {
		fCommit = commit;
	}

	public Commit getCommit() {
		return fCommit;
	}
	
	/*public synchronized void addClazz(Clazz clazz) {
		if(clazzs.containsKey(clazz.toString())) {
			// Update
			if(clazz.getStart() != -1 && clazz.getEnd() != -1) {
				Clazz value = clazzs.get(clazz.toString());
				value.setFile(clazz.getFile());
				value.setStart(clazz.getStart());
				value.setEnd(clazz.getEnd());
			}
		}
		else {
			// Insert
			clazzs.put(clazz.toString(), clazz);
			classes.add(clazz);
			//elements=clazz.getElement();
			//elements.put(key, value);
			//clazz.setElement(elements);
			
		}
	}*/

	/*public synchronized void addClazzInvokes(Clazz caller, Clazz callee) {//增加调用
		if(clazzInvokes.containsKey(caller)) {
			List<Method> calls = invokes.get(caller);
			calls.add(callee);
		}
		else {
			List<Method> calls = new ArrayList<Method>();
			calls.add(callee);
			invokes.put(caller, calls);
		}
	}*/
	
	public void setClazzsAsChanged(String file, int startLine, int endLine) {
		List<Clazz> affectedClazzs = getChangedClazzs(file, startLine, endLine);
		
		for (Clazz m : affectedClazzs) {
			m.setWasChanged(true);
			System.out.println("CallClazzGraph.java:"+m.getPkg()+"中的"+m.getName()+"发生变化");
		}
	}
	
	public List<Clazz> getChangedClazzs(String file, int start, int end) {
		List<Clazz> changedClazzs = new ArrayList<Clazz>();
		
		for(Map.Entry<String, Clazz> entry: clazzs.entrySet()) {
			Clazz clazz = entry.getValue();
			if(clazz.getFile() == null)
				continue;
			if(clazz.getFile().equals(file) && (Math.min(end, clazz.getEnd()) - Math.max(start, clazz.getStart()) >= 0)) {
				changedClazzs.add(clazz);
			}
		}
		System.out.println("CallClazzGraph:changedClazzs"+changedClazzs.size());
		
		return changedClazzs;
	}
	
	/*public List<Method> getCallersOfMethod(Method method) {
		List<Method> callers = new ArrayList<Method>();
		
		for(Map.Entry<Method, List<Method>> entry: invokes.entrySet()) {
			List<Method> callees = entry.getValue();
			for(Method callee: callees) {
				if(callee.toString().equals(method.toString())) {
					callers.add(entry.getKey());
					break;
				}
			}
		}
		
		return callers;
	}*/

	/*public Map<String, Clazz> getClazzs() {
		return clazzs;
	}

	public synchronized void setClazzs(Map<String, Clazz> clazzs) {
		this.clazzs = clazzs;
	}*/
	
	public Map<Clazz, String> getElements() {
		return elements;
	}

	public void setElements(Map<Clazz, String> elements) {
		this.elements = elements;
	}
	/*public Map<Method, List<Method>> getInvokes() {
		return invokes;
	}

	public synchronized void setInvokes(Map<Method, List<Method>> invokes) {
		this.invokes = invokes;
	}

	public List<Method> getCalledMethods(Method method) {
		return invokes.get(method);
	}*/

	public void addClazz(Clazz clazz) {
		classes.add(clazz);
	}
	
	public void addInterface(JavaInterface javaInterface) {
		interfaces.add(javaInterface);
	}
	
}
