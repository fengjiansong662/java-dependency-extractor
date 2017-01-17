package jde.ast;

import java.util.Stack;

import jde.dependency.CallGraph;
import jde.entity.Method;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ASTMethodVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {
	
	private Stack<Method> methodStack;
	private String file;
	CompilationUnit cu;
	CallGraph cg;
	private BlockingBindingResolver bbr;
	
	//该数组存储从源代码中解析出来的类属性集合，用于抽取方法所访问的属性
	//2015-04-16注释，暂时不需要此功能
	//private ArrayList<IBinding> fieldBindings = new ArrayList<IBinding>();

	public ASTMethodVisitor(String file, CompilationUnit cu, CallGraph cg, BlockingBindingResolver bbr) {
		methodStack = new Stack<Method>();
		this.file = file;
		this.cg = cg;
		this.cu = cu;
		this.bbr = bbr;
	}
	
	//访问attribute declaration属性
	/*2015-04-16注释，暂时不需要此功能
	@Override
	public boolean visit(FieldDeclaration node) {
		List<IBinding> iFieldBindings = bbr.resolveDeclaringAttributeBinding(node);
		fieldBindings.addAll(iFieldBindings);
		
		return super.visit(node);
	}
	*/
	
	@Override
	public boolean visit(MethodDeclaration node) {
					
		IMethodBinding methodBinding = bbr.resolveBinding(node);
		Method method = createMethodFromBinding(node, methodBinding);

		if(method != null) {
			// Insert
			cg.addMethod(method);								
			// Push onto stack
			methodStack.push(method);
			
			//不考虑没有实现体的方法（如interface和抽象类内）
			/*2015-04-16注释，暂时不需要此功能
			if(node.getBody()!= null) {	
				ASTMethodBodyVisitor visitor = new ASTMethodBodyVisitor(method, fieldBindings);
				node.getBody().accept(visitor);
			}
			*/
		}
			
		return super.visit(node);
	}

	/**
	 * This function overrides what to do when we reach
	 * the end of a method declaration.
	 */
	@Override
	public void endVisit(MethodDeclaration node) {
		if(!methodStack.isEmpty())
			methodStack.pop();
	}
	
	/**
	 * This function overrides what to do when we reach
	 * a method invocation statement.
	 */
	@Override
	public boolean visit(MethodInvocation node) {
		
		IMethodBinding methodBinding = bbr.resolveMethodBinding(node);	
		Method method = createMethodFromBinding(null, methodBinding);

		int line = cu.getLineNumber(node.getStartPosition());
		
		// Insert
		if(method != null) {
			cg.addMethod(method);
	
			if(!methodStack.isEmpty())
				cg.addInvokes(methodStack.peek(), method, line);
		}

		return super.visit(node);
	}

	/**
	 * This function overrides what to do when we reach
	 * a constructor invocation.
	 */
	@Override
	public boolean visit(ClassInstanceCreation node) {

		// Resolve the constructor
		IMethodBinding methodBinding = bbr.resolveConstructorBinding(node);
		Method method = createMethodFromBinding(null, methodBinding);
		int line = cu.getLineNumber(node.getStartPosition());

		// Insert
		if(method != null) {
			cg.addMethod(method);

			if(!methodStack.isEmpty())
				cg.addInvokes(methodStack.peek(), method, line);
		}
		
		return super.visit(node);
	}
	
	private Method createMethodFromBinding(MethodDeclaration node, IMethodBinding methodBinding) {
		if(methodBinding != null) {
			Method method = new Method();
			method.setName(bbr.getName(methodBinding));
			ITypeBinding rtnTypeBinding = bbr.getDeclaringClass(methodBinding);
			String rtnType = bbr.getQualifiedName(rtnTypeBinding);
			
			if(node != null) {
				// Handle working directory
				method.setFile(file); 
				method.setStart(cu.getLineNumber(node.getStartPosition()));
				method.setEnd(cu.getLineNumber(node.getStartPosition() + node.getLength()));
				method.setReturnType(rtnType);
			}
			
			// Parameters
			ITypeBinding[] parameters = bbr.getParameterTypes(methodBinding);
			if(parameters.length > 0) {
				for(int i = 0; i < parameters.length; i++) {
					if(parameters[i] != null)
						method.addParameter(bbr.getQualifiedName(parameters[i]));
				}
			}
			
			// Class and Package
			ITypeBinding clazz = bbr.getDeclaringClass(methodBinding);
			if(clazz != null) {
				method.setClazz(bbr.getName(clazz));
				
				IPackageBinding pkg = bbr.getPackage(clazz);
				if(pkg != null)
					method.setPkg(bbr.getName(pkg));
			}

			return method;
		}
		else
			return null;
	}
}
