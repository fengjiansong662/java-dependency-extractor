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
 * Efferent Couplings 
 * @author jiansong feng
 *The Ce metric for a package is defined as the number of other packages that the classes in the package depend upon.  
 *It measures the outgoing dependencies (fan-out).  
 */

public class Ce {
	public static List<String> pkgs=new ArrayList<String>();
	public static Map<String,Integer>pgksCe=new HashMap<String,Integer>();
	public static void GetCe(String commitID,List<Package> p){
		File file = new File("E:/Ce.txt");
		FileWriter fw = null;
		try {		
	    	fw = new FileWriter(file,true);
	    for(int i=0;i<p.size();i++){
	    		pgksCe.put(p.get(i).getPath(), p.get(i).getDependencyPkgs().size());
	    		fw.write(commitID+":Ce(" + p.get(i).getPath() + ")="+":"+p.get(i).getDependencyPkgs().size()+"\r\n");
	    	}
	    	fw.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    }
			
		
		
	}
}
