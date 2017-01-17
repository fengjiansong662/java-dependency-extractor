package as.jde.main;

//import java.io.File;
import java.io.IOException;

import as.jde.graph.CallGraph;
import as.jde.output.ThreadedOutput;
import as.jde.scm.SCMIterator;
import as.jde.scm.git.GitController;

public class CallGraphExtractor {//调用图提取器

	private GitController gitController;
	private SCMIterator scmIterator;
	private ThreadedOutput outputter;

	public void setGitController(GitController gitController) {
		this.gitController = gitController;
	}

	public void setSCMIterator(SCMIterator scmIterator) {
		this.scmIterator = scmIterator;
	}

	public void setOutputter(ThreadedOutput outputter) {
		this.outputter = outputter;
	}

	public void extract() throws IOException, InterruptedException {
		outputter.start(gitController.getRepositoryPath().split("\\\\")
				[gitController.getRepositoryPath().split("\\\\").length-2]);//File.separator
		while (scmIterator.hasNext()) {
		
			try {
				CallGraph cg = scmIterator.next();
				outputter.add(cg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		outputter.stop();
	}

}
