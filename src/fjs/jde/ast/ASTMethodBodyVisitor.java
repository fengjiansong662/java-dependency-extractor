package fjs.jde.ast;

import java.util.ArrayList;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import as.jde.graph.Method;

public class ASTMethodBodyVisitor extends ASTVisitor {
	
	//该数组存储从源代码中解析出来的属性集合，用于抽取方法所访问的属性
	private Method method;
	private ArrayList<IBinding> attributes;
	final ArrayList<String> localVariables = new ArrayList<String>();

	public ASTMethodBodyVisitor (Method method, ArrayList<IBinding> fieldBindings) {
		this.method = method;
		this.attributes = fieldBindings;
	}

	//找方法体里面对类成员属性的访问
	@Override
	public boolean visit(SimpleName variableName){
		if(variableName == null)
			return true;
		
    	for(IBinding att : attributes) {    		
    		if(att == null || att.getName() == null) continue;
    		
    		if (att.getName().contentEquals(variableName.toString())){			    			
    			//将variableName所代表的属性存入method中，表明该方法访问了该属性
    			method.addAccessedAttributes(att.getName());
    			break;
    		}
    	}
    	return true;
	}
	
	//方法里对基本变量的定义
	@Override
	public boolean visit(SingleVariableDeclaration variable){						
    	return addLocalVarables (variable);
	}
	
	//方法里对基本变量的定义，但这两个函数还不全，找不完整
	@Override
	public boolean visit(VariableDeclarationFragment variable){
    	return addLocalVarables (variable);
	}
	
	//方法里对if语句的定义，计算其中条件判断的个数
	@Override
	public boolean visit(IfStatement node) {

		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
		
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 
		return true;
	}
	
	//方法里对while语句的定义，计算其中条件判断的个数
	@Override
	public boolean visit(WhileStatement node) {
		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
								
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 

		return true;
	}
	//方法里对for语句的定义，计算其中条件判断的个数
	//目前尚无法处理for( : )的情形
	@Override
	public boolean visit(ForStatement node) {
		if(node.getExpression()==null) return true;

		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
		
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 

		return true;
	}
	
	//方法里对switch语句的定义，计算其中条件判断的个数
	@Override
	public boolean visit(SwitchStatement node) {
		String str = node.getExpression().toString();
		String[] caseNum = str.split("case ");
		
		method.setConditionNum( method.getConditionNum() + caseNum.length ); 

		return true;
	}
	
	private boolean addLocalVarables (Object variable) {
		if (variable == null)
			return true;
		boolean added = false;
		for (String str : localVariables) {
			if(str.equals(variable)) {
				added = true;
				break;
			}
		}
		if(!added) {
			localVariables.add(variable.toString());
			method.addAccessedLocalData(variable.toString());
		}
		
		return true;
	}
}