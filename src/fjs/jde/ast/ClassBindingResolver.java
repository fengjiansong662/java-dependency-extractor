package fjs.jde.ast;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassBindingResolver {
	private boolean locked = false;
	private int delay = 2;
	private void releaseLock() {
		locked = false;
	}

	private synchronized boolean obtainLock() {
		if (locked)
			return false;
		locked = true;
		return true;
	}
	private synchronized void waiting(int time) {
		try {
			this.wait((long) time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public ITypeBinding resolveBinding(TypeDeclaration node) {
		
		while (!obtainLock()){ waiting(delay); }
		
		ITypeBinding resolveBinding = node.resolveBinding();
		
		releaseLock();
		
		return resolveBinding;
	}
	
	
	public String getQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}
	public String getQualitifiedName(IVariableBinding attr) {
		while (!obtainLock()) { waiting(delay); }
		String name = attr.getName();
		releaseLock();
		return name;
	}

	public ITypeBinding getSuperClass(ITypeBinding classBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		ITypeBinding superClass = classBinding.getSuperclass();
		
		releaseLock();
		
		return superClass;
	}
	
	public IMethodBinding[] getDeclaredMethods(ITypeBinding classBinding) {
		while (!obtainLock()) { waiting(delay); }
		IMethodBinding[] methods = classBinding.getDeclaredMethods();
		releaseLock();
		return methods;
	}
	public IVariableBinding[] getDeclaredFields(ITypeBinding classBinding){
		while (!obtainLock()) { waiting(delay); }
		IVariableBinding[]attributes=classBinding.getDeclaredFields();
		releaseLock();
		return attributes;
		
	}

}
