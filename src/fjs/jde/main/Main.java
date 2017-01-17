package fjs.jde.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.hibernate.Query;

import fjs.jde.ast.ASTClazzVisitor;
import fjs.jde.graph.CallClazzGraph;
import fjs.jde.model.Clazz;
import fjs.jde.model.DynamicQueryTest;
import fjs.jde.sql.SQLMethod;

import as.jde.main.ArgumentParser;
import as.jde.main.input.HelpArgument;
import as.jde.main.input.IgnoreFolderArgument;
import as.jde.main.input.QueueLimitArgument;
import as.jde.output.ThreadedOutput;
import as.jde.output.XMLOutput;
import as.jde.scm.git.GitController;


public class Main {
	/**
	 * 分析版本主方法
	 * @param F:\repo\java-dependency-extractor\
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, InterruptedException {
		
		ArgumentParser argumentParser = new ArgumentParser();
		Map<String,String> optArgs = argumentParser.parseArguments(args);
		
		if (helpFlagPresent(optArgs)) {
			argumentParser.printHelp();
			return;
		}
		
		Writer stdout = new OutputStreamWriter(System.out);
		//XMLOutput outputter = new XMLOutput(stdout);
		//ThreadedOutput out = new ThreadedOutput(outputter, 10000);
		//ThreadedOutput out = new ThreadedOutput(outputter, Integer.parseInt(optArgs.get(QueueLimitArgument.OPT_QUEUE_LIMIT)));
		//System.out.println(Integer.parseInt(optArgs.get(QueueLimitArgument.OPT_QUEUE_LIMIT)));
		CallClazzGraphExtractor extractor = new CallClazzGraphExtractor();
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		System.out.println(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		extractor.setGitController(gc);
		extractor.setSCMIterator(new SCMIterator(gc, optArgs.get(IgnoreFolderArgument.OPT_IGNORE_FOLDER)));
		//extractor.setOutputter(out);
		
		extractor.extract();	
		
		SQLMethod.insertClazzEdegs(ASTClazzVisitor.clazzEdges);
		
		stdout.close();
	}

	/**
	 * 下载远程仓库主方法
	 * @param https://github.com/square/cascading2-protobufs.git
	 * @return 一个远程仓库
	 */
	/*public static void main(String[] args) throws ParserConfigurationException, TransformerException, IOException, InterruptedException {
		
		String[] args1=new String[]{"F:\\repo\\"};
		ArgumentParser argumentParser = new ArgumentParser();
		
		Map<String,String> optArgs = argumentParser.parseArguments(args1);
		
		if (helpFlagPresent(optArgs)) {
			argumentParser.printHelp();
			return;
		}
		
		Writer stdout = new OutputStreamWriter(System.out);
		XMLOutput outputter = new XMLOutput(stdout);
		ThreadedOutput out = new ThreadedOutput(outputter, 100000);
		//ThreadedOutput out = new ThreadedOutput(outputter, Integer.parseInt(optArgs.get(QueueLimitArgument.OPT_QUEUE_LIMIT)));
		//System.out.println(Integer.parseInt(optArgs.get(QueueLimitArgument.OPT_QUEUE_LIMIT)));
		CallClazzGraphExtractor extractor = new CallClazzGraphExtractor();
		GitController gc = new GitController(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		System.out.println(optArgs.get(ArgumentParser.OPT_REPOSITORY_LOCATION));
		extractor.setGitController(gc);
		extractor.setSCMIterator(new SCMIterator(gc, optArgs.get(IgnoreFolderArgument.OPT_IGNORE_FOLDER)));
		//extractor.setOutputter(out);
		extractor.extract2(args);	
		//extractor.extract1(args);	//修改前的
		
		stdout.close();
	}
*/
	private static boolean helpFlagPresent(Map<String, String> optArgs) {
		return !optArgs.get(HelpArgument.OPT_HELP).equals(HelpArgument.HELP_FALSE);
	}
}
