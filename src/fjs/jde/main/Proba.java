package fjs.jde.main;

import java.io.File;

public class Proba {
	String lineString="";
	public static void main(String[] args) {
        Proba p = new Proba();
        p.start();
        //p.start2();
}

private void start() {
	  Treenodes root = new Treenodes(); 
	   root.name="����";
	   root.Childs = new Treenodes[3];
	   root.Childs[0]=new Treenodes();
       root.Childs[0].name="����1";
       root.Childs[1]=new Treenodes();
       root.Childs[1].name="����2";
       root.Childs[2]=new Treenodes();
       root.Childs[2].name="����3";
       root.Childs[0].Childs=new Treenodes[2];
       root.Childs[0].Childs[0]= new Treenodes(); 
       root.Childs[0].Childs[0].name="����1";
       root.Childs[0].Childs[1]= new Treenodes(); 
       root.Childs[0].Childs[1].name="����2";
       root.Childs[1].Childs=new Treenodes[2];
       root.Childs[1].Childs[0]= new Treenodes();
       root.Childs[1].Childs[0].name="����3";
       root.Childs[1].Childs[1]= new Treenodes();
       root.Childs[1].Childs[1].name="����4";
       root.Childs[2].Childs=new Treenodes[2];
       root.Childs[2].Childs[0]= new Treenodes();
       root.Childs[2].Childs[0].name="����5";
       root.Childs[2].Childs[1]= new Treenodes();
       root.Childs[2].Childs[1].name="����6";
	   /********************************/      
        GraphViz gv = new GraphViz();
        gv.addln(gv.start_graph());
        //gv.addln("rankdir = LR");//��ɺ���
        
        /*gv.addln("encoding=\"UTF-8\"");
        gv.addln("node[shape=record]");*/
        //gv.addln("{rank=same;Strategy}");
  
        gv.addln("nodesep=0.8");
        gv.addln("node [ fontname=\"Bitstream Vera Sans\", fontsize=8, shape=\"record\" ]");
        gv.addln("edge [fontsize=8,arrowhead=\"empty\"];");
  /*      //gv.addln("Animal [label = \"Animal\"];");
        gv.addln("Animal"+"[label = \"a.Animal\"];");
        gv.addln("subgraph clusterAnimalImpl {label=\"Package animal.impl\"Dog [label = \"Dog\"]{ rank=same; Dog }}");
        gv.addln("subgraph clusterAnimalImpl {label=\"Package animal.impl\"Cat [label = \"Cat\"]{ rank=same; Cat }}");
        gv.addln("subgraph clusterAnimalImpl1 {label=\"Package as.jde.test.bugs\"eclipse [label = \"eclipse\"]{ rank=same; eclipse }}");
        gv.add("subgraph clusterAnimalImpl1 {label=\"Package java\"io [label = \"io\"]{ rank=same; io }}");
        gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");
        gv.addln("eclipse->io;");
        gv.addln("Dog -> Animal;");
        gv.addln("Cat -> Animal;");
        gv.addln("edge [arrowhead = \"none\"headlabel = \"0..*\"taillabel = \"0..*\"]");
        gv.addln("NetworkBuilder" + "[label = \" as.jcge.main.NetworkBuilder \"];");
        gv.addln("DatabaseConnector" + "[label = \" as.jcge.db.DatabaseConnector \"];");
        gv.addln("edge[arrowhead=\"onormal\",style=\"filled\"]");
		gv.addln( "NetworkBuilder -> DatabaseConnector;");*/
		gv.addln("mechanjarweixinbeanoutxmlbuilderVoiceBuilder[label = \"me.chanjar.weixin.bean.outxmlbuilder.VoiceBuilder\"];");
		gv.addln("mechanjarweixinbeanoutxmlbuilderBaseBuilder<mechanjarweixinbeanoutxmlbuilderVoiceBuilder,mechanjarweixinbeanWxXmlOutVoiceMessage>[label = \"me.chanjar.weixin.bean.outxmlbuilder.BaseBuilder<me.chanjar.weixin.bean.outxmlbuilder.VoiceBuilder,me.chanjar.weixin.bean.WxXmlOutVoiceMessage>\"];");
        gv.addln("edge[arrowhead=\"onormal\",style=\"filled\"]");
        gv.addln("mechanjarweixinbeanoutxmlbuilderVoiceBuilder->mechanjarweixinbeanoutxmlbuilderBaseBuilder<mechanjarweixinbeanoutxmlbuilderVoiceBuilder,mechanjarweixinbeanWxXmlOutVoiceMessage>;");

        /*gv.addln("{rank=same;util}");
        gv.addln("{rank=same;File}");
        gv.addln("{rank=same;F}");
        gv.addln("{rank=same;as\\.jde\\.test\\.main}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("util->File;");
        gv.addln("{rank=same;ast}");
        gv.addln("{rank=same;File;Method}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("ast->File;");
        gv.addln("ast->Method;");
        gv.addln("{rank=same;output}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("output->File;");
        gv.addln("output->Method;");
        gv.addln("output->Element;");
        gv.addln("output->Writer;");
        gv.addln("output->CallGraph;");
        gv.addln("{rank=same;git}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph;Commit}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("git->File;");
        gv.addln("git->Method;");
        gv.addln("git->Element;");
        gv.addln("git->Writer;");
        gv.addln("git->CallGraph;");
        gv.addln("git->Commit;");
        gv.addln("{rank=same;main}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph;Commit;GitController;ThreadedOutput;SCMIterator}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("main->File;");
        gv.addln("main->Method;");
        gv.addln("main->Element;");
        gv.addln("main->Writer;");
        gv.addln("main->CallGraph;");
        gv.addln("main->Commit;");
        gv.addln("main->GitController;");
        gv.addln("main->ThreadedOutput;");
        gv.addln("main->SCMIterator;");
        gv.addln("{rank=same;graph1}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph;Commit;GitController;ThreadedOutput;SCMIterator}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("graph1->File;");
        gv.addln("graph1->Method;");
        gv.addln("graph1->Element;");
        gv.addln("graph1->Writer;");
        gv.addln("graph1->CallGraph;");
        gv.addln("graph1->Commit;");
        gv.addln("graph1->GitController;");
        gv.addln("graph1->ThreadedOutput;");
        gv.addln("graph1->SCMIterator;");
        gv.addln("{rank=same;eclipse}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph;Commit;GitController;ThreadedOutput;SCMIterator}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("eclipse->File;");
        gv.addln("eclipse->Method;");
        gv.addln("eclipse->Element;");
        gv.addln("eclipse->Writer;");
        gv.addln("eclipse->CallGraph;");
        gv.addln("eclipse->Commit;");
        gv.addln("eclipse->GitController;");
        gv.addln("eclipse->ThreadedOutput;");
        gv.addln("eclipse->SCMIterator;");
        gv.addln("{rank=same;Context;Strategy}");
        gv.addln("{rank=same;scm}");
        gv.addln("{rank=same;File;Method;Element;Writer;CallGraph;Commit;GitController;ThreadedOutput;SCMIterator;JProject}");
        gv.addln("edge [ arrowhead=\"open\", style=\"dashed\"]");
        gv.addln("scm->File;");
        gv.addln("scm->Method;");
        gv.addln("scm->Element;");
        gv.addln("scm->Writer;");
        gv.addln("scm->CallGraph;");
        gv.addln("scm->Commit;");
        gv.addln("scm->GitController;");
        gv.addln("scm->ThreadedOutput;");
        gv.addln("scm->SCMIterator;");
        gv.addln("scm->JProject;");
        gv.addln("{rank=same;Context;Strategy}");
        //gv.addln(" Context[label=\"{Context| +strategy : Strategy\\l| +contextInterface()\\l}\"]");
        //gv.addln("docs[style=\"filled\", color=\"gold\",	label=\"Strategy AlgorithmInterface()\"] ");
        gv.addln("edge [ arrowhead=\"none\", style=\"dashed\"]");
        gv.addln("Context->docs;");
        
        //gv.addln("Strategy[ label=\"{Strategy|\\l|+algorithmInterface()\\l}\"]");
        //gv.addln("ca[label=\"{ConcreteStrategyA|\\l+algorithmInterface()\\l}\"]");
        //gv.addln("cb[label=\"{ConcreteStrategyB|\\l+algorithmInterface()\\l}\"]");
        //gv.addln("cc[label=\"{ConcreteStrategyC|\\l+algorithmInterface()\\l}\"] ");
        gv.addln("edge[arrowhead=\"odiamond\", style=\"filled\"]");//�ۺϹ�ϵ
        gv.addln("Strategy -> Context;");
        gv.addln("edge[ arrowhead=\"onormal\", style=\"filled\"]");//�̳й�ϵ
       gv.addln("{rank=same;ca;cb;cc;docs}");
        gv.addln("ca->Strategy;");
        gv.addln("cb->Strategy;");
        gv.addln("cc->Strategy;");*/
        
        
        
       // gv.addln("node [shape=box]");
        //����һ�У�����Ϊ����
        //gv.addln("node [fontname=\"simhei.ttf\",style=filled, fillcolor=\".7 .3 1.0\", color=green, fontsize=10]");
        //����һ�У�����Ϊ����
       // gv.addln("edge[arrowhead=\"open\",style=\"dashed\"]");//������ϵ
        //gv.addln("edge[arrowhead=\"open\", style=\"filled\"]");//������ϵ       arrowsize= 1.5�ߵĴ�ϸ   ,fontsize=10
        //gv.addln("A -> D->����[penwidth=7,color=\"red\"];");
        //root.traverse(gv);
//        String s1="A -> D->����";
//        gv.addln(s1);
//        gv.addln("A -> C;");
//        gv.addln("�����->C->B;");
//        gv.addln("lpf->�����;");
//        gv.addln("lpf->������;");
//        gv.addln("C->E;");
        gv.addln(gv.end_graph());
        System.out.println(gv.getDotSource());

        String type = "gif";
        //String type = "dot";
        // String type = "fig"; // open with xfig
        // String type = "pdf";
        // String type = "ps";
        // String type = "svg"; // open with inkscape
        // String type = "png";
        // String type = "plain";
        File out = new File("F:\\graphviz-2.38\\o." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
}


private void start2() {
        //String input = "e:/temp/simple.dot";
		String input = "E:\\graphviz-2.38\\digraph G.dot";
        GraphViz gv = new GraphViz();
       gv.addln("node [shape=box,fontname=\"simhei.ttf\",style=filled, fillcolor=\".7 .3 1.0\", color=green, fontsize=10]");
        gv.addln("encoding=\"UTF-8\"");
        gv.readSource(input);
        System.out.println(gv.getDotSource());


        String type = "gif";
        // String type = "dot";
        // String type = "fig"; // open with xfig
        // String type = "pdf";
        // String type = "ps";
        // String type = "svg"; // open with inkscape
        // String type = "png";
        // String type = "plain";
        File out = new File("E:\\graphviz-2.38\\o." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);      
}

}
class Treenodes {
    String key = "�й�";
    String name = null;
    Treenodes[] Childs = null;
   public Treenodes(){
   }
   /* ����һ��������α��� */
   public void traverse(GraphViz gv) {
       if (Childs == null)
           return;
       int childNumber = Childs.length;
       for (int i = 0; i < childNumber; i++) {
    	   Treenodes child = Childs[i];
    	   String lineString=this.name+"->"+Childs[i].name;
    	   lineString=lineString+"[label="+'"'+Childs[i].key+'"' +",fontname=\"simhei.ttf\"]";
    	   System.out.println(lineString);
    	   gv.addln(lineString);
           child.traverse(gv);
       }
   }
}