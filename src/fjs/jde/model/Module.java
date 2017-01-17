package fjs.jde.model;

import java.util.HashSet;
import java.util.Set;

/**
 * 由于开源源码，工程下可能有多个源码文件夹，而每个源码文件夹被视为一个模块
 * @author fengjiansong
 *
 */

public class Module {
	private Integer id;
	private String allPath;//模块的物理路径，到src
	private Set<String> packageName;
	
	public Module() {
		this.packageName = new HashSet<String>();
	}

	public Set<String> getPackageName() {
		return packageName;
	}

	public void addPackageName(String packageName) {
		this.packageName.add(packageName);
	}

	public String getAllPath() {
		return allPath;
	}

	public void setAllPath(String allPath) {
		this.allPath = allPath;
	}
}
