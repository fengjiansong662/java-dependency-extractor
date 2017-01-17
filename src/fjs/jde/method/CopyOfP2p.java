package fjs.jde.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fjs.jde.ast.ASTClazzVisitor;
import fjs.jde.model.JavaFile;
import fjs.jde.model.Package;

public class CopyOfP2p {
	public static Map<String,Map<String,Double>>pkgP2ps=new HashMap<String, Map<String,Double>>();//按照commitID，将每个版本中所有包的P2P值存储起来
	public static Map<String,Package> oldpkg=new HashMap<String, Package>();//存储上个版本中的包
	public static Map<String,List<String>> oldPkgs=new HashMap<String,List<String>>();
	public static Map<String,Map<String,Set<JavaFile>>> pcfCollection=new HashMap<String,Map<String,Set<JavaFile>>>();
	public static Map<String,Double> GetP2ps(String beforeCommitID,String afterCommitID){
	
		Map<String,Double> p2ps=new HashMap<String, Double>();
		Map<String,Set<JavaFile>> beforepcf=pcfCollection.get(beforeCommitID);
		Map<String,Set<JavaFile>> afterpcf=pcfCollection.get(afterCommitID);
		Iterator it =afterpcf.entrySet().iterator();
		Map<String,Double> pkgMaxSize=new HashMap<String, Double>();
		while(it.hasNext()){
					Map.Entry<String,Set<JavaFile>> entry=(Map.Entry<String, Set<JavaFile>>)it.next();
					String key=entry.getKey();
					double p2p=0.0;
					if(beforeCommitID!=null){
					Set<JavaFile> afterFiles=entry.getValue();
					Set<JavaFile> beforeFiles=beforepcf.get(key);
					Iterator<JavaFile> afterFile = afterFiles.iterator();  
					double maxSize=0.0;
					Set<JavaFile> commonFile=new HashSet<JavaFile>();
					while (afterFile.hasNext()) {  
					  JavaFile afterF = afterFile.next();  
					  Iterator<JavaFile> beforeFile = beforeFiles.iterator();
					  while(beforeFile.hasNext()){
						  JavaFile beforeF = beforeFile.next(); 
						  if(beforeF.getFileName().equals(afterF.getFileName())){
							  System.out.println(beforeF.getFileName()+"  "+afterF.getFileName());
							  if(beforeF.getLength()==afterF.getLength()){
								  commonFile.add(afterF);
								  break;
							  }
						  }
					  }
					} 
					if(afterFiles.size()>=beforeFiles.size()){
						maxSize=afterFiles.size();
						
					}else{
						maxSize=beforeFiles.size();
					}
					pkgMaxSize.put( key, maxSize);
					p2p=commonFile.size()/maxSize;
					System.out.println("包："+key+" maxsize:"+maxSize+" commonFileSize:"+commonFile.size()+" p2p:"+p2p);
					p2ps.put(key, p2p);
					}else{
						p2ps.put(key, p2p);
					}
		}
		pkgP2ps.put(afterCommitID, p2ps);
		
		return p2ps;
	}
	public static Map<String, Double> GetP2p(String commitID,List<Package> list){
		//List<String> packages=new ArrayList<String>();
		Map<String,Double> p2ps=new HashMap<String, Double>();
		if(oldpkg!=null){
			for(int i=0;i<list.size();i++){
				if(oldpkg.get(list.get(i).getPathName())!=null){
					int count=0;
					Package oldP=oldpkg.get(list.get(i).getPathName());
					int maxSize=0;
					if(oldP.getSize()>list.get(i).getSize()){
						maxSize=oldP.getSize();
					}else{
						maxSize=list.get(i).getSize();
					}
					for(int j=0;j<list.get(i).getClazzs().size();j++){
						if(oldP.getClazzs().contains(list.get(i).getClazzs().get(j))==true){
							//判断两个类是否发生变化，如果没有发生变化，count++
							if(!list.get(i).getClazzs().get(j).wasModified()){
								count++;
							}
						}
					}
					double p2pValue=count/maxSize;
					System.out.println("P2P:"+list.get(i).getPathName()+"1 "+p2pValue);
					p2ps.put(list.get(i).getPathName(), p2pValue);
				}else{
					System.out.println("P2P:"+list.get(i).getPathName()+"2 "+ 0.0);
					p2ps.put(list.get(i).getPathName(), 0.0);
				}
			}
			
		}else{
			for(int i=0;i<list.size();i++){
				System.out.println("P2P:"+list.get(i).getPathName()+"3 "+ 0.0);
				p2ps.put(list.get(i).getPathName(), 0.0);
			}
		}
		oldpkg=null;
		oldpkg=new HashMap<String, Package>();
		for(int i=0;i<list.size();i++){
			oldpkg.put(list.get(i).getPathName(), list.get(i));
		}
		pkgP2ps.put(commitID, p2ps);
		return p2ps;
	}
}
