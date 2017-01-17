package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fjs.jde.model.Package;

/**
 * Instability (I) 
 * @author jiansong feng
 *The I metric for a package is defined as the ratio of efferent coupling (Ce) to total coupling (Ce+Ca)for the package;
 *such that I=Ce/(Ce + Ca).  
 *This metric is an indicator of the package's resilience to change. 
 *The range for this  metric is zero to one,
 *with zero indicating a completely stable package and one indicating a completely unstable package.
 */

public class Instability {
	
	public static void GetInstability(String commitID,Map<String,Integer>ce,Map<String,Integer>ca,List<Package> p){
		File file = new File("E:/I.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			for (int i = 0; i < p.size(); i++) {
				Integer e = ce.get(p.get(i).getPath());
				Integer a = ca.get(p.get(i).getPath());
				if(e != null&&a != null&&(e.intValue()!=0||a.intValue()!=0)){
					//System.out.println(e.doubleValue()+" , "+a.doubleValue());
					double iVaule = e.doubleValue() /(e.doubleValue() + a.doubleValue());
					fw.write(commitID+":I(" + p.get(i).getPath() + ")="+":"+iVaule+"\r\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
