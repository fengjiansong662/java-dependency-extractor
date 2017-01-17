package fjs.jde.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import as.jde.scm.git.GitController;

import fjs.jde.ast.ASTClazzVisitor;
import fjs.jde.model.Clazz;
import fjs.jde.model.Package;
import fjs.jde.model.PackageEdge;
import fjs.jde.model.ReleaseCommit;
import fjs.jde.sql.SQLMethod;

public class P2p {
	public static Map<String,Map<String,Double>>pkgP2ps=new HashMap<String, Map<String,Double>>();//按照commitID，将每个版本中所有包的P2P值存储起来

	public static void GetPacakgeEdgeOfP2p(ReleaseCommit beforeRelease,ReleaseCommit afterRelease){
		
		//Map<String,Double> p2ps=new HashMap<String, Double>();
		List<PackageEdge> beforePkgEdges=new ArrayList<PackageEdge>();
		List<PackageEdge> afterPkgEdges=new ArrayList<PackageEdge>();
		beforePkgEdges=SQLMethod.queryPackageEdge(beforeRelease.getReleaseName());
		afterPkgEdges=SQLMethod.queryPackageEdge(afterRelease.getReleaseName());
		if(beforePkgEdges!=null){
			for(int i=0;i<afterPkgEdges.size();i++){
				boolean type=false;
				for(int j=0;j<beforePkgEdges.size();j++){
					if(beforePkgEdges.get(j).getStartP().equals(afterPkgEdges.get(i).getStartP())&&beforePkgEdges.get(j).getEndP().equals(afterPkgEdges.get(i).getEndP())){
							double p2pValue=(afterPkgEdges.get(i).getNum()-beforePkgEdges.get(j).getNum())/beforePkgEdges.get(j).getNum();//计算边的变化率
							SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), p2pValue);
							type=true;
					}
				}
				if(type==false){
					SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), 1.0);
				}
		}		
		//pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
	}else{
		for(int i=0;i<afterPkgEdges.size();i++){
			SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), 1.0);
		}
	}
}
	public static Map<String, Double> GetP2p(ReleaseCommit beforeRelease,ReleaseCommit afterRelease){
		//List<String> packages=new ArrayList<String>();
		Map<String,Double> p2ps=new HashMap<String, Double>();
		List<Package> beforePkgs=new ArrayList<Package>();
		List<Package> afterPkgs=new ArrayList<Package>();
		beforePkgs=SQLMethod.queryPackage(beforeRelease.getReleaseName());
		afterPkgs=SQLMethod.queryPackage(afterRelease.getReleaseName());
		if(beforePkgs!=null){
			for(int i=0;i<afterPkgs.size();i++){
				if(beforePkgs.contains(afterPkgs.get(i))!=false){
					int count=0;
					List<Clazz>beforeClazzs=new ArrayList<Clazz>();
					List<Clazz>afterClazzs=new ArrayList<Clazz>();
					beforeClazzs=SQLMethod.queryClazzOfPackage(beforeRelease.getReleaseName(), afterPkgs.get(i).getPath());
					afterClazzs=SQLMethod.queryClazzOfPackage(afterRelease.getReleaseName(), afterPkgs.get(i).getPath());
					int maxSize=0;
					if(beforeClazzs.size()>afterClazzs.size()){
						maxSize=beforeClazzs.size();
					}else{
						maxSize=afterClazzs.size();
					}
					for(int j=0;j<afterClazzs.size();j++){
							//判断两个类是否发生变化，如果没有发生变化，count++
							for(int p=0;p<beforeClazzs.size();p++){
								if(beforeClazzs.get(p).getName().equals(afterClazzs.get(j).getName())){
									if(beforeClazzs.get(p).getLoc()==afterClazzs.get(j).getLoc()){
										count++;
									}
								}
							}

					}
					double p2pValue=count/maxSize;
					System.out.println("P2P:"+afterPkgs.get(i).getPath()+"1 "+p2pValue);
					p2ps.put(afterPkgs.get(i).getPath(), p2pValue);
					SQLMethod.updatePackage(afterRelease.getReleaseName(), afterPkgs.get(i).getPath(), p2pValue);
				}else{
					System.out.println("P2P:"+afterPkgs.get(i).getPath()+"2 "+ 0.0);
					p2ps.put(afterPkgs.get(i).getPath(), 0.0);
					SQLMethod.updatePackage(afterRelease.getReleaseName(), afterPkgs.get(i).getPath(), 0.0);
				}
			}
			
		}else{
			for(int i=0;i<afterPkgs.size();i++){
				System.out.println("P2P:"+afterPkgs.get(i).getPath()+"3 "+ 0.0);
				p2ps.put(afterPkgs.get(i).getPath(), 0.0);
				SQLMethod.updatePackage(afterRelease.getReleaseName(), afterPkgs.get(i).getPath(), 0.0);
			}
		}
		pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
		return p2ps;
	}
	
	public static void printP2p(GitController gitController){
		GitController fGit = gitController;
		List<ReleaseCommit> commitIDS=fGit.getReleaseCommits();
		for (int i = 0; i < commitIDS.size()-1; i++) {//commitIDS.size()
			GetP2p(commitIDS.get(i-1),commitIDS.get(i));
			GetPacakgeEdgeOfP2p(commitIDS.get(i-1),commitIDS.get(i));
		}
	}
	
}
