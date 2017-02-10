package fjs.jde.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.CompilationUnit;

import fjs.jde.ast.ASTClazzVisitor;
import fjs.jde.ast.BlockingBindingResolverClazz;
import fjs.jde.ast.ClassBindingResolver;
import fjs.jde.graph.CallClazzGraph;
import fjs.jde.model.Clazz;
import fjs.jde.model.ClazzEdge;
import fjs.jde.model.JavaFile;
import fjs.jde.model.Package;
import fjs.jde.model.PackageEdge;
import fjs.jde.model.ReleaseCommit;
import fjs.jde.method.Abstractness;
import fjs.jde.method.Ca;
import fjs.jde.method.Ce;
import fjs.jde.method.ChangeEdges;
import fjs.jde.method.Instability;
import fjs.jde.method.LOC;
import fjs.jde.method.NC;
import fjs.jde.method.NP;
import fjs.jde.method.NumberOfClasses;
import fjs.jde.method.NumberOfMethodParameters;
import fjs.jde.method.P2p;
import fjs.jde.sql.SQLMethod;    //写p2p时注释掉，以后还要放开

import as.jde.ast.JavaFileParser;
import as.jde.diff.FileChange;
import as.jde.diff.Range;
import as.jde.diff.UnifiedDiffParser;
import as.jde.graph.CallGraph;
/*import as.jde.diff.FileChange;
import as.jde.diff.Range;
import as.jde.diff.UnifiedDiffParser;*/
import as.jde.scm.git.GitController;
import as.jde.util.JProject;
import as.jde.util.JavaJarLocator;

public class SCMIterator {
	private GitController fGit = null;
	private int fCurrentCommit = -1;
	//private List<String> fCommits = null;
	private List<ReleaseCommit> fCommits = null;
	private JProject fProject = null;
	private Pattern fIgnoreFolderPattern;
	private static  ArrayList<String> superClazzs=new ArrayList<String>();
	Map<String, List<String>> affectedFiles = new HashMap<String, List<String>>();
	
	public SCMIterator(GitController gitController, String ignoreFolderRegExp) {
		fGit = gitController;
		//fCommits = fGit.getAllCommits();
		fCommits=fGit.getReleaseCommits();
		fIgnoreFolderPattern = Pattern.compile(ignoreFolderRegExp);
	}

	public boolean hasNext() {
		return fCommits.size()-1 > fCurrentCommit ;
	}

	synchronized public CallClazzGraph next() {
		fCurrentCommit++;
		
		String commitID= fCommits.get(fCurrentCommit).getCommitID();
		
		// checkout next revision
		fGit.reset(commitID);
		//fGit.getCommitDiff(commitID);
		
		fProject = initJavaProject();
		CallClazzGraph ccg = createCallGraph(commitID,fCommits.get(fCurrentCommit).getReleaseName());
		int beforeId = 0;
		if(fCurrentCommit!=0){
			beforeId=fCurrentCommit-1;
			String commitID1=fCommits.get(beforeId).getCommitID();
			//P2p.GetP2ps(commitID1, commitID);
			fGit.getChangeLines(commitID1, commitID);
			if(fGit.getAffectedFiles(commitID1, commitID)!=null){
			affectedFiles=fGit.getAffectedFiles(commitID1, commitID);
			if(affectedFiles.get("A")!=null){
				System.out.println("SCMIterator A SIZE:"+fGit.getAffectedFiles(commitID1, commitID).get("A").size());
				for(int i=0;i<fGit.getAffectedFiles(commitID1, commitID).get("A").size();i++){
					System.out.println(fGit.getAffectedFiles(commitID1, commitID).get("A").get(i));
				}
			}
			if(affectedFiles.get("M")!=null){
				System.out.println("SCMIterator M SIZE:"+fGit.getAffectedFiles(commitID1, commitID).get("M").size());
			}
			if(affectedFiles.get("D")!=null){
				System.out.println("SCMIterator D SIZE:"+fGit.getAffectedFiles(commitID1, commitID).get("D").size());
			}
			}
			markChangedClasses(commitID1, ccg);
		}else{
			String before=null;
			//P2p.GetP2ps(before, commitID);
			
		}
		//System.out.println("SCMIterator:"+ccg.getClasses().size());
		SQLMethod.insertClazzs(ccg.getClasses());   //与A2A计算有关，写p2p时注释掉，以后还要放开


		Iterator it =ASTClazzVisitor.pcf.entrySet().iterator();
		while(it.hasNext()){
					Map.Entry<String,Set<String>> entry=(Map.Entry<String, Set<String>>)it.next();
					String key=entry.getKey();
					Set<String> value=entry.getValue();
					//System.out.println("Key="+key+" value="+value.size());
		}
		return ccg;
	}
	//下载远程仓库对应的next1     修改前的
/*	synchronized public CallClazzGraph next(String repo) {

		fCurrentCommit++;
		
		String commitID= fCommits.get(fCurrentCommit);
	
		// checkout next revision
		fGit.reset(commitID);
			
		fGit.clone(repo);
		
		fProject = initJavaProject();

		CallClazzGraph ccg = createCallGraph(commitID);
		
		
		return ccg;
	}*/
	
	
	//下载远程仓库对应的next2
	synchronized public void next(String repo) {

		fCurrentCommit++;
		
		String commitID= fCommits.get(fCurrentCommit).getCommitID();
	
		// checkout next revision
		fGit.reset(commitID);
			
		fGit.clone(repo);
		
		fProject = initJavaProject();			
	}
	
	
	private synchronized CallClazzGraph createCallGraph(final String commitID,final String releaseCommit) {
		final CallClazzGraph cg = new CallClazzGraph(fGit.getCommitInfo(commitID));

		if (fProject.javaFiles.size() == 0) return cg;
		
		// parse files
		JavaFileParser parser = new JavaFileParser(fProject.classPath, fProject.sourcePath.getPaths(), fGit.getRepositoryPath());
		
		List<String> files = new ArrayList<String>();
		Map<String,String> fileSourcePath=new HashMap<String, String>();
		
		for (File file : fProject.javaFiles) {
			if(file.getAbsolutePath()!=null && file.getAbsolutePath().contains(".java")){
			files.add(file.getAbsolutePath());
			fileSourcePath.put(file.getAbsolutePath(), file.getParentFile().getAbsolutePath());
			}
		}
		int listsize = (null == files) ? 0 : files.size();// List总记录条数，做非空判断避免程序出现异常
		int perpagesize = 800;// 每个子List存放的记录条数
		int sumpagenumber = listsize / perpagesize;// 总共需要多少个子List，整数相除会直接舍去小数部分
		int lastListsize = listsize % perpagesize;// 最后一个List的size
		if (lastListsize != 0) {// 如果最后一个List的大小不为0，则所需的List个数需要加1
			sumpagenumber++;
		}
		for (int i = 0; i < sumpagenumber; i++) {
			int starnum = i * perpagesize;// 每个子List起始的位置
			if (starnum + perpagesize < listsize) {
				System.out.println((starnum + 1) + "-"
						+ (starnum + perpagesize));// 每个List包含的记录范围
				System.out.println(files
						.subList(starnum, starnum + perpagesize));// 子List
				final Map<String, CompilationUnit> cUnits = parser
						.parseFiles(files.subList(starnum, starnum
								+ perpagesize));

				ExecutorService executor = Executors.newFixedThreadPool(Runtime
						.getRuntime().availableProcessors());

				// visit all compilation units
				final BlockingBindingResolverClazz bbrc = new BlockingBindingResolverClazz();
				final ClassBindingResolver cbr = new ClassBindingResolver();
				for (final String fullyQuallifiedFilename : cUnits.keySet()) {
					final JavaFile fullyQuallifiedFile = new JavaFile();
					fullyQuallifiedFile.setFileName(fullyQuallifiedFilename);

					fullyQuallifiedFile.setSourcePath(fileSourcePath.get(
							fullyQuallifiedFilename).substring(
							GitController.fRepository.length()));
					Runnable r = new Runnable() {
						public void run() {
							CompilationUnit unit = cUnits
									.get(fullyQuallifiedFilename);
							fullyQuallifiedFile
									.setFileName(fullyQuallifiedFilename);
							// System.out.println("SCMIterator package:"+unit.getPackage().getName().getFullyQualifiedName());
							fullyQuallifiedFile.setLength(unit.getLength());
							fullyQuallifiedFile.setPkg(unit.getPackage()
									.getName().getFullyQualifiedName());
							ASTClazzVisitor visitor = new ASTClazzVisitor(
									fullyQuallifiedFile, unit, cg, cbr, bbrc,
									commitID, releaseCommit);
							// System.out.println(fullyQuallifiedFilename);
							// //打印所有的java文件
							unit.accept(visitor);
						}
					};
					executor.execute(r);
				}

				executor.shutdown();

				while (!executor.isTerminated()) {
					try {
						this.wait(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.out.println((starnum + 1) + "-" + (listsize));// 每个List包含的记录范围
				System.out.println(files.subList(starnum, listsize));// 子List
				final Map<String, CompilationUnit> cUnits = parser
						.parseFiles(files.subList(starnum, listsize));

				ExecutorService executor = Executors.newFixedThreadPool(Runtime
						.getRuntime().availableProcessors());

				// visit all compilation units
				final BlockingBindingResolverClazz bbrc = new BlockingBindingResolverClazz();
				final ClassBindingResolver cbr = new ClassBindingResolver();
				for (final String fullyQuallifiedFilename : cUnits.keySet()) {
					final JavaFile fullyQuallifiedFile = new JavaFile();
					fullyQuallifiedFile.setFileName(fullyQuallifiedFilename);

					fullyQuallifiedFile.setSourcePath(fileSourcePath.get(
							fullyQuallifiedFilename).substring(
							GitController.fRepository.length()));
					Runnable r = new Runnable() {
						public void run() {
							CompilationUnit unit = cUnits
									.get(fullyQuallifiedFilename);
							fullyQuallifiedFile
									.setFileName(fullyQuallifiedFilename);
							// System.out.println("SCMIterator package:"+unit.getPackage().getName().getFullyQualifiedName());
							fullyQuallifiedFile.setLength(unit.getLength());
							fullyQuallifiedFile.setPkg(unit.getPackage()
									.getName().getFullyQualifiedName());
							ASTClazzVisitor visitor = new ASTClazzVisitor(
									fullyQuallifiedFile, unit, cg, cbr, bbrc,
									commitID, releaseCommit);
							// System.out.println(fullyQuallifiedFilename);
							// //打印所有的java文件
							unit.accept(visitor);
						}
					};
					executor.execute(r);
				}

				executor.shutdown();

				while (!executor.isTerminated()) {
					try {
						this.wait(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		/*final Map<String, CompilationUnit> cUnits = parser.parseFiles(files);
		System.out.println(cUnits.size());
		
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		
		// visit all compilation units
		final BlockingBindingResolverClazz bbrc = new BlockingBindingResolverClazz();
		final ClassBindingResolver cbr = new ClassBindingResolver();
		for (final String fullyQuallifiedFilename : cUnits.keySet()) {
			final JavaFile fullyQuallifiedFile=new JavaFile();
			fullyQuallifiedFile.setFileName(fullyQuallifiedFilename);
			
			fullyQuallifiedFile.setSourcePath(fileSourcePath.get(fullyQuallifiedFilename).substring(GitController.fRepository.length()));
			Runnable r = new Runnable() {
				public void run() {
					CompilationUnit unit = cUnits.get(fullyQuallifiedFilename);
					fullyQuallifiedFile.setFileName(fullyQuallifiedFilename);
					//System.out.println("SCMIterator package:"+unit.getPackage().getName().getFullyQualifiedName());
					fullyQuallifiedFile.setLength(unit.getLength());
					fullyQuallifiedFile.setPkg(unit.getPackage().getName().getFullyQualifiedName());
					ASTClazzVisitor visitor = new ASTClazzVisitor(fullyQuallifiedFile, unit, cg, cbr, bbrc,commitID,releaseCommit);
					//System.out.println(fullyQuallifiedFilename); //打印所有的java文件
					unit.accept(visitor);
				}
			};
			executor.execute(r);
		}
		
		executor.shutdown();
		
		while (!executor.isTerminated()) {
			try {
				this.wait(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		
		List<Package> pkgs=pkgRelation(cg,commitID);
		SQLMethod.insertP(pkgs);//与A2A的计算有关，写p2p时注释掉，以后还要放开
		
		for(int i=0;i<pkgs.size();i++){
			for (Map.Entry<String, Integer> entry  : pkgs.get(i).dependencyEdge.entrySet()) {
		        
		        insertPackageEdge(commitID, releaseCommit, pkgs.get(i).getAllPath(), entry.getKey(), "dependence", entry.getValue());
		       }
			for (Map.Entry<String, Integer> entry  : pkgs.get(i).inheritanceEdge.entrySet()) {
		        insertPackageEdge(commitID, releaseCommit, pkgs.get(i).getAllPath(), entry.getKey(), "inheritance", entry.getValue());
		       }
		}
		
		printPkg(pkgs,commitID,releaseCommit);
		print(cg,commitID,releaseCommit);
		//SQLMethod.insertClazzs(cg.getClasses());
		//计算NC指标
		NC.getNC(cg);
		//计算Ca指标
		Ca.GetCa(commitID,pkgs);
		//计算Ce指标
		Ce.GetCe(commitID,pkgs);
		//计算I指标
		//System.out.println();
		Instability.GetInstability(commitID,Ce.pgksCe,Ca.pgksCa,pkgs);
		//计算类的数量
		NumberOfClasses.GetNC(cg, commitID);
		//计算NP值
		NP.GetNC(cg, commitID);
		//计算每个类的LOC值
		LOC.GetLOC(cg, commitID);
		//计算每个类的每个方法中参数个数
		NumberOfMethodParameters.GetNMP(cg, commitID);
		//计算Abstractness
		Abstractness.getA(cg);
		
		
		//与A2A计算有关，写p2p时注释掉，以后还要放开
		//SQLMethod.insertChangeEdge(releaseCommit,commitID, ChangeEdges.GetChangeEdges(commitID, cg));
		
		return cg;
	}

/*	public List<Package> pkgRelation(CallClazzGraph cg,String commitID){
		List<Package>pkgs=new ArrayList<Package>();
		
		int count=0;
		
		for (int j = 0; j < cg.getClasses().size(); j++) {
			Clazz jj = cg.getClasses().get(j);
			if (pkgs.size() == 0) {
				boolean type = false;
				for (int q = 0; q < pkgs.size(); q++) {
					if (pkgs.get(q).getAllPath().equals(jj.getAllPath())) {
						type = true;
					}
				}
				if (type == false) {
					Package p = new Package();
					p.setSourcePath(jj.getSourcePath());
					p.setAllPath(jj.getAllPath());
					p.setCommitID(commitID);
					p.setReleaseName(jj.getReleaseName());
					p.addClazz(jj);
					//添加继承关系
					if(jj.getSuperClassName().size()!=0){
						addI(p, jj);
					}
					//添加依赖关系
					if (jj.getDependencyRelationship().getDepandMultiplicity()
							.size() != 0) {
						addD(p, jj);
					}
					addN(p, jj);
					pkgs.add(p);
					//SQLMethod.insertPackage(p);
					count++;
	
				}
			} else {
				if (jj.getDependencyRelationship().getDependencyClassName()
						.size() == 0
						&& jj.getSuperClassName().size()== 0) {
					int num = 0;
					int nm = 0;
					while (nm < pkgs.size()) {
						
						if (!pkgs.get(nm).getSimpleName().equals(jj.getPkg())) {
							num++;
						} else {
							pkgs.get(nm).addClazz(jj);
						}
						nm++;
					}
					if (num == pkgs.size()) {
						boolean type = false;
						for (int q = 0; q < pkgs.size(); q++) {
							
							if (pkgs.get(q).getAllPath().equals(jj.getAllPath())) {
								type = true;
							}
						}

						if (type == false) {
							Package p = new Package();
							p.setSourcePath(jj.getSourcePath());
							p.setAllPath(jj.getAllPath());
							p.setCommitID(commitID);
							p.setReleaseName(jj.getReleaseName());
							p.addClazz(jj);
							addN(p, jj);
							pkgs.add(p);
							//SQLMethod.insertPackage(p);
				
						}
					}
				} else if (jj.getDependencyRelationship()
						.getDependencyClassName().size() != 0
						&& jj.getSuperClassName().size() == 0) {
					int num = 0;
					int nm = 0;
					while (nm < pkgs.size()) {
						if (!pkgs.get(nm).getSimpleName().equals(jj.getPkg())) {
							num++;
						} else {
							pkgs.get(nm).addClazz(jj);
							addD(pkgs.get(nm), jj);
						}
						nm++;
					}
					if (num == pkgs.size()) {
						boolean type = false;
						for (int q = 0; q < pkgs.size(); q++) {
							if (pkgs.get(q).getAllPath().equals(jj.getAllPath())) {
								type = true;
							} else if (pkgs.get(q).getSimpleName()
									.equals(jj.getPkg())) {
								type = true;
							}
						}
						if (type == false) {
							Package p = new Package();
							p.setSourcePath(jj.getSourcePath());
							p.setAllPath(jj.getAllPath());
							p.setCommitID(commitID);
							p.setReleaseName(jj.getReleaseName());
							p.addClazz(jj);
							addD(p, jj);
							addN(p, jj);
							pkgs.add(p);
							//SQLMethod.insertPackage(p);
							count++;
						}
					}
				} else if (jj.getDependencyRelationship()
						.getDependencyClassName().size() == 0
						&& jj.getSuperClassName().size()!= 0) {
					int num = 0;
					int nm = 0;
					while (nm < pkgs.size()) {
						if (!pkgs.get(nm).getAllPath().equals(jj.getAllPath())) {
							num++;
						} else {
							pkgs.get(nm).addClazz(jj);
							Package p = pkgs.get(nm);
							addI(p, jj);
						}
						nm++;
					}
					if (num == pkgs.size()) {
						boolean type = false;
						for (int q = 0; q < pkgs.size(); q++) {
							if (pkgs.get(q).getAllPath().equals(jj.getAllPath())) {
								type = true;
							}
						}
						if (type == false) {
							Package p = new Package();
							p.setSourcePath(jj.getSourcePath());
							p.setAllPath(jj.getAllPath());
							p.setCommitID(commitID);
							p.setReleaseName(jj.getReleaseName());
							p.addClazz(jj);
							addI(p, jj);
							addN(p, jj);
							pkgs.add(p);
							//SQLMethod.insertPackage(p);
							count++;

						}
					}
				} else {
					int num = 0;
					int nm = 0;
					while (nm < pkgs.size()) {
						if (!pkgs.get(nm).getAllPath().equals(jj.getAllPath())) {
							num++;
						} else {
							pkgs.get(nm).addClazz(jj);
							Package p = pkgs.get(nm);
							addD(p, jj);
							if(jj.getSuperClassName().size()!=0){
								addI(p, jj);
							}
						}
						nm++;
					}
					if (num == pkgs.size()) {
						boolean type = false;
						for (int q = 0; q < pkgs.size(); q++) {
							if (pkgs.get(q).getAllPath().equals(jj.getAllPath())) {
								type = true;
							}
						}
						if (type == false) {
							Package p = new Package();
							p.setSourcePath(jj.getSourcePath());
							p.setAllPath(jj.getAllPath());
							p.setCommitID(commitID);
							p.setReleaseName(jj.getReleaseName());
							p.addClazz(jj);
							if(jj.getSuperClassName().size()!= 0){
								addI(p, jj);
							}
							addD(p, jj);
							addN(p, jj);
							pkgs.add(p);
							//SQLMethod.insertPackage(p);
							count++;

						}
					}
				}
			}
		}
		for(int i=0;i<pkgs.size();i++){
			pkgs.get(i).setSize(pkgs.get(i).getClazzs().size());
		}
		return pkgs;
	}
	*/
	public List<Package> pkgRelation(CallClazzGraph cg,String commitID){
		Map<String,Package>pkgs=new HashMap<String,Package>();
		
		for (int j = 0; j < cg.getClasses().size(); j++) {
			Clazz jj = cg.getClasses().get(j);
			if(!pkgs.containsKey(jj.getAllPath())){
				Package p = new Package();
				p.setSourcePath(jj.getSourcePath());
				p.setAllPath(jj.getAllPath());
				p.setCommitID(commitID);
				p.setReleaseName(jj.getReleaseName());
				p.addClazz(jj);
				p.setSize(1);
				//添加继承关系
				if(jj.getSuperClassName().size()!=0){
					addI(p, jj);
				}
				//添加依赖关系
				if (jj.getDependencyRelationship().getDepandMultiplicity()
						.size() != 0) {
					addD(p, jj);
				}
				addN(p, jj);
				pkgs.put(jj.getAllPath(), p);
		
	        }else{
	        	Package p=pkgs.get(jj.getAllPath());
	        	p.addClazz(jj);
	        	if (jj.getDependencyRelationship().getDependencyClassName().size() != 0){
	        		addD(p, jj);
	        	}
	        	if(jj.getSuperClassName().size()!= 0){
	        		addI(p, jj);
	        	}
	        	int size=p.getSize()+1;
	        	p.setSize(size);
	        	pkgs.put(jj.getAllPath(), p);
	        }
		}
		List<Package>pkgss=new ArrayList<Package>();
		for (Map.Entry<String, Package> entry : pkgs.entrySet()) {
				pkgss.add(entry.getValue()); 
		}
		return pkgss;
	}
	public void print(CallClazzGraph ccg, String commitID,String releaseName) {
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("encoding=\"UTF-8\"");
		/*gv.addln("ranksep=2.75");
		gv.addln("size=\"15,15\";");
*/		gv.addln("node[shape=record]");

		ArrayList<String> superClazz;
		ArrayList<String> incidenceClass;
		ArrayList<String> dependencyClass;

		Map haveSuperClazz;
		Map haveIncidenceClass;
		Map haveDependencyClass;
		for (int i = 0; i < ccg.getClasses().size(); i++) {
			superClazz = new ArrayList<String>();
			incidenceClass = new ArrayList<String>();
			dependencyClass = new ArrayList<String>();
			haveSuperClazz = new HashMap();
			haveIncidenceClass = new HashMap();
			haveDependencyClass = new HashMap();
			Clazz c = ccg.getClasses().get(i);
			
			String trueName=c.getTempName();
			String lj[]=c.getPkg().split("\\.");
			String name=null;
			for(int q=0;q<lj.length;q++){
				if(name==null){
					name=lj[q];
				}else{
					name+=lj[q];
				}
			}
			String classname=c.getPkg().concat(".").concat(c.getTempName());
			c.setTempName(name.concat(c.getName()));
			if (!(c.getSuperClassName().size() == 0
					&& c.getIncidenceRelationship().getIncidenceClassName()
							.size() == 0 && c.getDependencyRelationship()
					.getDepandMultiplicity().size() == 0)) {
				
					gv.addln(c.getTempName() + "[label = \"" + classname + "\"];");
				List<String> superClazzs = c.getSuperClassName();
				List<String> incidenceClassNames = c.getIncidenceRelationship()
						.getIncidenceClassName();
				List<String> dependencyClassNames = c
						.getDependencyRelationship().getDependencyClassName();

				for (int j = 0; j < superClazzs.size(); j++) {

					
						String superClazz1 = superClazzs.get(j);
					
						if (superClazzs.get(j).contains(">")&&!superClazzs.get(j).contains("<")) {
							superClazz1 = superClazz1.substring(0,
									superClazz1.length() - 1);
						}
						if (!haveSuperClazz.containsKey(superClazz1)) {
							haveSuperClazz.put(superClazz1, 1);
							superClazz.add(superClazz1);
						}
						if (!superClazzs.contains(superClazz1)) {
							superClazzs.add(superClazz1);
						}
					
				}
				if (superClazz.size() > 0) {
					File file = new File("E:/IC.txt");
				    FileWriter fw = null;
					try {
						
					    fw = new FileWriter(file,true);
						fw.write(commitID+":IC(" + classname + ")=:"
								+ superClazz.size()+"\r\n");
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (int j = 0; j < superClazz.size(); j++) {
					String di[] = (superClazz.get(j)).split("\\.");
					String superClazz2 = di[0];
					for(int m=1;m<di.length;m++){
						superClazz2+=di[m];
					}
					/*if(superClazz.get(j).contains("<")&&superClazz.get(j).contains(">")){
						superClazz.get(j).replace("<", "(");
						superClazz.get(j).replace(">",")");
					}*/
					
					gv.addln(superClazz2 + "[label = \""
							+ superClazz.get(j) + "\"];");
					gv.addln("edge[arrowhead=\"onormal\",style=\"filled\"]");// 继承关系
																				
																			
					gv.addln(c.getTempName() + "->" + superClazz2 + ";");

				
				}
				// 关联关系
				for (int j = 0; j < incidenceClassNames.size(); j++) {
						
						if (!haveIncidenceClass.containsKey(incidenceClassNames.get(j))) {
							haveIncidenceClass.put(incidenceClassNames.get(j), 1);
							incidenceClass.add(incidenceClassNames.get(j));
						} else {
							int count = (Integer) haveIncidenceClass
									.get(incidenceClassNames.get(j));
							haveIncidenceClass.remove(incidenceClassNames.get(j));
							count++;
							haveIncidenceClass.put(incidenceClassNames.get(j), count);
						}
					
				}
				for (int j = 0; j < incidenceClass.size(); j++) {
					String di[] = (incidenceClass.get(j)).split("\\.");
					String incidenceClassName = di[0];
					for(int m=1;m<di.length;m++){
						incidenceClassName+=di[m];
					}
					/*if(incidenceClass.get(j).contains("<")&&incidenceClass.get(j).contains(">")){
						incidenceClass.get(j).replace("<", "(");
						incidenceClass.get(j).replace(">",")");
					}*/
					gv.addln(incidenceClassName + "[label = \""
							+ incidenceClass.get(j) + "\"];");
					
					gv.addln("edge[arrowhead=\"open\",color=\"cRIMson\",style=\"filled\",label = \"1.."
							+ haveIncidenceClass.get(incidenceClass.get(j))
							+ "\"]");// 关联关系 ,taillabel =
										// \""+havedependencyClass.get(dependencyClass.get(j))+"\"
					gv.addln(c.getTempName() + "->" + incidenceClassName
							+ "[color=red]" + ";");
					
				}
				// 依赖关系
				for (int j = 0; j < dependencyClassNames.size(); j++) {
					
					if (!haveDependencyClass.containsKey(dependencyClassNames.get(j))) {
						haveDependencyClass.put(dependencyClassNames.get(j), 1);
						dependencyClass.add(dependencyClassNames.get(j));
					} else {
						int count = (Integer) haveDependencyClass
								.get(dependencyClassNames.get(j));
						haveDependencyClass.remove(dependencyClassNames.get(j));
						count++;
						haveDependencyClass.put(dependencyClassNames.get(j), count);
					}
				}
				for (int j = 0; j < dependencyClass.size(); j++) {
					String di[] = (dependencyClass.get(j)).split("\\.");
					String dependencyClassName = di[0];
					for(int m=1;m<di.length;m++){
						dependencyClassName+=di[m];
					}
					/*if(dependencyClass.get(j).contains("<")&&dependencyClass.get(j).contains(">")){
						dependencyClass.get(j).replace("<", "(");
						dependencyClass.get(j).replace(">",")");
					}*/
					gv.addln(dependencyClassName + "[label = \""
							+ dependencyClass.get(j) + "\"];");
					/*if(haveDependencyClass.get(dependencyClass.get(j)).toString().contains("<")&&haveDependencyClass.get(dependencyClass.get(j)).toString().contains(">")){
						haveDependencyClass.get(dependencyClass.get(j)).toString().replace("<", "(");
						haveDependencyClass.get(dependencyClass.get(j)).toString().replace(">",")");
					}*/ 
					
					gv.addln("edge[arrowhead=\"open\",style=\"dashed\",label = \"1.."
							+ haveDependencyClass.get(dependencyClass.get(j))
							+ "\"]");// 依赖关系 ,taillabel =
										// \""+havedependencyClass.get(dependencyClass.get(j))+"\"
					gv.addln(c.getTempName() + "->" +dependencyClassName
							+ "[color=red]" + ";");
				
				}

			}
		}
		gv.addln(gv.end_graph());
		//System.out.println(gv.getDotSource());
		String graphSide=GitController.fRepository.split("\\\\")[2];
		System.out.println(graphSide);
		String type = "pdf";
		File out = new File("F:\\repoGraph\\"+graphSide+"\\class\\class-" + releaseName + "." + type);	
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
       
	}
	
	
/*	public void printPkg(List<Package> pkgs,String commitID,String releaseName) {
	
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("encoding=\"UTF-8\"");
		gv.addln("node[shape=record]");
		ArrayList<String> superPkgs = new ArrayList<String>();
		ArrayList<String> dependencyPkgs = new ArrayList<String>();
		Map havePkgs = new HashMap();
		int count = 0;
		for (int i = 0; i < pkgs.size(); i++) {
			Package p = pkgs.get(i);
			if (!(p.getDependencyPkgs().size() == 0 && p.getInheritancePkgs()
					.size() == 0)) {
				if (p.getSimpleName().equals("graph")) {
					p.setSimpleName("graph1");
				}
				if(p.getPathName()!=null){
					if(!havePkgs.containsKey(p.getPathName())){
						count++;
						havePkgs.put(p.getPath(), count);
					}
				}else if (!havePkgs.containsKey(p.getPath())) {
					count++;
					havePkgs.put(p.getPath(), count);
				}
				if( p.getPathName()!=null){
				gv.addln("subgraph clusterAnimalImpl"
						+ havePkgs.get(p.getPathName()) + " {label=\"Package "
						+ p.getPathName() + "\"" + p.getSimpleName()
						+ " [label = \"" + p.getSimpleName()
						+ "\"]{ rank=same; " + p.getSimpleName() + " }}");
				
				}
				//super 继承关系
				for (int j = 0; j < p.getInheritancePkgs().size(); j++) {
					String ip = p.getInheritancePkgs().get(j);
					if(ip.contains(".")){
					String[] d = ip.split("\\.");
					if (!superPkgs.contains(d[d.length - 1])) {
						int ln = d[d.length - 1].length();
						if (d[d.length - 1].equalsIgnoreCase("graph")) {
							d[d.length - 1] = "graph1";
						}
						if (!havePkgs.containsKey(ip.substring(0, ip.length()
								- ln - 1))) {
							count++;
							havePkgs.put(ip.substring(0, ip.length() - ln - 1),
									count);
						}
						gv.addln("subgraph clusterAnimalImpl"
								+ havePkgs.get(ip.substring(0, ip.length() - ln
										- 1)) + " {label=\"Package "
								+ ip.substring(0, ip.length() - ln - 1) + "\""
								+ d[d.length - 1] + " [label = \""
								+ d[d.length - 1] + "\"]{ rank=same; "
								+ d[d.length - 1] + " }}");
						gv.addln("edge[arrowhead=\"onormal\", style=\"filled\"]");// 继承关系
						gv.addln(p.getSimpleName() + "->" + d[d.length - 1]+ ";");
						
					}
					}else{
						if (ip.equalsIgnoreCase("graph")) {
							ip= "graph1";
						}
						gv.addln("edge[arrowhead=\"onormal\", style=\"filled\"]");// 继承关系
						gv.addln(p.getSimpleName() + "->" + ip+ ";");
						
					}
					
				}
				//依赖关系
				for (int j = 0; j < p.getDependencyPkgs().size(); j++) {
					String dp = p.getDependencyPkgs().get(j);
					if(dp.contains(".")){
					String[] d = dp.split("\\.");
		
					if (!dependencyPkgs.contains(d[d.length - 1])) {
						int ln = d[d.length - 1].length();
						if (d[d.length - 1].equalsIgnoreCase("graph")) {
							d[d.length - 1]= "graph1";
						}
						if (!havePkgs.containsKey(dp.substring(0, dp.length()
								- ln - 1))) {
							count++;
							havePkgs.put(dp.substring(0, dp.length() - ln - 1),
									count);
						}
						
						gv.addln("subgraph clusterAnimalImpl"
								+ havePkgs.get(dp.substring(0, dp.length() - ln
										- 1)) + " {label=\"Package "
								+ dp.substring(0, dp.length() - ln - 1) + "\""
								+ d[d.length - 1] + " [label = \""
								+ d[d.length - 1]+ "\"]{ rank=same; "
								+d[d.length - 1]+ " }}");
						gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");// 依赖关系
						gv.addln(p.getSimpleName() + "->" + d[d.length - 1]
								+ ";");
						}
					}else{
						if (!dependencyPkgs.contains(dp)) {
							if (dp.equalsIgnoreCase("graph")) {
								dp= "graph1";
							}
							gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");// 依赖关系
							gv.addln(p.getSimpleName() + "->" + dp
									+ ";");
							}
					}
				}
			}
			
		}
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		String type = "gif";
		String graphSide=GitController.fRepository.split("\\\\")[2];
		System.out.println(graphSide);
		System.out.println("commitID:" + releaseName);
		File out = new File("F:\\repoGraph\\"+graphSide+"\\package\\package-" + releaseName + "." + type);
		
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);

	}*/
	public void printPkg(List<Package> pkgs,String commitID,String releaseName) {
		
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("encoding=\"UTF-8\"");
		gv.addln("node[shape=record]");
		Set<String> superPkgs = new HashSet<String>();
		Set<String> dependencyPkgs = new HashSet<String>();
		Set<String> allPkgs=new HashSet<String>();
		Map havePkgs = new HashMap();
		int count = 0;
		for (int i = 0; i < pkgs.size(); i++) {
			Package p = pkgs.get(i);
			String trueName=p.getAllPath();
			String lj[]=trueName.split("\\.");
			String allPathName=null;
			for(int q=0;q<lj.length;q++){
				if(allPathName==null){
					allPathName=lj[q];
				}else{
					allPathName+=lj[q];
				}
			}
			//String pathName=allPathName.substring(0, p.getSimpleName().length());
			if (!(p.getDependencyPkgs().size() == 0 && p.getInheritancePkgs()
					.size() == 0)) {
				if (p.getSimpleName().equals("graph")) {
					p.setSimpleName("graph1");
				}
				if(p.getPathName()!=null){
					if(!havePkgs.containsKey(p.getPathName())){
						count++;
						havePkgs.put(p.getPathName(), count);
					}
				}else if (!havePkgs.containsKey(p.getPath())) {
					count++;
					havePkgs.put(p.getPath(), count);
				}
				if( p.getPathName()!=null){
					if(!allPkgs.contains(p)){
						gv.addln("subgraph clusterAnimalImpl"
						+ havePkgs.get(p.getPathName()) + " {label=\"Package "
						+ p.getPathName() + "\"" + allPathName
						+ " [label = \"" + p.getSimpleName()
						+ "\"]{ rank=same; " +allPathName + " }}");
						allPkgs.add(p.getAllPath());
					}
				
				}
				//super 继承关系
				for (int j = 0; j < p.getInheritancePkgs().size(); j++) {
					String ip = p.getInheritancePkgs().get(j);
					if(ip.endsWith(".")){
						String a=ip.substring(0, ip.length()-1);
						ip=a;
					}
					if (ip.contains(".")) {
						String[] d = ip.split("\\.");
						if (!superPkgs.contains(ip)){
							
							int ln = d[d.length - 1].length();
							if (d[d.length - 1].equalsIgnoreCase("graph")) {
								d[d.length - 1] = "graph1";
							}
							String f = ip.substring(0, ip.length() - ln - 1);
							String fp;
							if (f.endsWith(".")) {
								fp = f.substring(0, f.length() - 1);
							} else {
								fp = f;
							}
							System.out.println(fp);
							if (!havePkgs.containsKey(fp)) {
								count++;
								havePkgs.put(fp, count);
							}
							if(!allPkgs.contains(ip)){
							gv.addln("subgraph clusterAnimalImpl"
									+ havePkgs.get(fp) + " {label=\"Package "
									+ fp
									+ "\"" + d[d.length - 1] + " [label = \""
									+ d[d.length - 1] + "\"]{ rank=same; "
									+ d[d.length - 1] + " }}");
							gv.addln("edge[arrowhead=\"onormal\", style=\"filled\"]");// 继承关系
							gv.addln(allPathName + "->" + d[d.length - 1] + ";");
							System.out.println(d[d.length - 1]);
							superPkgs.add(ip);
							allPkgs.add(ip);
							}else{
								gv.addln("edge[arrowhead=\"onormal\", style=\"filled\"]");// 继承关系
								gv.addln(allPathName + "->" + d[d.length - 1] + ";");
								System.out.println(d[d.length - 1]);
								superPkgs.add(ip);
							}
						}
					} else {
						if (ip.equalsIgnoreCase("graph")) {
							ip = "graph1";
						}
						gv.addln("edge[arrowhead=\"onormal\", style=\"filled\"]");// 继承关系
						gv.addln(allPathName + "->" + ip + ";");
						System.out.println(ip);
						if(!allPkgs.contains(ip)){
							allPkgs.add(ip);
						}

					}

				}
				//依赖关系
				for (int j = 0; j < p.getDependencyPkgs().size(); j++) {
					String dp = p.getDependencyPkgs().get(j);
					if(dp.endsWith(".")){
						String a=dp.substring(0, dp.length()-1);
						dp=a;
					}
					if(dp.contains(".")){
					String[] d = dp.split("\\.");
		
					if (!dependencyPkgs.contains(dp)) {
						int ln = d[d.length - 1].length();
						if (d[d.length - 1].equalsIgnoreCase("graph")) {
							d[d.length - 1]= "graph1";
						}
						String f=dp.substring(0, dp.length()- ln - 1);
						String fp;
						if(f.endsWith(".")){
							fp=f.substring(0, f.length()-1);
						}else{
							fp=f;
						}
						
						if (!havePkgs.containsKey(fp)) {
							count++;
							havePkgs.put(fp,count);
						}
						if(!allPkgs.contains(dp)){
						gv.addln("subgraph clusterAnimalImpl"
								+ havePkgs.get(fp) + " {label=\"Package "
								+ fp + "\""
								+ d[d.length - 1] + " [label = \""
								+ d[d.length - 1]+ "\"]{ rank=same; "
								+d[d.length - 1]+ " }}");
						gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");// 依赖关系
						gv.addln(allPathName+ "->" + d[d.length - 1]
								+ ";");
						dependencyPkgs.add(dp);
						allPkgs.add(dp);
						}else{
							gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");// 依赖关系
							gv.addln(allPathName+ "->" + d[d.length - 1]
									+ ";");
							dependencyPkgs.add(dp);
						}
						}
					}else{
						if (!dependencyPkgs.contains(dp)) {
							if (dp.equalsIgnoreCase("graph")) {
								dp= "graph1";
							}
							gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");// 依赖关系
							gv.addln(allPathName + "->" + dp
									+ ";");
							}
					}
				}
			}
			
		}
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		String type = "pdf";
		String graphSide=GitController.fRepository.split("\\\\")[2];
		System.out.println(graphSide);
		System.out.println("commitID:" + releaseName);
		File out = new File("F:\\repoGraph\\"+graphSide+"\\package\\package-" + releaseName + "." + type);
		
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);

	}
	private void addD(Package p,Clazz jj){
			for (int m = 0; m < jj.getDependencyRelationship()
					.getDepandMultiplicity().size(); m++) {
				String dClass=jj.getDependencyRelationship().getDepandMultiplicity().get(m);
				String []s=dClass.split("\\.");
				System.out.println(dClass);
				int len=s.length;
				String dPkg;
				if(dClass.contains("<")&&dClass.contains(">")){
					String a=dClass.substring(0, dClass.indexOf("<")-1);
					System.out.println(a);
					String []temp=a.split("\\.");
					int l=temp.length;
					if(temp.length>1){
					dPkg=a.substring(0, a.length()-temp[l-1].length()-1);
					}else{
						dPkg=a;
					}
				}else{
					if(s.length>1){
					dPkg=dClass.substring(0, dClass.length()-s[len - 1].length());
					}else{
						dPkg=dClass;
					}
				}
				if(dPkg.endsWith(".")){
					dPkg.subSequence(0, dPkg.length()-1);
				}
				if(p.getDependencyPkgs().size()==0){
					p.addDependencyPkg(dPkg);
					p.dependencyEdge.put(dPkg, 1);
					
				}else{
					boolean type=false;
					for (int n = 0; n < p.getDependencyPkgs().size(); n++) {
						if (p.getDependencyPkgs().get(n).contains(dPkg)) {
							type=true;
					}
				}
				if(type==false){
					p.addDependencyPkg(dPkg);
					p.dependencyEdge.put(dPkg, 1);
				}else{
					if(p.dependencyEdge.get(dPkg)!=null){
						int num=p.dependencyEdge.get(dPkg)+1;
						p.dependencyEdge.put(dPkg,num);//jj.getSuperClassName().get(m)
						}else{
							p.addDependencyPkg(dPkg);
							p.dependencyEdge.put(dPkg, 1);
						}
				}
			}}
		
	}
	//添加继承关系
	private void addI(Package p, Clazz jj) {

		for (int m = 0; m < jj.getSuperClassName().size(); m++) {
			String iClass = jj.getSuperClassName().get(m);
			String[] s = iClass.split("\\.");
			int len=s.length;
			String iPkg ;
			System.out.println(iClass);
			if(iClass.contains("<")&&iClass.contains(">")){
				String a=iClass.substring(0, iClass.indexOf("<"));
				System.out.println(a);
				String []temp=a.split("\\.");
				int l=temp.length;
				if(temp.length>1){
					iPkg=a.substring(0, a.length()-temp[l-1].length()-1);
				}else{
					iPkg=a;
				}
			}else{
				if(s.length>1){
					iPkg=iClass.substring(0, iClass.length()-s[len - 1].length());
				}else{
					iPkg=iClass;
				}
			}
			System.out.println(iPkg);
			if(iPkg.endsWith(".")){
				iPkg.subSequence(0, iPkg.length()-1);
			}
			if (p.getInheritancePkgs().size() == 0) {
				p.addInheritancePkg(iPkg);
				p.inheritanceEdge.put(iPkg, 1);
				
			} else {
				boolean type = false;
				for (int n = 0; n < p.getInheritancePkgs().size(); n++) {
					if (p.getInheritancePkgs().get(n).contains(iPkg)) {
						type = true;
					}
				}
				if (type == false) {
					p.addInheritancePkg(iPkg);
					p.inheritanceEdge.put(iPkg, 1);
				}else{
					if(p.inheritanceEdge.get(iPkg)!=null){
					int num=p.inheritanceEdge.get(iPkg)+1;
					p.inheritanceEdge.put(iPkg,num);//jj.getSuperClassName().get(m)
					}else{
					p.addInheritancePkg(iPkg);
						p.inheritanceEdge.put(iPkg, 1);
					}
				}
			}
		}
	}
	
	private void addN(Package p,Clazz jj){
		String pathName = jj.getAllPath();
		//if(pathName.contains(".")){
			
			String pn[] = pathName.split("\\.");
			String simpleName=pn[pn.length - 1];
			
			if(pathName.length()!=simpleName.length()){
			p.setPathName(pathName.substring(0, pathName.length()-simpleName.length()-1));
			p.setSimpleName(simpleName);
			}else{
				p.setPathName(null);
				p.setSimpleName(simpleName);
			}
			p.setPath(jj.getPkg());
			
	}
	private void markChangedClasses(String commitID, CallClazzGraph cg) {
		UnifiedDiffParser diffParser = new UnifiedDiffParser(); 
		List<FileChange> changes = diffParser.parse(fGit.getCommitDiffHunkHeaders(commitID));
		for (FileChange change : changes) {
			String filename = change.getNewFile();
			if (filename != null && filename.endsWith("java")) {
				for (Range changeRange : change.getRanges()) {
					cg.setClazzsAsChanged(fGit.getRepositoryPath() + filename, changeRange.getStart(), changeRange.getEnd());
				}
			}
		}
	}
	
	
	private void insertPackageEdge(String commitID,String releaseName,String startP,String endp,String type,int num){
		PackageEdge packageEdge=new PackageEdge();
		packageEdge.setCommitID(commitID);
		packageEdge.setReleaseName(releaseName);
		packageEdge.setStartP(startP);
		if(endp.endsWith(".")){
			String temp=endp.substring(0, endp.length()-1);
			endp=temp;
		}
		packageEdge.setEndP(endp);
		packageEdge.setType(type);
		packageEdge.setNum(num);

		SQLMethod.insertPackageEdge(packageEdge);
	}
	
	private JProject initJavaProject() {
		JProject project = new JProject();
		
		String[] foldersToIgnrore = {".git"};
		
		JavaJarLocator locator = new JavaJarLocator();
		locator.locate(new File(fGit.getRepositoryPath()), foldersToIgnrore, fIgnoreFolderPattern);
		project.classPath = locator.getJarFiles();
		project.sourcePath = locator.getJavaFilePaths();
		project.javaFiles = locator.getJavaFiles();
		
		return project;
	}
}
