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
import fjs.jde.model.Package;

/**
 * Distance 
 * @author jiansong feng
 *The D metric for a package is defined as the perpendicular distance of the package from the idealized line (A + I = 1), 
 *where A is the percentage of the abstract classes to  the  total  number  of  classes  in  the  package.  
 *This  metric  is  an indicator of the  package's balance between  abstractness  and  stability. 
 *A package squarely on the main sequence is optimally balanced with respect to its abstractness and stability.
 *The  range  for  this  metric  is  zero  to  one,
 *with zero indicating a package that is coincident with the main sequence and one indicating a package that is as far from the main sequence as possible.
 *
 */

public class Distance {
	public static Map <String,ArrayList<Clazz>>pkgsClass=new HashMap<String,ArrayList<Clazz>>();
	public static List<String> pkgs=new ArrayList<String>();
	public static void putAdd(String sr,Map <String,ArrayList<Clazz>> m,Clazz name){
        if(!m.containsKey(sr)){
            m.put(sr, new ArrayList<Clazz>());
            pkgs.add(sr);
        }
        m.get(sr).add(name);
    }
    public static void getDistance(CallClazzGraph cg,Map<String,Integer>ce,Map<String,Integer>ca,List<Package> p){
    	for (int j = 0; j < cg.getClasses().size(); j++) {
			Clazz jj = cg.getClasses().get(j);
			putAdd(jj.getPkg(),pkgsClass,jj);
		}
    	//计算质量指标NC
    	File file = new File("E:/Distance.txt");
    	FileWriter fw = null;
    	try {
    				
    			fw = new FileWriter(file,true);
    			for (int i = 0; i < p.size(); i++) {
    				Integer e = ce.get(p.get(i).getPath());
    				Integer a = ca.get(p.get(i).getPath());
    				if(e != null&&a != null&&(e.intValue()!=0||a.intValue()!=0)){
    					System.out.println(e.doubleValue()+" , "+a.doubleValue());
    					double iVaule = e.doubleValue() /(e.doubleValue() + a.doubleValue());
    					fw.write(cg.getCommit().commitID+":I(" + p.get(i).getPath() + ")="+":"+iVaule+"\r\n");
    				}   			
    	for(int i=0;i<pkgs.size();i++){
    			int abstractnum=0;
    			for(int j=0;j<pkgsClass.get(pkgs.get(i)).size();j++){
    				if(pkgsClass.get(pkgs.get(i)).get(j).isAbstract()==true){
    					abstractnum++;
    				}
    			}
    			double a=abstractnum/pkgsClass.get(pkgs.get(i)).size();
    			//System.out.println(pkgs.get(i).getPath()+":"+pkgs.get(i).getClazzs().size());
    			
    			fw.write(cg.getCommit().commitID+":NC(" + pkgs.get(i) + ")="+":"+a+"\r\n");
    		}
    		fw.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
    	}
    	public static void GetInstability(String commitID,Map<String,Integer>ce,Map<String,Integer>ca,List<Package> p){
    		File file = new File("E:/I.txt");
    		FileWriter fw = null;
    		try {
    			fw = new FileWriter(file, true);
    			for (int i = 0; i < p.size(); i++) {
    				Integer e = ce.get(p.get(i).getPath());
    				Integer a = ca.get(p.get(i).getPath());
    				if(e != null&&a != null&&(e.intValue()!=0||a.intValue()!=0)){
    					System.out.println(e.doubleValue()+" , "+a.doubleValue());
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
