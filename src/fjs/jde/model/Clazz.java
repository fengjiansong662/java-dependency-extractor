package fjs.jde.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fjs.jde.ast.Attribute;
import fjs.jde.ast.Module;
import fjs.jde.relationship.DependencyRelationship;
import fjs.jde.relationship.IncidenceRelationship;

import as.jde.graph.Method;

public class Clazz
{
	//private String id;
	private Integer id;
	private JavaFile file;
	private String fileName;
	private String sourcePath;//包以上的物理路径
	private String allPath;//总物理路径
	public String getSourcePath() {
		return sourcePath;
	}
	public String getAllPath() {
		return allPath;
	}
	public void setAllPath(String allPath) {
		this.allPath = allPath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	private String commitID;
	private String releaseName;
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getCommitID() {
		return commitID;
	}
	public void setCommitID(String commitID) {
		this.commitID = commitID;
	}
	private String pkg;
	private List<Method> methods;
	private List<String> superClassName;
	private String name;
	private Map<String,String> elements;
	private int start = -1;
	private int end = -1;
	private int loc=0;
	public int getLoc() {
		return loc;
	}
	
	public void setLoc(int loc) {
		this.loc = loc;
	}
	private boolean wasEdited = false;
	private DependencyRelationship dependencyRelationship;
	private IncidenceRelationship incidenceRelationship;
	private List<Attribute> attributes;
	private boolean Abstract;
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttributes(Attribute attribute){
		this.attributes.add(attribute);
	}
	public Clazz() {
		attributes=new ArrayList<Attribute>();
		methods=new ArrayList<Method>();
		elements = new HashMap<String,String>(); 
		superClassName=new ArrayList<String>();
		dependencyRelationship=new DependencyRelationship();
		incidenceRelationship=new IncidenceRelationship();
		attributes=new ArrayList<Attribute>();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public IncidenceRelationship getIncidenceRelationship() {
		return incidenceRelationship;
	}
	public void setClass1(String clazz){
		this.dependencyRelationship.setClazz1(clazz);
		this.incidenceRelationship.setClazz1(clazz);
	}
	public void setIncidenceRelationship(String localLine,String element,String incidenceClassName) {
		this.incidenceRelationship.addElement(element);
		this.incidenceRelationship.addIncidenceClassName(incidenceClassName);
		if (this.incidenceRelationship.getClazz2().size() != 0) {
			for (int i = 0; i < this.incidenceRelationship.getClazz2().size(); i++) {
				if(!this.incidenceRelationship.getClazz2().get(i).equals(incidenceClassName)){
					this.incidenceRelationship.addIncidenceMultiplicity(incidenceClassName);
				}
			}
		}else{
			this.incidenceRelationship.addIncidenceMultiplicity(incidenceClassName);
		}
		this.incidenceRelationship.addLocalLine(localLine);
	}

	public Clazz(JavaFile file, String pkg, List<Method> methods, String name, Map<String,String> elements, int start, int end, boolean wasEdited,ArrayList<String> superClassName) {
		this.file = file;
		this.pkg = pkg;
		this.methods = methods;
		this.name = name;
		this.elements = elements;
		this.start = start;
		this.end = end;
		this.wasEdited = wasEdited;
		this.superClassName=superClassName;
	}
	
	public DependencyRelationship getDependencyRelationship() {
		return dependencyRelationship;
	}
	public void setDependencyRelationship( Method method,String local,String dependencyClassName) {
		this.dependencyRelationship.addLocal(local);
		this.dependencyRelationship.addRelationP(method, dependencyClassName);
		this.dependencyRelationship.addDependencyClassName(dependencyClassName);
		if (this.dependencyRelationship.getClazz2().size() != 0) {
			for (int i = 0; i < this.dependencyRelationship.getClazz2().size(); i++) {
				if(!this.dependencyRelationship.getClazz2().get(i).equals(dependencyClassName)){
					this.dependencyRelationship.addDepandMultiplicity(dependencyClassName);
				}
			}
		}else{
			this.dependencyRelationship.addDepandMultiplicity(dependencyClassName);
		}
		
	}
	public void setDependencyRelationship( Method method,String local,String dependencyClassName,String c) {
		this.dependencyRelationship.addLocal(local);
		this.dependencyRelationship.addRelationP(method, dependencyClassName);
		this.dependencyRelationship.addDependencyClassName(c);
		if (this.dependencyRelationship.getClazz2().size() != 0) {
			for (int i = 0; i < this.dependencyRelationship.getClazz2().size(); i++) {
				if(!this.dependencyRelationship.getClazz2().get(i).equals(c)){
					this.dependencyRelationship.addDepandMultiplicity(c);
				}
			}
		}else{
			this.dependencyRelationship.addDepandMultiplicity(c);
		}
	}
	
	public JavaFile getFile() {
		return file;
	}

	public void setFile(JavaFile file) {
		this.file = file;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public List<Method> getMethods() {
		return methods;
	}

	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	
	public void addMethod(Method method) {
		methods.add(method);
	}
	
	public String getName() {
		return name;
	}

	/*public Map<Clazz, String> getParameters() {
		return Parameters;
	}

	public void setParameters(Map<Clazz, String> parameters) {
		Parameters = parameters;
	}
	
	public void addParameter(Clazz clazz,String param) {
		Parameters.put(clazz, param);
	}*/
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*public void addMethodsNames(String methodsNames) {
		elements.add(methodsNames);
	}*/
	/*public List<String> getMethodsNames() {
		return methodsNames;
	}*/
	public void addElement(String classType,String param) {
		elements.put(classType, param);
	}
	public Map<String, String> getElements() {
		return elements;
	}
	public void setElements(Map<String, String> elements) {
		this.elements = elements;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	@Override
	public String toString() {
		/*String val = pkg + "." + clazz + "." + name + 
				"(" + parameters.toString() + ")";*/
		String val =pkg+ "."+ name;
		//System.out.println(val);
		return val;
	}

	public boolean wasModified() {
		return wasEdited;
	}

	public void setWasChanged(boolean b) {
		wasEdited = b;
	}

	public void addSuperClassName(String qualifiedName) {
		this.superClassName.add(qualifiedName);
	}
	
	public void setSuperClassName(String qualifiedName) {
		this.superClassName.add(qualifiedName);
		
	}

	public List<String> getSuperClassName() {
		return superClassName;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}

	public void setAbstract(boolean b) {
		this.Abstract=b;
		
	}

	public boolean isAbstract() {
		return Abstract;
	}
	/*public void setPackage(String pkg) {
		this.pkg=pkg;
	}*/
	
	/*public void addIncidenceClassName(String incidenceClassName) {
		this.incidenceClassName.add(incidenceClassName);
	}
	public ArrayList<String> getIncidenceClassName() {
		return incidenceClassName;
	}

	public void setIncidenceClassName(ArrayList<String> incidenceClassName) {
		this.incidenceClassName = incidenceClassName;
	}*/
}
