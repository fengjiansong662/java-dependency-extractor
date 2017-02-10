package fjs.jde.io.output.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import jde.entity.Dependency;
import jde.entity.GitObject;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.screencap.Dump;
import edu.uci.ics.screencap.EPSDump;

public abstract class JungGraphVisualizer {
	
	protected String commitID;
	protected Graph mGraph;
	protected Dimension DIMENSION = new Dimension(1100, 900);
	//KKLayout, CircleLayout, DAGLayout, FRLayout, ISOMLayout, SpringLayout, StaticLayout
	
	public JungGraphVisualizer(String commitID) {
		this.commitID = commitID;
	}
	
	public Collection getEdges(){
		return this.mGraph.getEdges();
	}
	
	public Collection getNodes(){
		return this.mGraph.getVertices();
	}
	
	public int getNodeCount() {
		return this.mGraph.getVertexCount();
	}

	public int getEdgeCount() {
		return this.mGraph.getEdgeCount();
	}
	
	public Pair getEndpoints(Object edge) {
		return this.mGraph.getEndpoints(edge);
	}
	
	public void show(boolean showVertexLabel, boolean showEdgeLabel, String jungGraphLayout, String dir) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		try {
			visualize(jungGraphLayout, showVertexLabel, showEdgeLabel, dir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void visualize(String layoutType, boolean showVertexLabel, boolean showEdgeLabel, String dir) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
		
		Layout<String, String> layout = createLayout (layoutType);
		layout.setSize(DIMENSION);
        
        EditingGraphViewer sgv = new EditingGraphViewer();
        VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout);
        vv.setPreferredSize(DIMENSION);
        if(showVertexLabel) {
        	//2015-07-08 简化节点文字的显示内容
        	//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        
	        vv.getRenderContext().setVertexLabelTransformer(new Transformer<String, String>() {
				public String transform(String input) {
					int length = input.length();
					if(length<4)
						return input;
					else {
						return input.substring(0,4) + "...";
					}
				}
	        });
        }
        if(showEdgeLabel)
        	vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setLabelOffset(20);
        vv.setBackground(Color.white);
        
        EditingModalGraphMouse gm = new EditingModalGraphMouse(vv.getRenderContext(), 
                sgv.vertexFactory, sgv.edgeFactory); 
        vv.setGraphMouse(gm);
        
		JFrame frame = new JFrame(commitID);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.white);
		frame.getContentPane().add(vv); 
		
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = gm.getModeMenu();
        modeMenu.setText("Selection");
        modeMenu.setIcon(null);
        modeMenu.setPreferredSize(new Dimension(80,20)); 
        menuBar.add(modeMenu);
        
        frame.setJMenuBar(menuBar);        
        gm.setMode(ModalGraphMouse.Mode.PICKING); 
		frame.pack();
		frame.setVisible(true);   
		
		Dump dumper = new EPSDump(); 
		dumper.dumpComponent(new File(dir), frame.getContentPane()); 
	}
	
	private Layout<String, String> createLayout(String layoutType) 
			throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
			IllegalArgumentException, InstantiationException, IllegalAccessException, 
			InvocationTargetException {
		
		Layout<String, String> layout = null;

		Class<?> clazz = Class.forName("edu.uci.ics.jung.algorithms.layout."+layoutType);
		Constructor<?> ctor = clazz.getConstructor(Graph.class);
		layout = (Layout<String, String>) ctor.newInstance(mGraph);
		
		return layout;
	}

	protected void addVertexToGraph(GitObject obj) {
		boolean exist = false;
		//先要到mGraph里去找是不是已经存在这个节点了，若是，则直接使用，避免重复
		Collection<GitObject> exists = mGraph.getVertices();
		for(GitObject e : exists) {
			if(e.getDistinctName().equals(obj.getDistinctName())) { 
				exist = true;
				break;
			}
		}
		if( !exist) 
			mGraph.addVertex(obj);
	}

	protected void addEdgeToGraph(GitObject start, GitObject end, GitObject invoke) {
		
		Dependency dp = findExistedEdge(start, end);
		if (dp == null) {
			dp = new Dependency(start, end, invoke);
			mGraph.addEdge(dp, start, end, EdgeType.DIRECTED);
		} else {
			dp.setWeight(dp.getWeight() + 1);
		}
	}

	protected Dependency findExistedEdge(GitObject prevStart, GitObject prevEnd) {
		Collection<Dependency> edges = mGraph.getEdges();
		for (Dependency edge : edges) {
			if (edge.getStartNode().getDistinctName().equals(prevStart.getDistinctName()) 
					&& edge.getEndNode().getDistinctName().equals(prevEnd.getDistinctName())) {
				return edge;
			}
		}

		return null;
	}
	
	protected GitObject findExistedNode(GitObject cs) {
		Collection<GitObject> jss = mGraph.getVertices();
		for (GitObject js : jss) {
			if (js.getDistinctName().equals(cs.getDistinctName()))
				return js;
		}

		return cs;
	}
}

class EditingGraphViewer {
	Graph<Integer, String> g;
	int nodeCount, edgeCount;
	Factory<Integer> vertexFactory;
	Factory<String> edgeFactory;

	/** Creates a new instance of SimpleGraphView */
	public EditingGraphViewer() {
		// Graph<V, E> where V is the type of the vertices and E is the type of
		// the edges
		g = new SparseMultigraph<Integer, String>();
		nodeCount = 0;
		edgeCount = 0;
		vertexFactory = new Factory<Integer>() { // My vertex factory
			public Integer create() {
				return nodeCount++;
			}
		};
		edgeFactory = new Factory<String>() { // My edge factory
			public String create() {
				return "E" + edgeCount++;
			}
		};
	}
}