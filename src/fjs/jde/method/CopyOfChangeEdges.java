package fjs.jde.method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fjs.jde.graph.CallClazzGraph;
import fjs.jde.model.Clazz;

public class CopyOfChangeEdges {
	public static Map<String,Clazz> oldClazzs=new HashMap<String, Clazz>();
	public static List<Integer>GetChangeEdges(String commitID,CallClazzGraph ccg){
		List<Integer> changeEdges=new ArrayList<Integer>();
		int addE=0;
		int remE=0;
		int addNE=0;//从空集到目前版本中添加的边
		Map<String,Clazz> tempClazzs=new HashMap<String, Clazz>();
		if (oldClazzs != null) {
			for (int i = 0; i < ccg.getClasses().size(); i++) {
				Clazz c = ccg.getClasses().get(i);
				tempClazzs.put(c.getName() + c.getPkg(), c);
				Clazz oldc = oldClazzs.get(c.getName() + c.getPkg());
				List<String> dependClazzs = c.getDependencyRelationship()
						.getClazz2();// 依赖关系类
				List<String> incidenceClazzs = c.getIncidenceRelationship()
						.getClazz2();// 关联关系类
				List<String> inheritanceClazzs = c.getSuperClassName();// 继承类
				if(oldc!=null){
				if(oldc.getDependencyRelationship().getClazz2()!=null){
				List<String> olddependClazzs = oldc.getDependencyRelationship()
						.getClazz2();// 上一个版本 依赖关系类
				addE += cEdge(dependClazzs, olddependClazzs);
				remE += cEdge(olddependClazzs, dependClazzs);
				}
				if(oldc.getIncidenceRelationship().getClazz2()!=null){					
					List<String> oldincidenceClazzs = oldc.getIncidenceRelationship().getClazz2();// 上一个版本 关联关系类
					addE += cEdge(incidenceClazzs, oldincidenceClazzs);
					remE += cEdge(oldincidenceClazzs, incidenceClazzs);
				}
				if(oldc.getSuperClassName()!=null){					
					List<String> oldinheritanceClazzs = oldc.getSuperClassName();// 上一个版本 继承类
					addE += cEdge(inheritanceClazzs, oldinheritanceClazzs);
					remE += cEdge(oldinheritanceClazzs, inheritanceClazzs);
				}
				}else{
					if(c.getDependencyRelationship().getClazz2()!=null){
						addE+=c.getDependencyRelationship().getClazz2().size();
					}
					if(c.getIncidenceRelationship().getClazz2()!=null){
						addE+=c.getIncidenceRelationship().getClazz2().size();
					}
					if(c.getSuperClassName()!=null){
						addE+=c.getSuperClassName().size();
					}
				}
				
																				
				if(c.getDependencyRelationship().getClazz2()!=null){
					addNE+=c.getDependencyRelationship().getClazz2().size();
				}
				if(c.getIncidenceRelationship().getClazz2()!=null){
					addNE+=c.getIncidenceRelationship().getClazz2().size();
				}
				if(c.getSuperClassName()!=null){
					addNE+=c.getSuperClassName().size();
				}
				
			}
		} else {
			for(int i=0;i<ccg.getClasses().size();i++){
				Clazz c = ccg.getClasses().get(i);
				if(c.getDependencyRelationship().getClazz2()!=null){
					addNE+=c.getDependencyRelationship().getClazz2().size();
				}
				if(c.getIncidenceRelationship().getClazz2()!=null){
					addNE+=c.getIncidenceRelationship().getClazz2().size();
				}
				if(c.getSuperClassName()!=null){
					addNE+=c.getSuperClassName().size();
				}
				addE=addNE;
				remE=0;
			}

		}
		changeEdges.add(addE);
		changeEdges.add(remE);
		changeEdges.add(addNE);
		return changeEdges;
		
	}
	//计算变化的边的方法
	public static int cEdge(List<String>e1,List<String>e2){
		int num=0;
		if (e1 != null) {
			for (int i = 0; i < e1.size(); i++) {
				if (!e2.contains(i)) {
					++num;
				}
			}
		}
		return num;
		
	}
}
