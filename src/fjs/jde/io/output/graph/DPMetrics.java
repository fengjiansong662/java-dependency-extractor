package fjs.jde.io.output.graph;

import ged.editpath.CostLimitExceededException;
import ged.editpath.EditPath;
import ged.editpath.NodeEditPath;
import ged.editpath.editoperation.EditOperation;
import ged.graph.DecoratedNode;
import ged.graph.DotParseException;
import ged.processor.CostContainer;
import ged.processor.InputContainer;
import ged.processor.OutputContainer;
import ged.processor.Processor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import att.grappa.Graph;

public class DPMetrics {
	
	public static Graph mGraph1;
	public static Graph mGraph2;
	
	
	public static void main(String[] args) throws ClassNotFoundException, DotParseException, CostLimitExceededException {
		
		Map<String, Double> posEditWeights = null;
		Map<String, Double> deprelEditWeights = null;
		//需要构造出这两个数组
		//以及从JUNG的图转为GED里自定义的两个图
		
		CostContainer cc = new CostContainer();
		cc.setAcceptanceLimitCost(null);
		cc.setEdgeDeletionCost(new BigDecimal(1));
		cc.setEdgeInsertionCost(new BigDecimal(1));
		cc.setNodeDeletionCost(new BigDecimal(1));
		cc.setNodeInsertionCost(new BigDecimal(1));
		cc.setNodeSubstitutionCost(new BigDecimal(1));
		
		String graph1 = "digraph from {\n" +
				"a -> a;\n"+
			    "a -> b1;\n"+
			    "a -> c1;\n"+
			    "b1 -> c1;\n"+
			    "c1 -> d1;\n"+
			    "d1 -> a;\n"+
			    "f -> b1;" +
			    "}";
		
		String graph2 = "digraph to {\n"+
			    "a -> b2;\n"+
			    "a -> d2;\n"+
			    "b2 -> c2;\n"+
			    "c2 -> a;\n"+
			    "d2 -> e1;\n"+
			    "e1 -> b2;\n"+
			    "}";
		
		InputContainer ic = new InputContainer(cc, graph1, graph2);
		OutputContainer oc = Processor.process(ic);
		EditPath ep = oc.getEditPath();
		Graph graph = oc.getGraph();	
		
		List<NodeEditPath> neps = ep.getNodeEditPaths();
		double totalCost = 0;
		for(NodeEditPath nep : neps) {
			BigDecimal cost = nep.getCost();
			DecoratedNode from = nep.getFrom();
			DecoratedNode to = nep.getTo();
			totalCost += cost.doubleValue();
			System.out.println("from " +from.getLabel()+" to "+ to.getLabel()+", with cost "+cost.toPlainString());
			System.out.println("   including the following edit operations:");
			List<EditOperation> eops = nep.getEditOperations();
			for(EditOperation eop : eops) {
				System.out.println("      "+eop.toString()+" with cost "+eop.getCost());
			}
		}
		System.out.println("total edit cost is: " + totalCost);


		//
	}
	/*
	getDistance(mGraph1, mGraph2, posEditWeights, deprelEditWeights);
	
	public static void getDistance(Graph g1, Graph g2,  Map<String,Double> posEditWeights, Map<String,Double> deprelEditWeights)  {
		
		GraphEditDistance ged = new GraphEditDistance(g1, g2, posEditWeights, deprelEditWeights);

		System.out.println("Calculating graph edit distance for the two sentences:");
		System.out.println("Distance between the two sentences: "+ged.getDistance()+". Normalised: "+ged.getNormalizedDistance());
		System.out.println("Edit path:");
		for(String editPath : getEditPath(g1, g2, ged.getCostMatrix(), true)) {
			System.out.println(editPath);
		}
	}

	public static String[] getInputTexts(String[] args)  {
		String text1="", text2="";
		if(args.length!=2) {
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			try {
				System.out.println("Please enter the first sentence: ");
				text1 = in.readLine();

				System.out.println("Please enter the second sentence: ");
				text2 = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			return args;
		}

		return new String[] {text1, text2};
	}

	public static List<String> getEditPath(Graph g1, Graph g2, double[][] costMatrix, boolean printCost) {
		return getAssignment(g1, g2, costMatrix, true, printCost);
	}

	public static List<String> getFreeEdits(Graph g1, Graph g2, double[][] costMatrix) {
		return getAssignment(g1, g2, costMatrix, false, false);
	}

	public static List<String> getAssignment(Graph g1, Graph g2, double[][] costMatrix, boolean editPath, boolean printCost) {
		List<String> editPaths = new ArrayList<String>();
		int[][] assignment = HungarianAlgorithm.hgAlgorithm(costMatrix, "min");

		for (int i = 0; i < assignment.length; i++) {
			String from = getEditPathAttribute(assignment[i][0], g1);
			String to = getEditPathAttribute(assignment[i][1], g2);

			double cost = costMatrix[assignment[i][0]][assignment[i][1]];
			if(cost != 0 && editPath) {
				if(printCost) {
					editPaths.add("("+from+" -> "+to+") = "+cost);
				}
			}else if(cost == 0 && !editPath) {
				editPaths.add("("+from+" -> "+to+")");
			}
		}

		return editPaths;

	}

	private static String getEditPathAttribute(int nodeNumber, Graph g) {
		if(nodeNumber < g.getNodes().size()) {
			Node n= g.getNode(nodeNumber);
			return n.getLabel();
		}else {
			return "ε";
		}
	}
	
	public static boolean shouldContinue() {
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		System.out.println("continue? [y/n]");
		try {
			String answer = in.readLine();
			return (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
*/
}
