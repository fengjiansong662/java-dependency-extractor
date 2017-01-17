package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

public class NumberOfClasses {
	public static void GetNC(CallClazzGraph cg,String commitID){
		File file = new File("E:/NumberOfClasses.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			fw.write(commitID+":NC=:"+cg.getClasses().size()+"\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
