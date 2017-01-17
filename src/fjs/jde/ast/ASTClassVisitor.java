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
	//2014-05-16ע��
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

		//2015-04-04ע�ͣ��ù����Ѿ�����ϵͳ�������
		//��¼��java�ļ��Ĵ�������������ע�͵ȣ�
		//this.loc = cu.toString().split("\n").length;
		//this.file.setTotalLoC(loc);
		
		//2014-05-16ע��
		//this.imports = new ArrayList<String>();
	}
	
	//����import���
	//2014-05-16ע��
	/*
	@Override
	public boolean visit(ImportDeclaration node) {
		String importPkg = node.toString();
		imports.add(importPkg);
		file.addImport(importPkg);	
		return super.visit(node);
	}
	*/
	
	//�������������룬���й���JavaClass
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
		//�������д����޷��õ���ȷ�Ľ����кţ������෵�� -1������������һ��-1
		int endLine = cu.getLineNumber(node.getStartPosition()+node.getLength()-1);
		//�����������ַ�ʽ�����ٿ��ǿ��У�����ʵ�ʶ�Ӧ���ϡ�
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

					//�˴�Ӧ��CallGraph��ȥ�ҵ��Ѵ��ڵ�method����
					Map.Entry<String, Method> result = cg.findExistingMethod(method);
					
					if(result == null) 	{
						//�Ҳ��������1��ȱʡ�Ĺ��캯�����ڴ����в�δ��ʽ���֣���class visitor����ʱ���Է���
						//�Ҳ��������2��inner����ͷ�����method visitor������Ե���
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
	
	//����������ԣ��ص�����������к�
	@Override
	public boolean visit(FieldDeclaration node) {
		List<VariableDeclarationFragment> fragments = node.fragments();
		
		//ÿ��variableObj�����Ÿ��ж����һ�����������磺int a,b,c���������������
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
		//������һ��������󣬶���������Ե��к����õ�
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
