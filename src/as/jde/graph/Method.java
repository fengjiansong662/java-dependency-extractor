package as.jde.graph;

import java.util.ArrayList;
import java.util.List;

import fjs.jde.ast.Attribute;

public class Method
{
	private String file;
	private String pkg;
	private String clazz;
	private String name;
	private String returnType;
	private List<String> parameters;
	private List<String> accessedAttributes;
	private List<String> AccessedLocalData;
	private List<Attribute> attributes;
	private List<String> internalParameters;
	private int start = -1;
	private int end = -1;
	private boolean wasEdited = false;
	private boolean isOverride=false;
	
	public boolean isOverride() {
		return isOverride;
	}

	public void setOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}

	public Method() {
		parameters = new ArrayList<String>();
	}

	public Method(String file, String pkg, String clazz, String name, List<String> parameters,List<String> internalParameters, int start, int end, boolean wasEdited) {
		this.file = file;
		this.pkg = pkg;
		this.clazz = clazz;
		this.name = name;
		this.parameters = parameters;
		//this.internalParameters=internalParameters;
		this.start = start;
		this.end = end;
		this.wasEdited = wasEdited;
		attributes=new ArrayList<Attribute>();
	}

	public List<String> getAccessedAttributes() {
		return accessedAttributes;
	}

	public void setAccessedAttributes(List<String> accessedAttributes) {
		this.accessedAttributes = accessedAttributes;
	}

	public List<String> getAccessedLocalData() {
		return AccessedLocalData;
	}

	public void setAccessedLocalData(List<String> accessedLocalData) {
		AccessedLocalData = accessedLocalData;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getPkg() {
		return pkg;
	}

	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getInternalParameters() {
		return internalParameters;
	}
	
	public void addInternalParameter(String param) {
		this.internalParameters.add(param);
	}

	public void setInternalParameters(List<String> internalParameters) {
		this.internalParameters = internalParameters;
	}

	public void addParameter(String param) {
		parameters.add(param);
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
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
		System.out.println(clazz);
		String val = pkg + "." + clazz + "." + name + 
				"(" + parameters.toString() + ")";
		return val;
	}

	public boolean wasModified() {
		return wasEdited;
	}

	public void setWasChanged(boolean b) {
		wasEdited = b;
	}

	public void setReturnType(String rtnType) {
		this.returnType=rtnType;
		
	}

	public String getReturnType() {
		return returnType;
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addAccessedAttributes(String name2) {
		accessedAttributes.add(name2);
		
	}

	public void addAccessedLocalData(String string) {
		AccessedLocalData.add(string);
		
	}

	
}
