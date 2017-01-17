package fjs.jde.main;

//import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fjs.jde.graph.CallClazzGraph;

import as.jde.graph.CallGraph;
import as.jde.output.ThreadedOutput;
import as.jde.scm.git.GitController;

public class CallClazzGraphExtractor {//调用图提取器

	private GitController gitController;
	private SCMIterator scmIterator;
	//private ThreadedOutput outputter;
	
	
	public void setGitController(GitController gitController) {
		this.gitController = gitController;
	}

	public void setSCMIterator(SCMIterator scmIterator) {
		this.scmIterator = scmIterator;
	}

	/*public void setOutputter(ThreadedOutput outputter) {
		this.outputter = outputter;
	}*/

	public void extract() throws IOException, InterruptedException {
		/*outputter.start(gitController.getRepositoryPath().
				split("\\\\")
				[gitController.getRepositoryPath().
				 split("\\\\").length-2]);//File.separator
*/		while (scmIterator.hasNext()) {
			try {
				CallClazzGraph ccg = scmIterator.next();
			//	outputter.add(ccg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//outputter.stop();
	}
	//下载远程仓库时调用1    修改前的
	/*	public void extract1(String[] repo) throws IOException, InterruptedException {
				while (scmIterator.hasNext()) {
				try {
					CallClazzGraph ccg = scmIterator.next(repo[0]);
					 scmIterator.next(repo[0]);
					//outputter.add(ccg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	
			//outputter.stop();
		}*/
		
		
		
		//下载远程仓库时调用2只git clone
		public void extract2(String[] repo) throws IOException, InterruptedException {
				try {
					 scmIterator.next(repo[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

}
