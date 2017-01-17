package fjs.jde.ast;

import java.util.List;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class BlockingBindingResolverClazz {
	private boolean locked = false;
	private int delay = 2;

	public ITypeBinding resolveBinding(TypeDeclaration node) {
		while (!obtainLock()) {
			waiting(delay);
		}

		ITypeBinding resolveBinding = node.resolveBinding();
		releaseLock();

		return resolveBinding;
	}
	
	public IMethodBinding resolveBinding(MethodDeclaration node) {
		while (!obtainLock()){ waiting(delay); }
		
		IMethodBinding resolveBinding = node.resolveBinding();
		
		releaseLock();
		
		return resolveBinding;
	}
	private void releaseLock() {
		locked = false;
	}

	private synchronized boolean obtainLock() {
		if (locked)
			return false;
		locked = true;
		return true;
	}

	public IPackageBinding getPackage(ITypeBinding clazz) {
		while (!obtainLock()) {
			waiting(delay);
		}

		IPackageBinding package1 = clazz.getPackage();

		releaseLock();

		return package1;
	}

	/*
	 * public ITypeBinding resolveClazzBinding(MethodInvocation node) {
	 * while(!obtainLock()){ waiting(delay); }
	 * 
	 * ITypeBinding resolveClazzBinding = node.resolveClazzBinding();
	 * 
	 * releaseLock();
	 * 
	 * return resolveClazzBinding; }
	 */

	public IVariableBinding[] getElementTypes(ITypeBinding clazzBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}
		IVariableBinding[] elementTypes = clazzBinding.getDeclaredFields();
		releaseLock();

		return elementTypes;
	}

	public String getMethodsName(ITypeBinding clazzBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		String name = clazzBinding.getName();

		releaseLock();

		return name;
	}
 
	public String getMethods(ITypeBinding clazzBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		String name = clazzBinding.getName();

		releaseLock();

		return name;
	}

	public ITypeBinding getDeclaringClass(IMethodBinding methodBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		ITypeBinding declaringClass = methodBinding.getDeclaringClass();

		releaseLock();

		return declaringClass;
	}

	public String getName(IMethodBinding methodBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		String name = methodBinding.getName();

		releaseLock();

		return name;
	}
	public String getName(ITypeBinding methodBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		String name = methodBinding.getName();

		releaseLock();

		return name;
	}

	public String getName(IPackageBinding pkg) {
		// while (!obtainLock()) { waiting(delay); }

		String name = pkg.getName();

		releaseLock();

		return name;
	}
	
	//获得变量名
	public String getQualifiedName(IVariableBinding iVariableBinding) {
		while (!obtainLock()) {waiting(delay);}

		String qualifiedName = iVariableBinding.getName();

		releaseLock();

		return qualifiedName;
	}

	//获得变量类型
/*	public String getQualifiedType(IVariableBinding iVariableBinding) {
		while (!obtainLock()) {waiting(delay);}
		//String qualifiedType = iVariableBinding.getType().getName();
		String qualifiedType = iVariableBinding.getType().getQualifiedName();
		//System.out.println(iVariableBinding.getType().getTypeDeclaration().getBinaryName());

		releaseLock();

		return qualifiedType;
	}*/
	public ITypeBinding getQualifiedType(IVariableBinding iVariableBinding) {
		while (!obtainLock()) {waiting(delay);}
		ITypeBinding qualifiedType = iVariableBinding.getType();
		//System.out.println(iVariableBinding.getType().getTypeDeclaration().getBinaryName());
		releaseLock();

		return qualifiedType;
	}
	
	/*public List typeParameters(IMethodBinding methodBinding) {
		while (!obtainLock()){ waiting(delay); }
		//MethodDeclaration node
		List parameters = methodBinding.typeParameters();
		System.out.println("我在这："+parameters.listIterator().next());
		
		releaseLock();
		
		return parameters;
	}*/
	
	
	
	/*
	 * public ITypeBinding resolveConstructorBinding(ClassInstanceCreation node)
	 * { while(!obtainLock()) { waiting(delay); }
	 * 
	 * ITypeBinding resolveConstructorBinding =
	 * node.resolveConstructorBinding();
	 * 
	 * releaseLock();
	 * 
	 * return resolveConstructorBinding; }
	 */

	private synchronized void waiting(int time) {
		try {
			this.wait((long) time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getMethodName(IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String name = methodBinding.getName();
		
		releaseLock();
		
		return name;
	}

	public ITypeBinding[] getMethodParameterTypes(IMethodBinding methodBinding) {
		while (!obtainLock()) { waiting(delay); }		
		
		ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();
		releaseLock();
		
		return parameterTypes;
	}
 
	public Type getType(FieldDeclaration node) {
		while (!obtainLock()) { waiting(delay); }		
		
		Type type = node.getType();
		releaseLock();
		
		return type;
	}
	
	public String getMethodQualifiedName(ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		String qualifiedName = iTypeBinding.getQualifiedName();
		
		releaseLock();
		
		return qualifiedName;
	}
	
	public ITypeBinding getSuperClass(ITypeBinding classBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		ITypeBinding superClass = classBinding.getSuperclass();
		
		releaseLock();
		
		return superClass;
	}
	
	public boolean[] getOverrideMethod(ITypeBinding classBinding) {
		while (!obtainLock()) { waiting(delay); }
		
		IMethodBinding[] overrideMethod = classBinding.getDeclaredMethods();
		boolean[] override=new boolean[overrideMethod.length];
		for(int i=0 ;i<overrideMethod.length;i++){
			override[i]=overrideMethod[i].overrides(overrideMethod[i]);
		}
		
		releaseLock();
		return override;
	}
	
	public IVariableBinding [] getDeclaredFields (ITypeBinding iTypeBinding) {
		while (!obtainLock()) { waiting(delay); }

		IVariableBinding [] attributes = iTypeBinding.getDeclaredFields();
		releaseLock();
		
		return attributes;
	}
	/*
	 * public ITypeBinding[] getParameterTypes(ITypeBinding clazzBinding) {
	 * while (!obtainLock()) { waiting(delay); }
	 * 
	 * ITypeBinding[] parameterTypes = clazzBinding.getDeclaredFields()
	 * 
	 * releaseLock();
	 * 
	 * return parameterTypes; }
	 */

	public ITypeBinding[] getParameterTypes(IMethodBinding methodBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		ITypeBinding[] parameterTypes = methodBinding.getParameterTypes();

		releaseLock();

		return parameterTypes;
	}
	public ITypeBinding getReturnType(IMethodBinding methodBinding) {
		while (!obtainLock()) {
			waiting(delay);
		}

		ITypeBinding returnType = methodBinding.getReturnType();

		releaseLock();

		return returnType;
	}
}
