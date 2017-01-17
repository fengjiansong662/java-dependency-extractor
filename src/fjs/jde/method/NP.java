package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

/**
 * NP(C)= N * (N – 1)/2，N是类中方法的数量
 * @author jiansong feng
 *
 */

public class NP {
	public static void GetNC(CallClazzGraph cg,String commitID){
		File file = new File("E:/NP.txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(file, true);
			for(int i=0;i<cg.getClasses().size();i++){
				int n=cg.getClasses().get(i).getMethods().size();
				double np=n*(n-1)/2;
				fw.write(commitID+":Np("+cg.getClasses().get(i)+")=:"+np+"\r\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
