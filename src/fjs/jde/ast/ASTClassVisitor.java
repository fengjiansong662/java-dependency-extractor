package jde.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import jde.dependency.CallGraph;
import jde.dependency.ClassGraph;
import jde.entity.Attribute;
import jde.entity.JavaClass;
import jde.entity.JavaFile;
import jde.entity.Method;
import jde.entity.Module;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import fjs.jde.graph.Clazz;

public class ASTClassVisitor extends ASTVisitor {
	
	private Stack<JavaClass> classStack;
	private JavaFile file;
	//2014-05-16注释
	//private ArrayList<String> imports;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private CompilationUnit cu;
	private ClassGraph zg;
	private CallGraph cg;
	private ClassBindingResolver cbr;
	private BlockingBindingResolver bbr;
	
	public ASTClassVisitor(JavaFile file, CompilationUnit cu,
			ClassGraph zg, CallGraph cg, ClassBindingResolver cbr, BlockingBindingResolver bbr) {
		classStack = new Stack<JavaClass>();
		this.zg = zg;
		this.cu = cu;
		this.cg = cg;
		this.bbr = bbr;
		this.cbr = cbr;
		
		this.file = file;

		//2015-04-04注释，该功能已经移至系统运行最初
		//记录该java文件的代码行数（不算注释等）
		//this.loc = cu.toString().split("\n").length;
		//this.file.setTotalLoC(loc);
		
		//2014-05-16注释
		//this.imports = new ArrayList<String>();
	}
	
	//解析import语句
	//2014-05-16注释
	/*
	@Override
	public boolean visit(ImportDeclaration node) {
		String importPkg = node.toString();
		imports.add(importPkg);
		file.addImport(importPkg);	
		return super.visit(node);
	}
	*/
	
	//解析类声明代码，从中构造JavaClass
	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding classBinding = cbr.resolveBinding(node);
				
		JavaClass clazz = createClassFromBinding(node, classBinding);
		
		if(clazz != null) {
			zg.addClass(clazz, this.file, true);
			classStack.push(clazz);
		}
		return super.visit(node);
	}

	private JavaClass createClassFromBinding(TypeDeclaration node,
			ITypeBinding classBinding) {
		
		if(classBinding != null) {
			
			JavaClass clazz = new JavaClass();
			//name, file, type, etc
			getClassBasicInfo(node, classBinding, clazz);			
			// Attributes			
			addClassAttributes(classBinding, clazz);
			// Superclass
			addSuperClassInfo(classBinding, clazz);
			// Methods
			addDeclaredMethods(classBinding, clazz);
								
			return clazz;
		}
		else
			return null;
	}

	private void getClassBasicInfo(TypeDeclaration node, ITypeBinding classBinding, JavaClass clazz) {
		String className = cbr.getQualifiedName(classBinding);
		clazz.setName(className);
		clazz.setFile(this.file);
		if(node.toString().split("\n")[0].contains(" abstract "))
			clazz.setAbstract(true);
		
		String pkg = classBinding.getPackage().getName();
		Module module = new Module(pkg, "");
		clazz.setPackage (module);
		
		int startLine = cu.getLineNumber(node.getStartPosition());
		//下面这行代码无法得到正确的结束行号，不少类返回 -1，故在最后加了一个-1
		int endLine = cu.getLineNumber(node.getStartPosition()+node.getLength()-1);
		//采用下面这种方式，不再考虑空行，故与实际对应不上。
		//int endLine =  startLine + node.toString().split("\n").length;
		
		clazz.setStartLine(startLine);
		clazz.setEndLine(endLine);
	}

	private void addDeclaredMethods(ITypeBinding classBinding, JavaClass clazz) {
		ArrayList<Method> methods = new ArrayList<Method>();
		IMethodBinding [] methodBindings = cbr.getDeclaredMethods(classBinding);
		if(methods != null) {
			for (int i = 0; i < methodBindings.length; i++) {
				IMethodBinding methodBinding = methodBindings[i];
				if (methodBinding != null) {
					String methodName = bbr.getName(methodBinding);
					ITypeBinding rtnTypeBinding = bbr.getDeclaringClass(methodBinding);
					String rtnType = cbr.getQualifiedName(rtnTypeBinding);
					
					Method method = new Method();
					method.setName(methodName);
					method.setReturnType(rtnType);
					method.setClazz(clazz.getName());
					method.setFile(file.getDistinctName());
					
					ITypeBinding[] paramTypeBindings = bbr.getParameterTypes(methodBinding);
					for(ITypeBinding paramTypeBinding : paramTypeBindings) {
						String paramType = cbr.getQualifiedName(paramTypeBinding);
						method.addParameter(paramType);
					}

					//此处应从CallGraph中去找到已存在的method对象
					Map.Entry<String, Method> result = cg.findExistingMethod(method);
					
					if(result == null) 	{
						//找不到的情况1：缺省的构造函数，在代码中并未显式出现，但class visitor搜索时可以发现
						//找不到的情况2：inner的类和方法，method visitor将其忽略掉了
						clazz.addMethod(method);	
					}
					else {
						Method existMethod = result.getValue();
						existMethod.setReturnType(method.getReturnType());
						clazz.addMethod(existMethod);
					}					
				}
			}
		}		
	}

	private void addSuperClassInfo(ITypeBinding classBinding, JavaClass clazz) {
		ITypeBinding superClass = cbr.getSuperClass(classBinding);
		if(superClass != null) {
			clazz.setSuperClassName(cbr.getQualifiedName(superClass));
		}		
	}

	private void addClassAttributes(ITypeBinding classBinding, Clazz clazz) {
		IVariableBinding[] attributes = cbr.getDeclaredFields(classBinding);
		if(attributes.length > 0) {
			for(int i = 0; i < attributes.length; i++) {
				IVariableBinding attr = attributes[i];
				if (attr != null) {
					Attribute attribute = new Attribute(cbr.getQualitifiedName(attr), 
							cbr.getQualifiedName(attr.getType()), 
							clazz);
					clazz.addAttribute(attribute);
				}
			}
		}		
	}
	
	//解析类的属性，重点关心其所在行号
	@Override
	public boolean visit(FieldDeclaration node) {
		List<VariableDeclarationFragment> fragments = node.fragments();
		
		//每个variableObj代表着该行定义的一个变量，例如：int a,b,c，则会有三个变量
		for(VariableDeclarationFragment variableObj : fragments) {
			
			if(variableObj instanceof VariableDeclarationFragment){
		
				String attrName = ((VariableDeclarationFragment)variableObj).getName().toString();
				int startLine = cu.getLineNumber(node.getStartPosition());
				int endLine = cu.getLineNumber(node.getStartPosition()+node.getLength());
				
				Attribute att = new Attribute(attrName, "", startLine, endLine);		
				this.attributes.add(att);
			}
		}
		return super.visit(node);		
	}
	
	/**
	 * This function overrides what to do when we reach
	 * the end of a method declaration.
	 */
	@Override
	public void endVisit(TypeDeclaration node) {
		//当访问一个类结束后，对其进行属性的行号设置等
		if(!classStack.isEmpty()) {
			JavaClass jc = classStack.pop();
			for(Attribute attClass : jc.getAttributes()) {				
				for(Attribute att : this.attributes) {
					if(attClass.getDistinctName().contains(att.name)) {
						int [] lineInfo = att.getLineInfo();
						attClass.setLineInfo(lineInfo[0], lineInfo[1]);
						break;
					}
				}
			}
		}
	}
}
