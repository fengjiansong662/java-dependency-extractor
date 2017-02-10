package fjs.jde.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import as.jde.scm.git.GitController;

import fjs.jde.graph.CallClazzGraph;
import fjs.jde.model.ChangeEdge;
import fjs.jde.model.Clazz;
import fjs.jde.model.ClazzEdge;
import fjs.jde.model.ReleaseCommit;
import fjs.jde.sql.SQLMethod;

public class ChangeEdges {
	
	public static void GetChangeEdges(ReleaseCommit beforeRelease,ReleaseCommit afterRelease){
		int addE=0;
		int remE=0;
		int addNE=0;//从空集到目前版本中添加的边
		List<Integer> changeEdges=new ArrayList<Integer>();
		addNE=SQLMethod.queryClazzEdge(afterRelease.getReleaseName()).size();
		if(beforeRelease==null){
			addE=addNE;
			
		}else{
			List<ClazzEdge> beforeClazzEdge=new ArrayList<ClazzEdge>();
			List<ClazzEdge> afterClazzEdge=new ArrayList<ClazzEdge>();
			beforeClazzEdge=SQLMethod.queryClazzEdge(beforeRelease.getReleaseName());
			afterClazzEdge=SQLMethod.queryClazzEdge(afterRelease.getReleaseName());
			for(int i=0;i<afterClazzEdge.size();i++){
				boolean type=false;
				for(int j=0;j<beforeClazzEdge.size();j++){
					if (beforeClazzEdge.get(j).getStartpoint()
							.equals(afterClazzEdge.get(i).getStartpoint())
							&& beforeClazzEdge
									.get(j)
									.getEndpoint()
									.equals(afterClazzEdge.get(i).getEndpoint())
							&& beforeClazzEdge.get(j).getType()
									.equals(afterClazzEdge.get(i).getType())) {
						if(afterClazzEdge.get(i).getNum()-beforeClazzEdge.get(j).getNum()>=0){
							addE+=afterClazzEdge.get(i).getNum()-beforeClazzEdge.get(j).getNum();
						}else{
							remE=beforeClazzEdge.get(j).getNum()-afterClazzEdge.get(i).getNum();
						}
						beforeClazzEdge.remove(j);
						type=true;
						break;
					}
				}
				if(type==false){
					addE+=afterClazzEdge.get(i).getNum();
				}
			}
			if(beforeClazzEdge.size()!=0){
				for(int i=0;i<beforeClazzEdge.size();i++){
					remE+=beforeClazzEdge.get(i).getNum();
				}
			}
		}
		changeEdges.add(addE);
		changeEdges.add(remE);
		changeEdges.add(addNE);
		SQLMethod.insertChangeEdge(afterRelease.getReleaseName(), afterRelease.getCommitID(), changeEdges);
		
	}
	public static void printChangeEdges(GitController gitController){
		GitController fGit = gitController;
		List<ReleaseCommit> commitIDS=fGit.getReleaseCommits();
		System.out.println(commitIDS.size());
		for (int i = 0; i < commitIDS.size(); i++) {//commitIDS.size()
			if(i==0){
				GetChangeEdges(null,commitIDS.get(i));
			}else{
				GetChangeEdges(commitIDS.get(i-1),commitIDS.get(i));
			}
			
		}
	}
}
