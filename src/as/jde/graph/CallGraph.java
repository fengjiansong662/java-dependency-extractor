package as.jde.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import as.jde.scm.Commit;

public class CallGraph {//调用图
	private Map<String, Method> methods = new HashMap<String, Method>();
	private Map<String, Method> clazz = new HashMap<String, Method>();
	private Entry<String, Method> existingMethod=(Entry<String, Method>) new HashMap<String, Method>();
	private Map<Method, List<Method>> invokes = new HashMap<Method, List<Method>>();
	private Commit fCommit;
	
	public CallGraph(Commit commit) {
		fCommit = commit;
	}

	public Commit getCommit() {
		return fCommit;
	}
	
/*	public synchronized void addMethod(Method method) {
		System.out.println(method.toString());
		if(methods.containsKey(method.toString())) {
			// Update
			if(method.getStart() != -1 && method.getEnd() != -1) {
				Method value = methods.get(method.toString());
				value.setFile(method.getFile());
				value.setStart(method.getStart());
				value.setEnd(method.getEnd());
			}
		}
		else {
			// Insert
			methods.put(method.toString(), method);
			
		}
	}*/
	public synchronized void addMethod(Method method) {
		//System.out.println(method.getClazz());
		if(!clazz.containsKey(method.getClazz())){
			clazz.put(method.getClazz(), method);
			System.out.println(method.getClazz());
		}
	}
	
	public synchronized void addInvokes(Method caller, Method callee) {//增加调用
		if(invokes.containsKey(caller)) {
			List<Method> calls = invokes.get(caller);
			calls.add(callee);
		}
		else {
			List<Method> calls = new ArrayList<Method>();
			calls.add(callee);
			invokes.put(caller, calls);
		}
	}
	
	public void setMethodsAsChanged(String file, int startLine, int endLine) {
		List<Method> affectedMethods = getChangedMethods(file, startLine, endLine);
		
		for (Method m : affectedMethods) {
			m.setWasChanged(true);
		}
	}
	
	public void setMethodIsOverride(Method method){
		List<Method> overrideMethods = new ArrayList<Method>();
		for(Method m:overrideMethods){
			m.setOverride(true);
		}
	}
	
	public List<Method> getChangedMethods(String file, int start, int end) {
		List<Method> changedMethods = new ArrayList<Method>();
		
		for(Map.Entry<String, Method> entry: methods.entrySet()) {
			Method method = entry.getValue();
			if(method.getFile() == null)
				continue;
			if(method.getFile().equals(file) && (Math.min(end, method.getEnd()) - Math.max(start, method.getStart()) >= 0)) {
				changedMethods.add(method);
			}
		}
		
		return changedMethods;
	}
	
	public List<Method> getCallersOfMethod(Method method) {
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
	}

	public Map<String, Method> getMethods() {
		return methods;
	}

	public synchronized void setMethods(Map<String, Method> methods) {
		this.methods = methods;
	}

	public Map<Method, List<Method>> getInvokes() {
		return invokes;
	}

	public synchronized void setInvokes(Map<Method, List<Method>> invokes) {
		this.invokes = invokes;
	}

	public List<Method> getCalledMethods(Method method) {
		return invokes.get(method);
	}

	public Entry<String, Method> findExistingMethod(Method method) {
		existingMethod.setValue(method);
		return existingMethod;
	}

	public void addInvokes(Method caller, Method callee, int line) {
		if(invokes.containsKey(caller)) {
			List<Method> calls = invokes.get(caller);
			calls.add(callee);
			callee.setStart(line);
		}
		else {
			List<Method> calls = new ArrayList<Method>();
			calls.add(callee);
			callee.setStart(line);
			invokes.put(caller, calls);
		}
		
	}
	
	
}
