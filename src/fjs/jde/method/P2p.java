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
import fjs.jde.model.ClazzEdge;
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
		if(beforeRelease!=null){
		beforePkgEdges=SQLMethod.queryPackageEdge(beforeRelease.getReleaseName());
		afterPkgEdges=SQLMethod.queryPackageEdge(afterRelease.getReleaseName());
			for(int i=0;i<afterPkgEdges.size();i++){
				boolean type=false;
				for(int j=0;j<beforePkgEdges.size();j++){
					if(beforePkgEdges.get(j).getStartP().equals(afterPkgEdges.get(i).getStartP())&&beforePkgEdges.get(j).getEndP().equals(afterPkgEdges.get(i).getEndP())){
							double p2pValue=(afterPkgEdges.get(i).getNum()-beforePkgEdges.get(j).getNum())/(double)beforePkgEdges.get(j).getNum();//计算边的变化率
							System.out.println(afterPkgEdges.get(i).getNum());
							System.out.println(beforePkgEdges.get(j).getNum());
							System.out.println(p2pValue);
							SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), p2pValue);
							type=true;
					}
				}
				if(type==false){
					SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), -1.0);
				}
		}		
		//pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
	}else{
		for(int i=0;i<afterPkgEdges.size();i++){
			SQLMethod.updatePackageEdge(afterRelease.getReleaseName(), afterPkgEdges.get(i).getStartP(), afterPkgEdges.get(i).getEndP(), -1.0);
		}
	}
}
	public static void GetClazzP2p(ReleaseCommit beforeRelease,
			ReleaseCommit afterRelease){
		List<Clazz> beforeClazzs=new ArrayList<Clazz>();
		List<Clazz> afterClazzs=new ArrayList<Clazz>();
		if(beforeRelease!=null){
			beforeClazzs=SQLMethod.queryClazz(beforeRelease.getReleaseName());
			afterClazzs=SQLMethod.queryClazz(afterRelease.getReleaseName());
				for(int i=0;i<afterClazzs.size();i++){
					boolean type=false;
					for(int j=0;j<beforeClazzs.size();j++){
						if(beforeClazzs.get(j).getAllPath().equals(afterClazzs.get(i).getAllPath())&&beforeClazzs.get(j).getName().equals(afterClazzs.get(i).getName())){
							double p2pValue;
							if(beforeClazzs.get(j).getLoc()!=0){
								p2pValue=(afterClazzs.get(i).getLoc()-beforeClazzs.get(j).getLoc())/(double)beforeClazzs.get(j).getLoc();//计算类的变化率
								
							}else{
								p2pValue=-1.0;
							}
								
								System.out.println(p2pValue);
								SQLMethod.updateClazz(afterRelease.getReleaseName(), afterClazzs.get(i).getName(), afterClazzs.get(i).getAllPath(), p2pValue);
								type=true;
						}
					}
					if(type==false){
						SQLMethod.updatePackageEdge(afterRelease.getReleaseName(),  afterClazzs.get(i).getName(), afterClazzs.get(i).getAllPath(), -1.0);
					}
			}		
			//pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
		}else{
			for(int i=0;i<afterClazzs.size();i++){
				SQLMethod.updatePackageEdge(afterRelease.getReleaseName(),  afterClazzs.get(i).getName(), afterClazzs.get(i).getAllPath(), -1.0);
			}
		}
		
	}
	
	public static void GetClazzEdgeP2p(ReleaseCommit beforeRelease,
			ReleaseCommit afterRelease){
		List<ClazzEdge> beforeClazzEdges=new ArrayList<ClazzEdge>();
		List<ClazzEdge> afterClazzEdges=new ArrayList<ClazzEdge>();
		if(beforeRelease!=null){
			beforeClazzEdges=SQLMethod.queryClazzEdge(beforeRelease.getReleaseName());
			afterClazzEdges=SQLMethod.queryClazzEdge(afterRelease.getReleaseName());
				for(int i=0;i<afterClazzEdges.size();i++){
					boolean type=false;
					for(int j=0;j<beforeClazzEdges.size();j++){
						if(beforeClazzEdges.get(j).getStartpoint().equals(afterClazzEdges.get(i).getStartpoint())&&beforeClazzEdges.get(j).getEndpoint().equals(afterClazzEdges.get(i).getEndpoint())&&beforeClazzEdges.get(j).getType().equals(afterClazzEdges.get(i).getType())){
								double p2pValue=(afterClazzEdges.get(i).getNum()-beforeClazzEdges.get(j).getNum())/(double)beforeClazzEdges.get(j).getNum();//计算类之间边的变化率
								SQLMethod.updateClazzEdge(afterRelease.getReleaseName(), afterClazzEdges.get(i).getStartpoint(), afterClazzEdges.get(i).getEndpoint(), afterClazzEdges.get(i).getType(),p2pValue);
								type=true;
						}
					}
					if(type==false){
						SQLMethod.updateClazzEdge(afterRelease.getReleaseName(), afterClazzEdges.get(i).getStartpoint(), afterClazzEdges.get(i).getEndpoint(), afterClazzEdges.get(i).getType(), -1.0);
					}
			}		
			//pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
		}else{
			for(int i=0;i<afterClazzEdges.size();i++){
				SQLMethod.updateClazzEdge(afterRelease.getReleaseName(), afterClazzEdges.get(i).getStartpoint(), afterClazzEdges.get(i).getEndpoint(), afterClazzEdges.get(i).getType(), -1.0);
			}
		}
		
	}
	
	
	public static Map<String, Double> GetP2p(ReleaseCommit beforeRelease,
			ReleaseCommit afterRelease) {
		// List<String> packages=new ArrayList<String>();
		Map<String, Double> p2ps = new HashMap<String, Double>();
		List<Package> beforePkgs = new ArrayList<Package>();
		List<Package> afterPkgs = new ArrayList<Package>();
		if (beforeRelease != null) {
			beforePkgs = SQLMethod.queryPackage(beforeRelease.getReleaseName());
			afterPkgs = SQLMethod.queryPackage(afterRelease.getReleaseName());
			Map<String, Package> beforeMapPS = new HashMap<String, Package>();
			Map<String, Package> afterMapPS = new HashMap<String, Package>();
			for (int i = 0; i < beforePkgs.size(); i++) {
				beforeMapPS.put(beforePkgs.get(i).getAllPath(),
						beforePkgs.get(i));
			}
			for (int j = 0; j < afterPkgs.size(); j++) {
				afterMapPS.put(afterPkgs.get(j).getAllPath(), afterPkgs.get(j));
			}
			for (int i = 0; i < afterPkgs.size(); i++) {
				if (beforeMapPS.get(afterPkgs.get(i).getAllPath()) != null) {
					Package bp=beforeMapPS.get(afterPkgs.get(i).getAllPath());
					int count = 0;
					List<Clazz> beforeClazzs = new ArrayList<Clazz>();
					List<Clazz> afterClazzs = new ArrayList<Clazz>();
					beforeClazzs = SQLMethod.queryClazzOfPackage(beforeRelease
							.getReleaseName(), afterPkgs.get(i).getAllPath());
					afterClazzs = SQLMethod.queryClazzOfPackage(afterRelease.getReleaseName(), afterPkgs.get(i).getAllPath());
					int maxSize = 0;
					if (beforeClazzs.size()> afterClazzs.size()) {
						maxSize = beforeClazzs.size();
					} else {
						maxSize = afterClazzs.size();
					}
					for (int j = 0; j < afterClazzs.size(); j++) {
						// 判断两个类是否发生变化，如果没有发生变化，count++
						for (int p = 0; p < beforeClazzs.size(); p++) {
							if (beforeClazzs.get(p).getName().equals(afterClazzs.get(j).getName())) {
								if (beforeClazzs.get(p).getLoc() == afterClazzs
										.get(j).getLoc()) {
									count++;
									break;
								}
							}
						}

					}
					double p2pValue = count / (double) maxSize;
					System.out.println("P2P:" + afterPkgs.get(i).getPath()
							+ "1 " + p2pValue);
					p2ps.put(afterPkgs.get(i).getPath(), p2pValue);
					SQLMethod.updatePackage(afterRelease.getReleaseName(),
							afterPkgs.get(i).getAllPath(), p2pValue);
				} else {
					System.out.println("P2P:" + afterPkgs.get(i).getPath()
							+ "2 " + 0.0);
					p2ps.put(afterPkgs.get(i).getPath(), 0.0);
					SQLMethod.updatePackage(afterRelease.getReleaseName(),
							afterPkgs.get(i).getAllPath(), 0.0);
				}
			}

		} else {
			for (int i = 0; i < afterPkgs.size(); i++) {
				System.out.println("P2P:" + afterPkgs.get(i).getPath() + "3 "
						+ 0.0);
				p2ps.put(afterPkgs.get(i).getPath(), 0.0);
				SQLMethod.updatePackage(afterRelease.getReleaseName(),
						afterPkgs.get(i).getAllPath(), 0.0);
			}
		}
		pkgP2ps.put(afterRelease.getReleaseName(), p2ps);
		return p2ps;
	}

	public static void printP2p(GitController gitController){
		GitController fGit = gitController;
		List<ReleaseCommit> commitIDS=fGit.getReleaseCommits();
		for (int i = 0; i < commitIDS.size(); i++) {//commitIDS.size()
			if(i==0){
				//GetP2p(null,commitIDS.get(i));
				//GetPacakgeEdgeOfP2p(null,commitIDS.get(i));
				//GetClazzEdgeP2p(null, commitIDS.get(i));
				GetClazzP2p(null, commitIDS.get(i));
			}else{
			//GetP2p(commitIDS.get(i-1),commitIDS.get(i));
			//GetPacakgeEdgeOfP2p(commitIDS.get(i-1),commitIDS.get(i));
			//GetClazzEdgeP2p(commitIDS.get(i-1), commitIDS.get(i));
			GetClazzP2p(commitIDS.get(i-1), commitIDS.get(i));
			}
		}
	}
	public static void printP2p2(GitController gitController){
		GitController fGit = gitController;
		List<ReleaseCommit> commitIDS=fGit.getReleaseCommits();
		for (int i = 0; i < commitIDS.size(); i++) {//commitIDS.size()
			if(i==0){
				GetClazzEdgeP2p(null, commitIDS.get(i));
				GetClazzP2p(null, commitIDS.get(i));
			}else{
			GetClazzEdgeP2p(commitIDS.get(i-1), commitIDS.get(i));
			GetClazzP2p(commitIDS.get(i-1), commitIDS.get(i));
			}
		}
	}
}
