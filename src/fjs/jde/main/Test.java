package fjs.jde.main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String []s=new String[];
			try {
			
				BufferedReader br  = new BufferedReader(new FileReader("F:\\java MyEclipse10.7\\Workspaces\\MyEclipse 10\\feng\\result.txt"));
				String line;
				while((line=br.readLine())!=null)
				{
					if(!line.contains("filename:./result.txt")){
					//String []s=line.split("&#\\*:\\*#&");
					String []s=line.split("	");
					System.out.println(s[0]);
					System.out.println(s[1]);
					System.out.println(s[2]);}
					//System.out.println(url[0].substring(2));
					//System.out.println(url[1]);

					/*System.out.println("名："+s[0]);
					String firstName[]=s[0].split(" \\(");
					System.out.println("大名："+firstName[0]);
					String lname=null;
					if(firstName.length>1){
						String lastname[]=firstName[1].split("\\)");
						lname=lastname[0];
						}
					
					if(lastname[0]==null){
						lastname[0]=null;
					}
					if(lname!=null){
					System.out.println("小名："+lname);}else{
						System.out.println("dddd");
					}*/
					//System.out.println("repo"+s[1]);
					//String repo[]=s[1].split(" ");
					//String repo[]=line.split(" "); 
					/*for (int i = 0; i < repo.length; i++) {
						String[] url = repo[i].split("\\\\");
						System.out.println("repo:"+repo[i]);
						System.out.println("1:"+url[0]);
						System.out.println("2:"+url[1]);
						System.out.println("3:"+url[2]);
						//System.out.println("4:"+url[3]);
						//System.out.println("5:"+url[4]);
					}*/
					
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
				
		
	}

}

