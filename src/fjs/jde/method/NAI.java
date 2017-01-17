package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

/**
 * NAI (Number of attributes)The number of attributes in a class (excluding inherited ones). Includes attributes of basic types such as strings, integers..
 * @author jiansong feng
 *
 */

public class NAI {
	public static void GetNAI(CallClazzGraph cg,String commitID){
			File file = new File("E:/NAI.txt");
			FileWriter fw = null;
			try {
				fw = new FileWriter(file, true);
				for(int i=0;i<cg.getClasses().size();i++){
					for(int j=0;j<cg.getClasses().get(i).getMethods().size();j++)
					{
						
						fw.write(commitID+":NAI("+cg.getClasses().get(i)+")=:"+cg.getClasses().get(i).getAttributes().size()+"\r\n");
					}
				}
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	
}
