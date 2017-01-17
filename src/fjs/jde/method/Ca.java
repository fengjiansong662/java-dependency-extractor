package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fjs.jde.model.Package;
/**
 * 
 * @author jiansong feng
 * Afferent Couplings
 * The Ca metric for a package is defined as the number of other packages that depend upon classes within the package. 
 * It measures the incoming dependencies (fan-in).
 *
 */

public class Ca {
	public static Map <String,ArrayList<String>>pkgsDependon=new HashMap<String,ArrayList<String>>();
	public static List<String> pkgs=new ArrayList<String>();
	public static Map<String,Integer>pgksCa=new HashMap<String,Integer>();
	public static void putAdd(String sr,Map <String,ArrayList<String>> m,String name){
        if(!m.containsKey(sr)){
            m.put(sr, new ArrayList<String>());
            pkgs.add(sr);
        }
        m.get(sr).add(name);
    }
	public static void GetCa(String commitID,List<Package> p){
		for(int i=0;i<p.size();i++){
			for(int j=0;j<p.get(i).getDependencyPkgs().size();j++){
				putAdd(p.get(i).getDependencyPkgs().get(j), pkgsDependon, p.get(i).getPath());
			}
		}
		//计算质量指标NC
    	File file = new File("E:/Ca.txt");
    	FileWriter fw = null;
    	try {	
    			fw = new FileWriter(file,true);
    	for(int i=0;i<pkgs.size();i++){
    			pgksCa.put(pkgs.get(i), pkgsDependon.get(pkgs.get(i)).size());
    			fw.write(commitID+":Ca(" + pkgs.get(i) + ")="+":"+pkgsDependon.get(pkgs.get(i)).size()+"\r\n");
    		}
    		fw.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    	}
		
	}
}
