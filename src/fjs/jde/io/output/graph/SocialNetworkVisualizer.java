package fjs.jde.io.output.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fjs.jde.io.output.Debug;
import as.jde.util.Utils;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class SocialNetworkVisualizer extends JungGraphVisualizer {
	/**
	 * 
	 * ���������ӻ�����
	 * @author fengjiansong
	 *
	 */
	
	class EdgeWithData {
		private String label;
		private double weight;
		public EdgeWithData(String label) {
			this.label = label;
		}
		public String toString() {
			return this.label;
		}
		public EdgeWithData(String label, double weight) {
			this.label = label;
			this.weight = weight;
		}
	}
	
	public SocialNetworkVisualizer(String commitID) {
		super(commitID);
	}
	
	public void createDirectedVisualGraph(HashMap<String[], double[]> pairs, int labelFlag) {
		
		this.mGraph = new DirectedSparseMultigraph<String, EdgeWithData>();
		
		for(String[] pair : pairs.keySet()) {
			
			double numeric = pairs.get(pair)[labelFlag];		
			
			//���Ȩ��Ϊ0����Ҫ��������
			if(numeric - 0.0 == 0) continue;
			
			Collection<String> nodes = mGraph.getVertices();
			if( ! nodes.contains(pair[0]))
				mGraph.addVertex(pair[0]);
			if( ! nodes.contains(pair[1]))
				mGraph.addVertex(pair[1]);
			
			String label = "";
			if(labelFlag == 1) {
				NumberFormat formatter = new DecimalFormat("#0.00");						 
				label = formatter.format(numeric); 
			}
			else {
				NumberFormat formatter = new DecimalFormat("#0");						 
				label = formatter.format(numeric); 
			}
			
			EdgeWithData edge = new EdgeWithData(label, numeric);
			mGraph.addEdge(edge, pair[1], pair[0], EdgeType.DIRECTED);
			
		}				
	}

	public void createUndirectedVisualGraph2(HashMap<String[], Integer> pairs) {
		
		this.mGraph = new UndirectedSparseMultigraph<String, EdgeWithData>();
		for(String[] pair : pairs.keySet()) {
			
			Collection<String> nodes = mGraph.getVertices();
			if( ! nodes.contains(pair[0]))
				mGraph.addVertex(pair[0]);
			if( ! nodes.contains(pair[1]))
				mGraph.addVertex(pair[1]);
					
			String label = pairs.get(pair).toString();
			
			EdgeWithData edge = new EdgeWithData(label, pairs.get(pair).doubleValue());
			mGraph.addEdge(edge, pair[0], pair[1], EdgeType.UNDIRECTED);
			
		}		
	}

	public void createUndirectedVisualGraph(HashMap<String[], double[]> pairs) {
		
		this.mGraph = new UndirectedSparseMultigraph<String, EdgeWithData>();
		for(String[] pair : pairs.keySet()) {
			
			Collection<String> nodes = mGraph.getVertices();
			if( ! nodes.contains(pair[0]))
				mGraph.addVertex(pair[0]);
			if( ! nodes.contains(pair[1]))
				mGraph.addVertex(pair[1]);
					
			double [] weight = pairs.get(pair); 
			String label = Double.valueOf(weight[0]).toString();
			if(weight.length > 1)
				label += "("+weight[1]+")";
			
			EdgeWithData edge = new EdgeWithData(label, weight[0]);
			mGraph.addEdge(edge, pair[0], pair[1], EdgeType.UNDIRECTED);
			
		}		
	}
	private static ArrayList<EdgeWithData> edgeExist(EdgeWithData e, Collection<EdgeWithData> edges, 
			SocialNetworkVisualizer dsn1, SocialNetworkVisualizer dsn2) {
		
		ArrayList<EdgeWithData> result = new ArrayList<EdgeWithData>(); 
		             
		Pair ends2 = dsn2.getEndpoints(e);
		String end21 = (String) ends2.getFirst();
		String end22 = (String) ends2.getSecond();
		
		int i=0;
		for(EdgeWithData e1 : edges) {
			if(i==2) break;
			Pair<String> ends1 = dsn1.getEndpoints(e1);
			String end11 = (String) ends1.getFirst();
			String end12 = (String) ends1.getSecond();	
			//��ʱ�����Ǳߵķ���
			if((end11.contentEquals(end21) && end12.contentEquals(end22)) || 
					(end11.contentEquals(end21) && end12.contentEquals(end22))) {
				result.add(e1);
				i++;
			}
		}
		if(i==0) result = null;
		
		return result;
	}
	
	public static double[] compareTwoDSNs(SocialNetworkVisualizer dsn1, SocialNetworkVisualizer dsn2) {
		
		//���ؽ��
		double[] stat = new double[8];
		//0 ��ͬ�ڵ�ı���
		//1 DSN1�в���ͬ�ı���
		//2 DSN2�в���ͬ�ı���
		//3 ��ͬ�ߵı���
		//4 DSN1�в���ͬ�ı���
		//5 DSN2�в���ͬ�ı���		
		//6 ��ͬ�ı������ɵ�Ȩ��������spearman���ϵ��
		//7 ��ͬ�ı������ɵ�Ȩ��������spearman p-value
		
		//�õ�����DSN�Ľڵ���Ŀ
		int numNode1 = dsn1.getNodeCount();
		int numNode2 = dsn2.getNodeCount();		
		Collection<String> nodes1 = dsn1.getNodes();
		Collection<String> nodes2 = dsn2.getNodes();		
		
		//�õ�����DSN�ı�
		Collection<EdgeWithData> edges1 = dsn1.getEdges();
		Collection<EdgeWithData> edges2 = dsn2.getEdges();
		//DSN1������ͼ���ʼ���õ�������ߵ���Ŀ������ߵ���Ŀ������������ĺ���һ����һ���ߣ�
		int numDirectedEdge1 = dsn1.getEdgeCount();
		int numUndirectedEdge1 = getUndirectedEdgeCount(edges1, dsn1);
		int numEdge2 = dsn2.getEdgeCount();
		
		//�õ�������ͬ�Ľڵ����ͬ�ı�
		ArrayList<String> sameNodes = new ArrayList<String>();
		//keyΪDSN1�а�����1������������ߣ�valueΪDSN2�а����ıߣ����ߵĽڵ����ͬ
		HashMap<ArrayList<EdgeWithData>, EdgeWithData> sameEdges = new HashMap<ArrayList<EdgeWithData>, EdgeWithData>();
		
		for(String n : nodes1) {
			if(nodes2.contains(n)) 
				sameNodes.add(n);
		}
		for(EdgeWithData e2 : edges2) {
			ArrayList<EdgeWithData> e1s = edgeExist(e2, edges1, dsn1, dsn2);
			if(e1s != null)
				sameEdges.put(e1s, e2);
		}
		
		//�õ�������ͬ�ߵ�Ȩ������
		int numSameEdges = sameEdges.size();
		if(numSameEdges == 0 || numSameEdges == 1)
			return null;
		
		double[] weight1 = new double[numSameEdges];
		double[] weight2 = new double[numSameEdges];
		
		int count = 0;
		for(ArrayList<EdgeWithData> e1 : sameEdges.keySet()) {
			EdgeWithData e2 = sameEdges.get(e1);
			double totalWeight1 = 0;
			for(EdgeWithData e11 : e1) 
				totalWeight1 += e11.weight;
			weight1[count] = totalWeight1;
			weight2[count] = e2.weight;
			count++;
		}
		
		//����spearman�����������ϵ����p-value
		double[] spearman = Utils.spearmanCorrelationPValue(weight1, weight2);
		Debug.pl(weight1);Debug.pl(weight2);
		
		//�����ܵĽڵ���
		int totalNodes = numNode1+numNode2-sameNodes.size();
		stat[0] = sameNodes.size() * 1.0 / totalNodes;
		stat[1] = (numNode1 - sameNodes.size()) * 1.0 / numNode1;
		stat[2] = (numNode2 - sameNodes.size()) * 1.0 / numNode2;
		
		//�����ܵı���
		int totalEdges = numUndirectedEdge1+numEdge2-numSameEdges;
		stat[3] = sameEdges.size() * 1.0 / totalEdges;
		stat[4] = (numUndirectedEdge1 - numSameEdges) * 1.0 / numUndirectedEdge1;
		stat[5] = (numEdge2 - numSameEdges) * 1.0 / numEdge2;
		
		//���ϵ��
		stat[6] = spearman[0];
		stat[7] = spearman[1];
		
		return stat;
	}

	private static boolean sameEdge(String n1, String n2, ArrayList<String[]> edges) {
		for(String[] e : edges) {
			if((e[0].contentEquals(n1) && e[1].contentEquals(n2)) || 
					(e[0].contentEquals(n2) && e[1].contentEquals(n1))) 
				return true;
		}
		return false;
	}
	
	private static int getUndirectedEdgeCount(Collection<EdgeWithData> edges, SocialNetworkVisualizer dsn) {
		
		ArrayList<String[]> undirected = new ArrayList<String[]>();
		
		for(EdgeWithData e : edges) {
			Pair<String> ends = dsn.getEndpoints(e);
			String end1 = (String) ends.getFirst();
			String end2 = (String) ends.getSecond();	
			if(sameEdge(end1, end2, undirected)) continue;
			else
				undirected.add(new String[]{end1, end2});
		}
		return undirected.size();
	}
	

	public static SocialNetworkVisualizer recoverDSNFromFile(String file, String direct, int flag) throws IOException {
		
		HashMap<String[], double[]> pairs = new HashMap<String[], double[]> ();
		HashMap<String[], Integer> pairsTopicDSN = new HashMap<String[], Integer> ();

		SocialNetworkVisualizer dsn = new SocialNetworkVisualizer(null);
		
		BufferedReader br = new BufferedReader(new FileReader(file)) ;
        String line = br.readLine();        
        while (line != null) {		        	
        	String[] splits = line.split("\t");
        	if(splits.length >= 3){
        		double w = Double.valueOf(splits[2]);
        		if(w - 0.0 >= 0.005) {
	        		String[] nodePair = new String[] {splits[0], splits[1]};
		        	double[] weights = new double[] {0,0,0};
		        	if(flag >= 0) {
		        		weights[flag] = w;
			        	pairs.put(nodePair, weights);
		        	}
		        	else if (flag == -2){
		        		int weight = (int) w;		        		
		        		pairsTopicDSN.put(nodePair, Integer.valueOf(weight));
		        	}		  
		        	else if (flag == -1) {
		        		weights[0] = w;
		        		weights[1] = Double.valueOf(splits[3]);	
		        		pairs.put(nodePair, weights);
		        	}
	        	}
        	}
            line = br.readLine();
        }
        br.close();			
        
        if(direct.equals("D"))
        	dsn.createDirectedVisualGraph(pairs, flag);
        else if (flag == -2)
        	dsn.createUndirectedVisualGraph2(pairsTopicDSN);      
        else if(flag == -1)
        	dsn.createUndirectedVisualGraph(pairs);
        
        return dsn;
   	}

	public static HashMap<String, Double> analyzeDeveloperScore(
			SocialNetworkVisualizer dsn) {
		
		HashMap<String, Double> result = new HashMap<String, Double> ();
		
		Graph<String, EdgeWithData> g = dsn.mGraph;
		
		//����������߽ڵ����Ҫ��
		DegreeScorer ds = new DegreeScorer(g);
		Collection<String> nodes = dsn.getNodes();
		for(String dev : nodes){
			int degreeScore = ds.getVertexScore(dev);
			result.put(dev, new Double(degreeScore));
		}	
		
		return result;
	}
	
	public static Set<Set<String>> analyzeCommunity (SocialNetworkVisualizer dsn) {

		Graph<String, EdgeWithData> g = dsn.mGraph;

		//��������
		EdgeBetweennessClusterer<String, EdgeWithData> clusterer = new EdgeBetweennessClusterer<String, EdgeWithData>(1);
		Set<Set<String>> clusterSet = clusterer.transform(g);
		List<EdgeWithData> edges = clusterer.getEdgesRemoved();
		
		Debug.pl("number of clusters: "+ clusterSet.size());
		Debug.pl("number of removed edges: "+ edges.size());
		
		for (Iterator<Set<String>> cIt = clusterSet.iterator(); cIt.hasNext();) {			 
			Set<String> vertices = cIt.next();				
			//for(String dev: vertices) {}
		}		 
		// for (EdgeWithData e : edges) {}
		
		return clusterSet;
	}
}
