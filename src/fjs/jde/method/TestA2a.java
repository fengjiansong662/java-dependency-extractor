package fjs.jde.method;

import java.util.Map;

import as.jde.main.ArgumentParser;
import as.jde.scm.git.GitController;

public class TestA2a {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//F:\repo\failsafe\
		ArgumentParser argumentParser = new ArgumentParser();
		Map<String,String> optArgs = argumentParser.parseArguments(args);
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		ChangeEdges.printChangeEdges(gc);
		A2a.printA2a(gc);
		P2p.printP2p(gc);
	}

}
