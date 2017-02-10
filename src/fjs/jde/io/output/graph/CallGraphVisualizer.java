package fjs.jde.io.output.graph;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import as.jde.graph.CallGraph;
import jde.entity.Dependency;
import fjs.jde.model.Clazz;
import fjs.jde.model.JavaFile;
import as.jde.graph.Method;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class CallGraphVisualizer extends JungGraphVisualizer {
	
	public CallGraphVisualizer(String commitID) {
		super(commitID);
	}

	public void createVisualGraph(CallGraph cg, String depType, boolean showOuter) throws IOException {
		
		if(depType.equals("method"))
			mGraph = createMethodGraph(cg, showOuter);
		else if(depType.equals("class"))
			mGraph = createClassGraph(cg, showOuter);
		else if(depType.equals("file"))
			mGraph = createFileGraph(cg, showOuter);
		
		/*���´�����ͼ��JUNG��ͼתΪGED�����ͼ��ʹ��JAR������GED�㷨��
		 * ��Ŀǰ���ٶ�ִ��̫���������У�GED����һ������·��
		System.out.println("-----"+cg.getCommit().commitID);
		String graph = "digraph from {\n";
		Collection <Dependency> dps = mGraph.getEdges();
		for(Dependency p : dps) {
			graph += p.getStartNode().toString() + " -> " + p.getEndNode().toString()+";\n";
		}
		graph += "}";
		System.out.println(graph);
		System.in.read();
		*/		
	}
	
	//Ϊfile֮���������ϵ����JUNG graph
	//Ŀǰ��˼·������Ǵ�ģ��ƺ�ֻ���ļ��еķ��������������ļ��ķ������Ż��й�ϵ��
	//ʵ���ϣ�����ֻ�з���֮��������ž������ļ���������
	private Graph createFileGraph(CallGraph cg, boolean showOuter) {
		
		mGraph = new DirectedSparseMultigraph<JavaFile, Dependency>();
		
		List<JavaFile> files = cg.getFiles();
		for(JavaFile jf : files) {
			addVertexToGraph(jf);
		}
		
		Collection<Method> methods = cg.getMethods().values();
			
		for (Method m : methods) {
			if( !showOuter && m.getFile() == "")
				continue;
			
			JavaFile jf1 = new JavaFile(m.getFile(), "");
			jf1 = (JavaFile) findExistedNode(jf1);

			List<Method> callees = cg.getCalledMethods(m);
			if (callees != null)
				for (Method c : callees) {
					if( !showOuter && c.getFile() == "")
						continue;
					
					if (c.getFile() != "" && c.getFile() != m.getFile()) {
						JavaFile jf2 = new JavaFile(c.getFile(),"");
						jf2 = (JavaFile) findExistedNode(jf2);

						addEdgeToGraph(jf1,jf2,c);
					}
				}
			else 
				addVertexToGraph(jf1);
		}

		return mGraph;		
	}

	//Ϊclass֮���������ϵ����JUNG graph
	private Graph createClassGraph(CallGraph cg, boolean showOuter) {
		this.mGraph = new DirectedSparseMultigraph<JavaClass, Dependency>();
		
		Collection<Method> methods = cg.getMethods().values();
		
		for (Method m : methods) {
			JavaClass class1 = new JavaClass(m.getClazz());
			
			if(!showOuter && m.getFile() == "")
				continue;
			
			class1 = (JavaClass) findExistedNode(class1);
			
			List<Method> callees = cg.getCalledMethods(m);
			if (callees != null) {
				for (Method c : callees) {
					JavaClass class2 = new JavaClass(c.getClazz());		
					
					if(!showOuter && c.getFile() == "")
						continue;
					
					class2 = (JavaClass) findExistedNode(class2);
					
					addEdgeToGraph(class1,class2,c);				
				}
			}
			else
				addVertexToGraph(class1);
		}
		
		return mGraph;		
	}

	//����callGraph����jung graph
	private Graph createMethodGraph(CallGraph cg, boolean showOuterMethod) {

		this.mGraph = new DirectedSparseMultigraph<Method, Dependency>();

		Collection<Method> methods = cg.getMethods().values();
		for (Method m : methods) {
			//�Ƿ���Ҫչʾ�ⲿ����
			if(!showOuterMethod && (m.getStart() == -1 || m.getEnd() == -1 || m.getFile() == ""))
				continue;
			
			mGraph.addVertex(m);
		}	
		
		for (Method m : cg.getInvokes().keySet()) {
			if(!showOuterMethod && m.getStart() == -1 && m.getEnd() == -1)
				continue;
			List<Method> callees = cg.getCalledMethods(m);
			if (callees != null)
				for (Method c : callees) {	
					if(!showOuterMethod && c.getStart() == -1 && c.getEnd() == -1)
						continue;
					addEdgeToGraph(m,c,c);
				}
		}

		return mGraph;
	}
}
