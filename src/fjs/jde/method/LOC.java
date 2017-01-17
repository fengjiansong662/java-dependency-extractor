package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

/**
 * the lines of code of the class (LOC), 
 * excluding blanks and comment lines
 * @author jiansong feng
 *
 */

public class LOC {
	public static void GetLOC(CallClazzGraph cg,String commitID){
		File file = new File("E:/LOC.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			for(int i=0;i<cg.getClasses().size();i++){
				
				int loc=cg.getClasses().get(i).getEnd()-cg.getClasses().get(i).getStart();
				fw.write(commitID+":LOC("+cg.getClasses().get(i)+")=:"+loc+"\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
