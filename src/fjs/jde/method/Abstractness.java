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
 * Abstractness(A)
 * @author jiansong feng
 *A is the percentage of the abstract classes to  the  total  number  of  classes  in  the  package. 
 */

public class Abstractness {
	public static Map <String,ArrayList<Clazz>>pkgsClass=new HashMap<String,ArrayList<Clazz>>();
	public static List<String> pkgs=new ArrayList<String>();
	public static void putAdd(String sr,Map <String,ArrayList<Clazz>> m,Clazz name){
        if(!m.containsKey(sr)){
            m.put(sr, new ArrayList<Clazz>());
            pkgs.add(sr);
        }
        m.get(sr).add(name);
    }
    public static void getA(CallClazzGraph cg){
    	for (int j = 0; j < cg.getClasses().size(); j++) {
			Clazz jj = cg.getClasses().get(j);
			putAdd(jj.getPkg(),pkgsClass,jj);
		}
    	//计算质量指标NC
    	File file = new File("E:/A.txt");
    	FileWriter fw = null;
    	try {
    				
    			fw = new FileWriter(file,true);
    			
    	for(int i=0;i<pkgs.size();i++){
    			int abstractnum=0;
    			for(int j=0;j<pkgsClass.get(pkgs.get(i)).size();j++){
    				if(pkgsClass.get(pkgs.get(i)).get(j).isAbstract()==true){
    					abstractnum++;
    				}
    			}
    			double a=abstractnum/pkgsClass.get(pkgs.get(i)).size();

    			fw.write(cg.getCommit().commitID+":NC(" + pkgs.get(i) + ")="+":"+a+"\r\n");
    		}
    		fw.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    }
}
