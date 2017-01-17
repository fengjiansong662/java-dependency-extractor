package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

/**
 * number  of  method  parameters ��������������
 * @author jiansong feng
 *
 */

public class NumberOfMethodParameters{
	public static void GetNMP(CallClazzGraph cg,String commitID){
		File file = new File("E:/NumberOfMethodParameters.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			for(int i=0;i<cg.getClasses().size();i++){
				for(int j=0;j<cg.getClasses().get(i).getMethods().size();j++)
				{
					
					fw.write(commitID+":NMP("+cg.getClasses().get(i)+"��"+cg.getClasses().get(i).getMethods().get(j).getName()+")=:"+cg.getClasses().get(i).getMethods().get(j).getParameters().size()+"\r\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
