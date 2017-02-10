package fjs.jde.method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import as.jde.scm.git.GitController;

import fjs.jde.model.ChangeEdge;
import fjs.jde.model.Clazz;
import fjs.jde.model.JavaFile;
import fjs.jde.model.ReleaseCommit;
import fjs.jde.sql.SQLMethod;
import fjs.jde.model.Package;

public class A2a {
	
	public static void putAdd(String sr,Map <String,ArrayList<String>> m,String name){
        if(!m.containsKey(sr)){
            m.put(sr, new ArrayList<String>());
        }
        m.get(sr).add(name);
    }
	public static double getA2a(ReleaseCommit commitID1,ReleaseCommit commitID2,List<String> addedFiles,List<String> modifiedFiles,List<String> deletedFiles){
		double addP=0;
		double remP=0;
		double mto=0;
		double aco1=0;
		double aco2=0;
		double addC1=0,addP1=0;
		double addC2=0,addP2=0,deleted=0,modified=0,add=0,mov=0;
		double addE1=0,addE2=0;
		double a2a;
		if(commitID1!=null){
		ChangeEdge changeEdge=SQLMethod.queryChangeEdge(commitID2.getReleaseName());
		List<Package> p1=SQLMethod.queryPackage(commitID1.getReleaseName());
		addP1=p1.size();
		List<Package> p2=SQLMethod.queryPackage(commitID2.getReleaseName());
		addP2=p2.size();
		List<Clazz> c1=SQLMethod.queryClazz(commitID1.getReleaseName());
		Map<String, List<String>> beforeFiles = new HashMap<String, List<String>>();
		addC1=c1.size();
		List<Clazz> c2=SQLMethod.queryClazz(commitID2.getReleaseName());
		List<JavaFile> file1=SQLMethod.queryFiles(commitID1.getReleaseName());
		List<JavaFile> file2=SQLMethod.queryFiles(commitID2.getReleaseName());
		Map<String,String> fileToPackage=new HashMap<String, String>();
		addC2=c2.size();
		addE1=SQLMethod.queryClazzEdges(commitID1.getReleaseName());
		addE2=SQLMethod.queryClazzEdges(commitID2.getReleaseName());
		aco1=addP1+addC1+addE1;
		aco2=addP2+addC2+addE2;
		for(int i=0;i<p1.size();i++){
			if(!p2.contains(p1.get(i))){
				remP++;
			}
		}
		for(int i=0;i<p2.size();i++){
			if(!p1.contains(p2.get(i))){
				++addP;
			}
		}
		
		for(int i=0;i<addedFiles.size();i++){
			for(int j=0;j<c2.size();j++){
				if(c2.get(j).getFileName().equals(addedFiles.get(i))){
					++add;
				}
			}
		}
		for(int i=0;i<deletedFiles.size();i++){
			for(int j=0;j<c1.size();j++){
				if(c1.get(j).getFileName().equals(deletedFiles.get(i))){
					++deleted;
				}
			}
		}
		
		for(int i=0;i<modifiedFiles.size();i++){
			for(int j=0;j<c2.size();j++){
				if(c2.get(j).getFileName().equals(modifiedFiles.get(i))){
					for(int p=0;p<c1.size();p++){
						if(c1.get(p).getFileName().equals(modifiedFiles.get(i))){
							if(c2.get(j).getLoc()!=c1.get(p).getLoc()){
								++modified;
							}
						}
					}
				}
			}
		}
		for(int i=0;i<file1.size();i++){
			fileToPackage.put(file1.get(i).getFileName(), file1.get(i).getPkg());
		}
		for(int i=0;i<file2.size();i++){
			if(fileToPackage.get(file2.get(i).getFileName())!=null){
			if(!file2.get(i).getPkg().equals(fileToPackage.get(file2.get(i).getFileName()))){
				++mov;
			}
			}
		}
		mto=remP+addP+deleted+add+modified+mov+changeEdge.getAddEdge()+changeEdge.getRemEdge();
		if(aco1!=0){
		aco1=aco1+aco2;
		a2a=(1-mto/aco1)*100;
		}else{
			a2a=100;
		}}else{
			a2a=0;
		}
		return a2a;
	}
	public static void printA2a(GitController gitController){
		GitController fGit = gitController;
		List<ReleaseCommit> commitIDS=fGit.commits;
		Map<String, List<String>> affectedFiles = new HashMap<String, List<String>>();
		List<String> addedFiles=new ArrayList<String>();
		List<String> modifiedFiles=new ArrayList<String>();
		List<String> deletedFiles=new ArrayList<String>();
		
		System.out.println(commitIDS.size());
			for (int i = 0; i < commitIDS.size(); i++) {
				
				if(i==0){
					SQLMethod.insertA2A(commitIDS.get(i).getReleaseName(), null,commitIDS.get(i).getCommitID(), getA2a(null, commitIDS.get(i), addedFiles, modifiedFiles, deletedFiles));
				}else{
				affectedFiles=fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID());
				if(affectedFiles.get("A")!=null){
					
					for(int j=0;j<fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("A").size();j++){
						addedFiles.add(fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("A").get(j));
					}
				}
				if(affectedFiles.get("M")!=null){
					for(int j=0;j<fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("M").size();j++){
						modifiedFiles.add(fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("M").get(j));
					}
				}
				if(affectedFiles.get("D")!=null){
					for(int j=0;j<fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("D").size();j++){
						deletedFiles.add(fGit.getAffectedFiles(commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID()).get("D").get(j));
					}
				}
					
					SQLMethod.insertA2A(commitIDS.get(i).getReleaseName(), commitIDS.get(i-1).getCommitID(),commitIDS.get(i).getCommitID(), getA2a(commitIDS.get(i-1), commitIDS.get(i), addedFiles, modifiedFiles, deletedFiles));
				}
		
	    }
		
	}
	
}
