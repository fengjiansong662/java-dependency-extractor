package as.jde.ast;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class BlockingBindingResolver {
	private boolean locked = false;
	private int delay = 2;
	
	
	public IMethodBinding resolveBinding(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		IMethodBinding resolveBinding = node.resolveBinding();
		
		releaseLock();
		
		return resolveBinding;
	}
	public Type getReturnType2(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		Type returnType2 = node.getReturnType2();
		
		releaseLock();
		
		return returnType2;
	}
	public List parameters(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		List parameters = node.parameters();
		
		releaseLock();
		
		return parameters;
	}
	
	
	public List typeParameters(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		List parameters = node.typeParameters();
		//System.out.println("ЮвдкетЃК"+parameters.listIterator().next());
		
		releaseLock();
		
		return parameters;
	}
	private void releaseLock() {
		locked = false;
	}
	
	private synchronized boolean obtainLock() {
		if (locked) return false;
		locked = true;
		return true;
	}

	public IPackageBinding getPackage(ITypeBinding clazz) {
		while(!obtainLock()){ waiting(delay); }
		
		IPackageBinding package1 = clazz.getPackage();
		
		releaseLock();
		
		return package1;
	}

	public IMethodBinding resolveMethodBinding(MethodInvocation node) {
		while(!obtainLock()){ waiting(delay); }
		
		IMethodBinding resolveMethodBinding = node.resolveMethodBinding();
		
		releaseLock();
		
		return resolveMethodBinding;
	}

	public ITypeBinding[] getParameterTypes(IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }		
		ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
		
		releaseLock();
		
		return parameterTypes;
	}

	public String getName(IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String name = methodBinding.getName();
		
		releaseLock();
		
		return name;
	}

	public ITypeBinding getDeclaringClass(IMethodBinding methodBinding) {
		while(!obtainLock()) { waiting(delay); }
		
		ITypeBinding declaringClass = methodBinding.getDeclaringClass();
		
		releaseLock();
		
		return declaringClass;
	}

	public String getName(ITypeBinding clazz) {
		while(!obtainLock()) { waiting(delay); }
		
		String name = clazz.getName();
		
		releaseLock();
		
		return name;
	}

	public String getName(IPackageBinding pkg) {
		//while (!obtainLock()) { waiting(delay); }
		
		String name = pkg.getName();
		
		releaseLock();
		
		return name;
	}

	public String getQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}

	public IMethodBinding resolveConstructorBinding(ClassInstanceCreation node) {
		while(!obtainLock()) { waiting(delay); }
		
		IMethodBinding resolveConstructorBinding = node.resolveConstructorBinding();
		
		releaseLock();
		
		return resolveConstructorBinding;
	}
	
	private synchronized void waiting(int time) {
		try {
			this.wait((long)time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public ITypeBinding[] getInternalParametersTypes(
			IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }		
		ITypeBinding[] internalParametersTypes = methodBinding.getTypeParameters();
		
		releaseLock();
		
		return internalParametersTypes;
	}
	/*public IMethodBinding getInternalParametersTypes(FieldDeclaration node){

		IMethodBinding resolveBinding = node.
		
		releaseLock();
		
		return resolveBinding;
	}*/

	public String getInternalParametersQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}
	
}
