package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fjs.jde.graph.CallClazzGraph;
import fjs.jde.model.Clazz;

/**
 * time 2016/7/11
 * @author jiansong feng
 * @The NC metric for a package is defined as the number of concrete 
 * and abstract classes (and interfaces) in the package. 
 * It is a measure of package size.
 */

 
public class NC {
	public static Map <String,ArrayList<String>>pkgsClass=new HashMap<String,ArrayList<String>>();
	public static List<String> pkgs=new ArrayList<String>();
	public static void putAdd(String sr,Map <String,ArrayList<String>> m,String name){
        if(!m.containsKey(sr)){
            m.put(sr, new ArrayList<String>());
            pkgs.add(sr);
        }
        m.get(sr).add(name);
    }
    public static void getNC(CallClazzGraph cg){
    	for (int j = 0; j < cg.getClasses().size(); j++) {
			Clazz jj = cg.getClasses().get(j);
			putAdd(jj.getPkg(),pkgsClass,jj.getName());
		}
    	//计算质量指标NC
    	File file = new File("E:/NC.txt");
    	FileWriter fw = null;
    	try {
    				
    			fw = new FileWriter(file,true);
    			
    	for(int i=0;i<pkgs.size();i++){
    			
    			fw.write(cg.getCommit().commitID+":NC(" + pkgs.get(i) + ")="+":"+pkgsClass.get(pkgs.get(i)).size()+"\r\n");
    		}
    		fw.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    }
}
