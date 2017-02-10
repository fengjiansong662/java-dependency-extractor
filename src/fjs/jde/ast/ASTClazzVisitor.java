package fjs.jde.ast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import as.jde.graph.Method;

import fjs.jde.graph.CallClazzGraph;
import fjs.jde.method.P2p;
import fjs.jde.model.Clazz;
import fjs.jde.model.ClazzEdge;
import fjs.jde.model.Package;
import fjs.jde.model.JavaFile;
import fjs.jde.model.JavaInterface;
import fjs.jde.sql.SQLMethod;     //与A2A计算有关，写p2p时注释掉，以后还要放开

public class ASTClazzVisitor extends org.eclipse.jdt.core.dom.ASTVisitor {

	private Stack<Clazz> clazzStack;
	//private Stack<JavaInterface> interfaceStack;
	private JavaFile file;
	CompilationUnit cu;
	CallClazzGraph ccg;
	private ClassBindingResolver cbr;
	private BlockingBindingResolverClazz bbrc;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private String commitID;
	private String releaseName;
	public static Map<String,Set<JavaFile>> pcf=new HashMap<String,Set<JavaFile>>();
	public static Map<String,ClazzEdge> clazzEdges=new HashMap<String,ClazzEdge>();
	
	public static void putAdd(String p,Map<String,Set<JavaFile>> m,JavaFile file){
        if(!m.containsKey(p)){
            m.put(p, new HashSet<JavaFile>());
        }
        if(m.get(p).contains(file)==false){
        	m.get(p).add(file);
        }
    }
	
	public static void putAdd(String p,Map<String,Set<String>> m,String file){
        if(!m.containsKey(p)){
            m.put(p, new HashSet<String>());
        }
        if(m.get(p).contains(file)==false){
        	m.get(p).add(file);
        }
    }
	
	public ASTClazzVisitor(JavaFile file, CompilationUnit cu, CallClazzGraph ccg,
			ClassBindingResolver cbr, BlockingBindingResolverClazz bbrc,String commitID,String releaseName) {
		clazzStack = new Stack<Clazz>();
		this.file = file;
		this.ccg = ccg;
		this.cu = cu;
		this.cbr = cbr;
		this.bbrc = bbrc;
		this.file.setCommitID(commitID);
		this.file.setReleaseName(releaseName);
		this.commitID=commitID;
		this.releaseName=releaseName;

	}
	//解析import语句
	@Override
	public boolean visit(ImportDeclaration node) {
		String importPkg = node.toString();
		file.addImport(importPkg);	
		return super.visit(node);
	}
	
	/**
	 * This function overrides what to do when we reach a method declaration.
	 */
	//@Override
	public boolean visit(TypeDeclaration node) {
		// Insert the clazz into the DB
		ITypeBinding classBinding = cbr.resolveBinding(node);
		if (classBinding != null) {
			this.file.setPkg(bbrc.getName(bbrc.getPackage(classBinding)));
			System.out.println("ASTClazzVisitor"+bbrc.getName(bbrc.getPackage(classBinding)));
			this.file .setFileName(bbrc.getName(classBinding));
			putAdd(bbrc.getName(bbrc.getPackage(classBinding)),pcf,this.file);
			if (classBinding.isInterface()) {
				for (int i = 0; i < classBinding.getInterfaces().length; i++) {
					JavaInterface javaInterface = createInterfaceFromBinding(
							node, classBinding.getInterfaces()[i]);
					if (javaInterface != null) {
						ccg.addInterface(javaInterface);
					}
				}

			} else {
				Clazz clazz = createClazzFromBinding(node, classBinding);
				if (clazz != null) {

					
					// Insert
					ccg.addClazz(clazz);
					//SQLMethod.insertClazz(clazz);
					clazz.setClass1(clazz.getName());
					isOverride(clazz);

				}
			}
			
		}
		SQLMethod.insertFile(this.file);       //与A2A计算有关，写p2p时注释掉，以后还要放开
		
		/*for(Iterator i=pcf.keySet().iterator();i.hasNext();){
			Object obj=i.next();
			System.out.println("key="+obj+" value="+pcf.get(obj));
		}*/
		//Iterator it =pcf.entrySet().iterator();
		/*while(it.hasNext()){
			Map.Entry<String,Set<String>> entry=(Map.Entry<String, Set<String>>)it.next();
			String key=entry.getKey();
			Set<String> value=entry.getValue();
			System.out.println("Key="+key+" value="+value.size());
		}*/
		//P2p.pcfCollection.put(commitID, pcf);
		
		return super.visit(node);
	}
	
	
	private JavaInterface createInterfaceFromBinding(TypeDeclaration node,
			ITypeBinding classBinding) {
		if (classBinding != null) {
			JavaInterface javaInterface=new JavaInterface();
			// SuperClass
			ITypeBinding superClassName = cbr.getSuperClass(classBinding);
			if (superClassName != null) {
				if (!superClassName.getName().equals("Object")) {
					javaInterface.addSuperClassName(cbr.getQualifiedName(superClassName));
					}
			}
			getInterfaceBasicInfo(node, classBinding,javaInterface);
			return javaInterface;
		}else{
			return null;
		}
	}
	private void getInterfaceBasicInfo(TypeDeclaration node,
			ITypeBinding classBinding, JavaInterface javaInterface) {
		String interfaceName = cbr.getQualifiedName(classBinding);
		javaInterface.setName(interfaceName);
	}
	/*private void addMethodAttributes(ITypeBinding clazzBinding, Clazz clazz) {
		IMethodBinding[] methods = cbr.getDeclaredMethods(clazzBinding);
		IVariableBinding[] attributes =methods[i].getk
		if(attributes.length > 0) {
			for(int i = 0; i < attributes.length; i++) {
				IVariableBinding attr = attributes[i];
				if (attr != null) {
					Attribute attribute = new Attribute(cbr.getQualitifiedName(attr), 
							cbr.getQualifiedName(attr.getType()), 
							clazz);
					methods.addAttribute(attribute);
				}
			}
		}		
	}*/
	/*private void addMethodAttributes(FieldDeclaration node) {
		List<VariableDeclarationFragment> fragments = node.fragments();
		List<Method> method=clazz.getMethods();
		//每个variableObj代表着该行定义的一个变量，例如：int a,b,c，则会有三个变量
				for(VariableDeclarationFragment variableObj : fragments) {
					
					if(variableObj instanceof VariableDeclarationFragment){
						String attrName = ((VariableDeclarationFragment)variableObj).getName().toString();
						int startLine = cu.getLineNumber(node.getStartPosition());
						int endLine = cu.getLineNumber(node.getStartPosition()+node.getLength());
						
						Attribute att = new Attribute(attrName, "", startLine, endLine);		
						this.attributes.add(att);
						for(int j=0;j<method.size();j++){
							System.out.println((method.get(j).getName()).equals(variableObj.resolveBinding().getDeclaringMethod().getName().toString()));
							if((method.get(j).getName()).equals(variableObj.resolveBinding().getDeclaringMethod().getName().toString())){
								method.get(j).addAttribute(att);
							}
						}
						
					}
				}
					
	}
	*/
	/*private void addClassAttributes(ITypeBinding classBinding) {
		IVariableBinding[] attributes = cbr.getDeclaredFields(classBinding);
		
		System.out.println("attributes.length:"+attributes.length);
		if(attributes.length > 0) {
			for(int i = 0; i < attributes.length; i++) {
				IVariableBinding attr = attributes[i];
				if (attr != null) {
					Attribute attribute = new Attribute(cbr.getQualitifiedName(attr), 
							cbr.getQualifiedName(attr.getType()), 
							clazz);
					//attribute.setLineInfo(i, j);
					System.out.println(attribute.name);
					System.out.println("我在这："+attribute.getLineInfo()[1]);
					clazz.addAttribute(attribute);
				}
			}
		}		
	}
	
	*/
	
	
	private Clazz createClazzFromBinding(TypeDeclaration node,
			ITypeBinding clazzBinding) {
		
		String local1="ReturnType";
		String local2="MethodBody";
		String local3="ParameterList";
		if (clazzBinding != null) {
			Clazz clazz = new Clazz();
			getClassBasicInfo(node, clazzBinding,clazz);
			
			// Attributes			
			clazz.setName(bbrc.getName(clazzBinding));
			clazz.setTempName(bbrc.getName(clazzBinding));
			clazz.setCommitID(commitID);
			clazz.setReleaseName(releaseName);
			clazz.setFileName(file.getFileName());
			clazz.setSourcePath(file.getSourcePath());
			if (node != null) {
				// Handle working directory
				clazz.setFile(file);
				clazz.setStart(cu.getLineNumber(node.getStartPosition()));
				clazz.setEnd(cu.getLineNumber(node.getStartPosition()
						+ node.getLength()));
				clazz.setLoc(clazz.getEnd()-clazz.getStart());
			}
			// SuperClass
			ITypeBinding superClassName = cbr.getSuperClass(clazzBinding);
			if (superClassName != null) {
				if (!superClassName.getName().equals("Object")) {
					clazz.addSuperClassName(cbr
							.getQualifiedName(superClassName));
					//System.out.println(cbr.getSuperClass(superClassName));
					insertClazzEdge(clazz.getName(),cbr.getQualifiedName(superClassName),"Inheritance");
				}
			}
			
			
			// elements
			IVariableBinding[] elements = bbrc.getElementTypes(clazzBinding);
			if (elements.length > 0) {
				for (int i = 0; i < elements.length; i++) {
					if (elements[i] != null)
						if (bbrc.getQualifiedType(elements[i])
								.isParameterizedType()) {
							if (bbrc.getQualifiedType(elements[i])
									.isParameterizedType()) {
								clazz.addElement(
										bbrc.getQualifiedType(elements[i])
												.getQualifiedName(), bbrc
												.getQualifiedName(elements[i]));
							}
						}
					
				}
			}

			// methods
			IMethodBinding[] methodBindings = clazzBinding.getDeclaredMethods();
			Method method = new Method();
			if (methodBindings.length > 0) {
				for (int i = 0; i < methodBindings.length; i++) {
					if (methodBindings[i] != null)
						method = createMethodFromBinding(methodBindings[i]);
					clazz.addMethod(method);

				}
			}

			// Package
			if (clazz != null) {
				IPackageBinding pkg = bbrc.getPackage(clazzBinding);
				
				if (pkg != null)
				
					clazz.setPkg(bbrc.getName(pkg));
					String temp=clazz.getSourcePath().replace("\\", ".");
					clazz.setAllPath(temp);//src.test.java.com.google.testing.compile
					clazz.setSourcePath(temp.substring(0, temp.length()-clazz.getPkg().length()-1));//com.google.testing.compile
					
			}
			
			
			// 类之间的关系
			for(int i=0;i<clazz.getMethods().size();i++){
				Method m=clazz.getMethods().get(i);
				String rt=clazz.getMethods().get(i).getReturnType();
				
				for (int n = 0; n < clazz.getFile().getImports().size(); n++) {
					if (clazz.getFile().getImports().get(n).contains(rt)) {
						if (rt.contains("<")) {
							while (rt.contains("<")) {
								String a = rt.substring(rt.indexOf("<", 1));
								String c = rt.substring(0, rt.indexOf("<", 1));
								clazz.setDependencyRelationship(m, local1, c);
		
								String b = a.substring(1, a.length() - 1);
								rt = b;
								insertClazzEdge(clazz.getName(),c,"Dependency");
							}
						} else {
							clazz.setDependencyRelationship(m, local1, rt);
							insertClazzEdge(clazz.getName(),rt,"Dependency");

						}

					}
				}
			}
			if (clazz.getElements().size() == 0) {
				for (int i = 0; i < clazz.getMethods().size(); i++) {
					for (int j = 0; j < clazz.getMethods().get(i)
							.getParameters().size(); j++) {
						String formal = clazz.getMethods().get(i)
								.getParameters().iterator().next();
						Method m=clazz.getMethods().get(i);
						for (int n = 0; n < clazz.getFile().getImports().size(); n++) {
						if(clazz.getFile().getImports().get(n).contains(formal)){
						if(formal.contains("<")){
						while (formal.contains("<")) {
							String a = formal.substring(formal.indexOf("<", 1));
							String c=formal.substring(0, formal.indexOf("<", 1));
							clazz.setDependencyRelationship(m, local2, formal,c);
							insertClazzEdge(clazz.getName(),formal,"Dependency");
							String b = a.substring(1, a.length() - 1);
							formal = b;
						}}else{
							clazz.setDependencyRelationship(m, local2, formal);	
							insertClazzEdge(clazz.getName(),formal,"Dependency");
						}
						
						}
					}
		
					}
				}
			} else {
				for (int i = 0; i < clazz.getMethods().size(); i++) {
					for (int j = 0; j < clazz.getMethods().get(i)
							.getParameters().size(); j++) {
						if (clazz.getElements().containsKey(
								clazz.getMethods().get(i).getParameters()
										.get(j))) {
							String formal2 = clazz.getMethods().get(i).getParameters()
									.get(j);
							Method m=clazz.getMethods().get(i);
							for (int n = 0; n < clazz.getFile().getImports()
									.size(); n++) {
								if (clazz.getFile().getImports().get(n)
										.contains(formal2)) {
									if (formal2.contains("<")) {
										while (formal2.contains("<")) {
											String a = formal2
													.substring(formal2.indexOf(
															"<", 1));
											String c = formal2.substring(0,
													formal2.indexOf("<", 1));
											clazz.setDependencyRelationship(m,
													local2, formal2, c);
											insertClazzEdge(clazz.getName(),formal2,"Dependency");
											String b = a.substring(1,
													a.length() - 1);
											formal2 = b;
										}
									} else {
										if (!clazz.getDependencyRelationship()
												.getDependencyClassName()
												.contains(formal2)) {
											clazz.getDependencyRelationship()
													.addDepandMultiplicity(
															formal2);
										}
										clazz.setDependencyRelationship(m,
												local2, formal2);
										insertClazzEdge(clazz.getName(),formal2,"Dependency");
										clazz.getDependencyRelationship()
												.addDependencyClassName(formal2);
									}
								}
							}
						} else {
							Iterator it = clazz.getElements().keySet().iterator();
							while (it.hasNext()) {
								String ic=(String) it.next();
								String elementName=clazz.getElements().get(ic);
								if (!clazz.getDependencyRelationship().getDependencyClassName().contains(ic)) {
									for (int n = 0; n < clazz.getFile()
											.getImports().size(); n++) {
										if (clazz.getFile().getImports().get(n)
												.contains(ic)) {
											if (ic.contains("<")) {
												while (ic.contains("<")) {
													String a = ic.substring(ic
															.indexOf("<", 1));
													String c = ic.substring(0,
															ic.indexOf("<", 1));
													clazz.setIncidenceRelationship(
															"localLine",
															elementName, c);//ic
													insertClazzEdge(clazz.getName(),c,"Incidence");
													String b = a.substring(1,a.length() - 1);
													ic = b;
												}
											} else {
												clazz.setIncidenceRelationship(
														"localLine",
														elementName, ic);
												insertClazzEdge(clazz.getName(),ic,"Incidence");
											}
										}
								}
						
								}
	
							}

						}
					}
				}

			}
			
			return clazz;
		} else
			return null;
	}

	private Method createMethodFromBinding(IMethodBinding methodBinding) {
		if (methodBinding != null) {
			Method method = new Method();
			method.setName(bbrc.getMethodName(methodBinding));
			
			method.setReturnType(bbrc.getReturnType(methodBinding)
					.getQualifiedName());
			// Parameters
			ITypeBinding[] parameters = bbrc
					.getMethodParameterTypes(methodBinding);
			if (parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					if (parameters[i] != null)
						method.addParameter(bbrc
								.getMethodQualifiedName(parameters[i]));
				}
			}

			return method;
		} else
			return null;
	}

/*	private void addSuperClassInfo(ITypeBinding classBinding) {
		ITypeBinding superClass = cbr.getSuperClass(classBinding);
		if (superClass != null) {
			System.out.println(superClass);
			if(!superClass.equals("java.lang.Object")){
			clazz.setSuperClassName(cbr.getQualifiedName(superClass));
			System.out.println("add super class info");}
		}
	}*/
	
	/*private void isOverride(Clazz clazz) {
		clazz.getSuperClassName().listIterator().getClass()
		for(int i=0;i<clazz.getMethods().size();i++){
			if(clazz.getMethods().get(i).isOverride())
				clazz.getClass().getSuperclass().getDeclaredMethod(clazz.getMethods().get(i).getName(), clazz.getMethods().get(i).getAttributes().get(0).getAttrType());
				clazz.getMethods().get(i).;
		}
	}*/
	private void isOverride(Clazz clazz) {
		java.lang.reflect.Method[] cm = clazz.getClass().getDeclaredMethods();
		java.lang.reflect.Method[] scm = clazz.getClass().getSuperclass().getDeclaredMethods();
		for (java.lang.reflect.Method bm : cm) {
			for (java.lang.reflect.Method am : scm) {
				if (bm.getName().equals(am.getName())
						&& bm.getReturnType().equals(am.getReturnType())) {
					Class[] bc = bm.getParameterTypes();
					Class[] ac = am.getParameterTypes();
					if (bc.length == ac.length) {
						boolean isEqual = true;
						for (int i = 0; i < bc.length; i++) {
							if (!bc[i].equals(ac[i])) {
								isEqual = false;
								break;
							}
						}
						if (isEqual) {
							/*System.out.println(clazz.getClass()
									+ " has method " + bm.getName()
									+ " override "
									+ clazz.getClass().getSuperclass());*/
						}
					}
				}
			}
		}
		
	}
	
	private void addDeclaredMethods(ITypeBinding classBinding,Clazz clazz) {
		ArrayList<Method> methods = new ArrayList<Method>();
		IMethodBinding[] methodBindings = cbr.getDeclaredMethods(classBinding);
		boolean[] isOverride=bbrc.getOverrideMethod(classBinding);
		if (methods != null) {
			Method method = new Method();
			for (int i = 0; i < methodBindings.length; i++) {
				IMethodBinding methodBinding = methodBindings[i];
				if (methodBinding != null) {
					String methodName = bbrc.getName(methodBinding);
					ITypeBinding rtnTypeBinding = bbrc
							.getDeclaringClass(methodBinding);
					String rtnType = cbr.getQualifiedName(rtnTypeBinding);
					method.setName(methodName);
					method.setReturnType(rtnType);
					method.setClazz(clazz.getName());
					method.setOverride(isOverride[i]);
					// method.setFile(file.getDistinctName());

					ITypeBinding[] paramTypeBindings = bbrc
							.getParameterTypes(methodBinding);
					for (ITypeBinding paramTypeBinding : paramTypeBindings) {
						if (paramTypeBinding.isParameterizedType()) {
							String paramType = cbr
									.getQualifiedName(paramTypeBinding);
							method.addParameter(paramType);
						}
					}
					// 此处应从CallGraph中去找到已存在的method对象
				/*	Map.Entry<String, Method> result = cg.findExistingMethod(method);
					if (result == null) {
						// 找不到的情况1：缺省的构造函数，在代码中并未显式出现，但class visitor搜索时可以发现
						// 找不到的情况2：inner的类和方法，method visitor将其忽略掉了
						clazz.addMethod(method);
					} else {
						Method existMethod = result.getValue();
						existMethod.setReturnType(method.getReturnType());
						clazz.addMethod(existMethod);
					}*/
				}
			}
		}
	}
	/**
	 * This function overrides what to do when we reach
	 * the end of a method declaration.
	 */
	@Override
	public void endVisit(TypeDeclaration node) {
		//当访问一个类结束后，对其进行属性的行号设置等
		if(!clazzStack.isEmpty()) {
			Clazz jc = clazzStack.pop();
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
	
	// 解析类的属性，重点关心其所在行号
	public boolean visit(FieldDeclaration node,Clazz clazz) {
		List<VariableDeclarationFragment> fragments = node.fragments();
		// 每个variableObj代表着该行定义的一个变量，例如：int a,b,c，则会有三个变量
		for (VariableDeclarationFragment variableObj : fragments) {

			if (variableObj instanceof VariableDeclarationFragment) {

				String attrName = ((VariableDeclarationFragment) variableObj)
						.getName().toString();

				int startLine = cu.getLineNumber(node.getStartPosition());
				int endLine = cu.getLineNumber(node.getStartPosition()
						+ node.getLength());
				// System.out.println("wodeclass"+cu.getClass().toString());
				Attribute att = new Attribute(attrName, cu.getClass()
						.toString(), startLine, endLine);

				this.attributes.add(att);
				clazz.addAttribute(att);
				//clazz.setAttributes(this.attributes);

			}
		}
		
			
	
		/*List<Method> method = clazz.getMethods();
		// 每个variableObj代表着该行定义的一个变量，例如：int a,b,c，则会有三个变量
		for (VariableDeclarationFragment variableObj : fragments) {

			if (variableObj instanceof VariableDeclarationFragment) {
				String attrName = ((VariableDeclarationFragment) variableObj)
						.getName().toString();
				int startLine = cu.getLineNumber(node.getStartPosition());
				int endLine = cu.getLineNumber(node.getStartPosition()
						+ node.getLength());

				Attribute att = new Attribute(attrName, "", startLine, endLine);
				this.attributes.add(att);
				for (int j = 0; j < method.size(); j++) {
					System.out
							.println((method.get(j).getName())
									.equals(variableObj.resolveBinding()
											.getDeclaringMethod().getName()
											.toString()));
					if ((method.get(j).getName()).equals(variableObj
							.resolveBinding().getDeclaringMethod().getName()
							.toString())) {
						method.get(j).addAttribute(att);
					}
				}

			}
		}*/
		return super.visit(node);
	}
	
	private void insertClazzEdge(String startP,String endp,String type){
		ClazzEdge clazzEdge=new ClazzEdge();
		clazzEdge.setCommitID(commitID);
		clazzEdge.setReleaseName(releaseName);
		clazzEdge.setStartpoint(startP);
		clazzEdge.setEndpoint(endp);
		clazzEdge.setType(type);
		String temp=commitID+","+releaseName+","+startP+","+endp+","+type;
		if(!clazzEdges.containsKey(temp)){
			clazzEdge.setNum(1);
			clazzEdges.put(temp, clazzEdge);
        }else{
        	ClazzEdge clazzEdge2=clazzEdges.get(temp);
        	clazzEdge.setNum(clazzEdge2.getNum()+1);
        	clazzEdges.put(temp, clazzEdge);
        }
	}
	
	private void getClassBasicInfo(TypeDeclaration node, ITypeBinding classBinding,Clazz clazz) {
		String className = cbr.getQualifiedName(classBinding);
		clazz.setName(className);
		clazz.setFile(this.file);
		if(node.toString().split("\n")[0].contains(" abstract "))
			clazz.setAbstract(true);
		String pkg = classBinding.getPackage().getName();
		
		//Module module = new Module(pkg, "");
		//clazz.setPackage (module);
		clazz.setPkg(pkg);
		int startLine = cu.getLineNumber(node.getStartPosition());
		//下面这行代码无法得到正确的结束行号，不少类返回 -1，故在最后加了一个-1
		int endLine = cu.getLineNumber(node.getStartPosition()+node.getLength()-1);
		//采用下面这种方式，不再考虑空行，故与实际对应不上。
		//int endLine =  startLine + node.toString().split("\n").length;
		
		clazz.setStart(startLine);
		clazz.setEnd(endLine);
	}
	 
}