package fjs.jde.model;

import java.util.HashSet;
import java.util.Set;

/**
 * ���ڿ�ԴԴ�룬�����¿����ж��Դ���ļ��У���ÿ��Դ���ļ��б���Ϊһ��ģ��
 * @author fengjiansong
 *
 */

public class Module {
	private Integer id;
	private String allPath;//ģ�������·������src
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
