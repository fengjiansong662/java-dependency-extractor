package fjs.jde.method;

import java.util.Map;

import as.jde.main.ArgumentParser;
import as.jde.scm.git.GitController;

public class TestChangeEdges {

	
	public static void main(String[] args) {
		
		ArgumentParser argumentParser = new ArgumentParser();
		Map<String,String> optArgs = argumentParser.parseArguments(args);
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		ChangeEdges.printChangeEdges(gc);
	}

}
