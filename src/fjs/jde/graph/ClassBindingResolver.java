package jde.ast;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassBindingResolver extends BindingResolver {

	public ITypeBinding resolveBinding(TypeDeclaration node) {
		
		while (!obtainLock()){ waiting(delay); }
		
		ITypeBinding resolveBinding = node.resolveBinding();
		
		releaseLock();
		
		return resolveBinding;
	}
	
	public IVariableBinding [] getDeclaredFields (ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }

		IVariableBinding [] attributes = iTypeBinding.getDeclaredFields();
		releaseLock();
		
		return attributes;
	}
	
	public String getQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}
	
	public String getName(ITypeBinding clazz) {
		while(!obtainLock()) { waiting(delay); }
		
		String name = clazz.getName();
		
		releaseLock();
		
		return name;
	}

	public String getName(IPackageBinding pkg) {
		while (!obtainLock()) { waiting(delay); }
		
		String name = pkg.getName();
		
		releaseLock();
		
		return name;
	}

	public String getQualitifiedName(IVariableBinding attr) {
		while (!obtainLock()) { waiting(delay); }
		String name = attr.getName();
		releaseLock();
		return name;
	}
	

	public IPackageBinding getPackage(ITypeBinding clazz) {
		while(!obtainLock()){ waiting(delay); }
		
		IPackageBinding package1 = clazz.getPackage();
		
		releaseLock();
		
		return package1;
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

}
