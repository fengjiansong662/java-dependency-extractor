package as.jde.scm.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fjs.jde.model.ReleaseCommit;

import as.jde.scm.Commit;
import as.jde.scm.Owner;
import as.jde.util.ProcessSpawner;

public class GitController {
	
	public final static String ADD = "A";
	public final static String DELETE = "D";
	public final static String MODIFY = "M";
	
	private static final String GIT_HEAD = "[a-z0-9]+";
	private static final String GIT_BLAME = "\\<(.+?)\\>";
	public static List<ReleaseCommit> commits = new ArrayList<ReleaseCommit>();
	public static List<ReleaseCommit> runCommits= new ArrayList<ReleaseCommit>();
	private ProcessSpawner fSpawner;
	public static String fRepository;
	
	
	public GitController(String repositoryPath) {
		fRepository = repositoryPath;
		fSpawner = new ProcessSpawner(repositoryPath);
	}
	
	public String getRepositoryPath() {
		return fRepository;
	}
	
	/**
	 * resets the repository to a specific commit
	 * 
	 * @param commitID the commitID the repository is supposed to be set to
	 */
	public void reset(String commitID) {
		fSpawner.spawnProcess(new String[] {"git", "reset", "--hard", commitID});
	}
	
	
	/**
	 * 
	 */
	public void clone(String repositoryPath){
		System.out.println("git clone");
		fSpawner.spawnProcess(new String[] {"git", "clone", repositoryPath});
		System.out.println("git clone 成功");
	}
	
	
	
	
	/**
	 * gets a list of all commits in the repository
	 * 
	 * @return list of commitids
	 */
	public List<String> getAllCommits() {
		List<String> commits = new ArrayList<String>();
		//String output = fSpawner.spawnProcess(new String[] {"git", "rev-list", "--all", "--pretty=oneline", "--reverse"});
		String output = fSpawner.spawnProcess(new String[] {"git", "rev-list", "--all", "--pretty=oneline", "--no-merges","--reverse"});// 
		String[] lines = output.split(System.getProperty("line.separator"));
		for (String line : lines) {
			
			commits.add(line.split(" ")[0]);
		}
	
		return commits;
	}
	
	/**
	 * gets a list of releases commits in the repository
	 * @return list of releases commitids
	 */
	public List<ReleaseCommit> getReleaseCommits(){
		//List<ReleaseCommit> commits = new ArrayList<ReleaseCommit>();
		BufferedReader br;
		try {
			br = new BufferedReader(
					new FileReader(
							"F:\\java MyEclipse10.7\\Workspaces\\MyEclipse 10\\java-dependency-extractor\\result.txt"));

			String line;
			
			while ((line = br.readLine()) != null) {
				if(!line.contains("filename:./result.txt")){
					//String []s=line.split("&#\\*:\\*#&");
					String []s=line.split("	");
					ReleaseCommit commit=new ReleaseCommit();
					commit.setCommitID(s[0]);
					commit.setReleaseName(s[2]);
					commits.add(commit);
				}
				
			}
			Collections.reverse(commits);//反转指定列表中元素的顺序。
			for(int i=0;i<10;i++){
				
				runCommits.add(commits.get(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return runCommits;
	}
	
	/**
	 * computes which files have been added/deleted/modified
	 * 
	 * note, the filename are all relative and not absolute
	 * 
	 * @param beforeCommitId
	 * @param afterCommitId
	 * @return
	 */
	public Map<String, List<String>> getAffectedFiles(String beforeCommitId, String afterCommitId) {
		
		String output = fSpawner.spawnProcess(new String[] {"git", "diff", "--name-status", beforeCommitId, afterCommitId});
		
		HashMap<String, List<String>> affectedFiles = new HashMap<String, List<String>>();
			
		affectedFiles.put(ADD, new ArrayList<String>());
		affectedFiles.put(DELETE, new ArrayList<String>());
		affectedFiles.put(MODIFY, new ArrayList<String>());

		if (output.length() == 0)
			return affectedFiles;
		
		String[] lines = output.split(System.getProperty("line.separator"));
		String type = null;
		for (String line : lines) {
			if (line.startsWith(ADD)) type = ADD;
			if (line.startsWith(DELETE)) type = DELETE;
			if (line.startsWith(MODIFY)) type = MODIFY;
			if(line.contains("bad")){
				break;
			}
			String filename = line.replaceFirst(type, "");
			filename = filename.replaceAll("\\s", "");
			if(filename.endsWith(".java")){
				affectedFiles.get(type).add(fRepository + filename);
			}
			
		}
		
		return affectedFiles;
	}
	
	/**
	 * TODO should be returning a list of fileChanges
	 * 
	 * @param commitID
	 * @return String containing the command line diff output
	 */
	public String getCommitDiff(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commitID});
	}
	
	/**
	 * TODO should be returning a list of fileChanges
	 * 
	 * @param commitID
	 * @param files files that should be diffed
	 * @return one string blobb containing all the diff information
	 */
	public String getCommitDiff(String commitID, List<String> files) {
		if(files.isEmpty())
			return getCommitDiff(commitID);
		else {
			files.add(0, "--");
			files.add(0, commitID);
			files.add(0, "--unified=0");
			files.add(0, "diff-tree");
			files.add(0, "git");
		}
		return fSpawner.spawnProcess(files);
	}
	
	public String getCommitDiffHunkHeaders(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commitID, "|" , "grep" , "'+++|---|@@'"});
	}
	
	/*public String getCommit(String commitID1,String commitID2){
		return fSpawner.spawnProcess(new String[] {"git", "diff-tree", "--unified=0", commitID, "|" , "grep" , "'+++|---|@@'"});
	}*/
	
	public String getCommitDiffHunkHeaders(String commitID, List<String> files) {
		if(files.isEmpty())
			return getCommitDiff(commitID);
		else {
			files.add(0, "--");
			files.add(0, commitID);
			files.add(0, "--unified=0");
			files.add(0, "diff-tree");
			files.add(0, "git");
			files.add("|");
			files.add("grep");
			files.add("'+++|---|@@'");
		}
		return fSpawner.spawnProcess(files);
	}
	
	/**
	 * returns the author of the commit
	 * 
	 * @param commitID
	 * @return auther string
	 */
	public String getAuthorOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%ce", commitID}).replace("\n", "");
	}
	
	
	
	/**
	 * returns the date of the commit
	 * @param commitID
	 * @return date in string form
	 */
	public String getDateOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%cd", commitID}).replace("\n", "");
	}
	
	/**
	 * returns the newline of the commit
	 * @param commitID
	 * @return newline in string form
	 */
	public String getNewLineOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%B", commitID}).replace("\n", "");
	}
	
	/**
	 * number of lines of code added, deleted, or altered
	 * @param commitID
	 * @return
	 */
	
	public String getChangeLines(String commitID1,String commitID2){
		return fSpawner.spawnProcess(new String[] {"git", "diff",  "--shortstat", commitID1,commitID2 , "'+++|---|@@'"}).replace("\n", "");
	}
	
	public String getbOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "--format=%b", commitID}).replace("\n", "");
	}
	
	/**
	 * returns the newline of the commit
	 * @param commitID
	 * @return newline in string form
	 */
	public String getAuthorNameOfCommit(String commitID) {
		return fSpawner.spawnProcess(new String[] {"git", "show", "-s", "--format=%an", commitID}).replace("\n", "");
	}
	
	
	/**
	 * returns the authors that last edited the file in the given line range
	 * 
	 * @param file
	 * @param startLine
	 * @param endLine
	 * @return
	 */
	public List<Owner> getOwnersOfFileRange(String file, int startLine, int endLine) {
		List<Owner> owners = new ArrayList<Owner>();
		
		String output = fSpawner.spawnProcess(new String[] {"git", "blame", "-e", "-L"+startLine+","+endLine, file});
		String[] lines = output.split(System.getProperty("line.separator"));
		
		Pattern pattern = Pattern.compile(GIT_BLAME);
		for(int i = 0; i < lines.length; i++) {
			Matcher matcher = pattern.matcher(lines[i]);

			if(matcher.find()) {
				Owner owner = new Owner();
				owner.setEmail(matcher.group(1));
				owner.setOwnership((float)1/(endLine-startLine+1));
				updateOwnersList(owners, owner);
			}
		}
		
		return owners;
	}
	
	private void updateOwnersList(List<Owner> owners, Owner owner) {
		for(Owner own: owners) {
			if(own.getEmail().equals(owner.getEmail())) {
				own.setOwnership(own.getOwnership()+owner.getOwnership());
				return;
			}
		}
		owners.add(owner);
	}
	
	/**
	 * Returns the HEAD commit ID of the repository.
	 * @return
	 */
	public String getHead() {
		String output = fSpawner.spawnProcess(new String[] {"git", "rev-parse", "HEAD"});
		String[] lines = output.split(System.getProperty("line.separator"));
		
		Pattern pattern = Pattern.compile(GIT_HEAD);
		Matcher matcher = pattern.matcher(lines[0]);
		
		if(matcher.find()) {
			return lines[0];
		}
		
		return null;
	}

	/**
	 * return a commit information object
	 * 
	 * @param commitID
	 * @return
	 */
	public Commit getCommitInfo(String commitID) {
		Commit c = new Commit();
		
		c.author = getAuthorOfCommit(commitID);
		c.commitID = commitID;
		c.time = getDateOfCommit(commitID);
		c.newLine=getNewLineOfCommit(commitID);
/*
		System.out.println("c.author:"+c.author);
		System.out.println("c.commitID:"+c.commitID);
		System.out.println("c.time:"+c.time);
		System.out.println("c.newLine:"+c.newLine);
		System.out.println("author name:"+getAuthorNameOfCommit(commitID));
		System.out.println("b:"+getbOfCommit(commitID));*/
		
		return c;
	}
}
