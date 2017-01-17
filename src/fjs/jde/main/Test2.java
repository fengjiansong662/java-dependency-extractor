package fjs.jde.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader br;
		Map<String,String> versionName=new HashMap<String,String>();
		try {
			br = new BufferedReader(
					new FileReader("E:\\版本号.txt"));
		
		String line;
		String[] url = null;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				String versions[]=line.split("  ");
				System.out.println(versions[0]+versions[1]);
				versionName.put(versions[0], versions[1]);
				/*String commitIDs[]=line.split("\\', \\'");
				for(int i=0;i<commitIDs.length;i++){
					System.out.println(commitIDs[i]);
				}*/
				/*String a[]= line.split("&#\\*:\\*#&");
				System.out.println(a[0]);
				//System.out.println(a[1]);
				System.out.println(a.length);
				for (int i = 0; i < a.length; i++) {
					url = a[i].split("\\\\");
					System.out.println("a[i]"+a[i]);
					System.out.println("3:" + url[2]);
					System.out.println("4:" + url[3]);
					String firstName[]=url[2].split(" \\(");
					System.out.println("大名："+firstName[0]);
					String lname=" ";
					if(firstName.length>1){
						String lastname[]=firstName[1].split("\\)");
						lname=lastname[0];
						}
					if(lname!=null){
					System.out.println("小名："+lname);}else{
						System.out.println("dddd");
					}
			*/
				//}
			}
			System.out.println(versionName.size());
		}catch (IOException e) {
			e.printStackTrace();
		}

	
	}
}
