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
	
	//������洢��Դ�����н������������Լ��ϣ����ڳ�ȡ���������ʵ�����
	private Method method;
	private ArrayList<IBinding> attributes;
	final ArrayList<String> localVariables = new ArrayList<String>();

	public ASTMethodBodyVisitor (Method method, ArrayList<IBinding> fieldBindings) {
		this.method = method;
		this.attributes = fieldBindings;
	}

	//�ҷ�������������Ա���Եķ���
	@Override
	public boolean visit(SimpleName variableName){
		if(variableName == null)
			return true;
		
    	for(IBinding att : attributes) {    		
    		if(att == null || att.getName() == null) continue;
    		
    		if (att.getName().contentEquals(variableName.toString())){			    			
    			//��variableName����������Դ���method�У������÷��������˸�����
    			method.addAccessedAttributes(att.getName());
    			break;
    		}
    	}
    	return true;
	}
	
	//������Ի��������Ķ���
	@Override
	public boolean visit(SingleVariableDeclaration variable){						
    	return addLocalVarables (variable);
	}
	
	//������Ի��������Ķ��壬����������������ȫ���Ҳ�����
	@Override
	public boolean visit(VariableDeclarationFragment variable){
    	return addLocalVarables (variable);
	}
	
	//�������if���Ķ��壬�������������жϵĸ���
	@Override
	public boolean visit(IfStatement node) {

		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
		
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 
		return true;
	}
	
	//�������while���Ķ��壬�������������жϵĸ���
	@Override
	public boolean visit(WhileStatement node) {
		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
								
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 

		return true;
	}
	//�������for���Ķ��壬�������������жϵĸ���
	//Ŀǰ���޷�����for( : )������
	@Override
	public boolean visit(ForStatement node) {
		if(node.getExpression()==null) return true;

		String str = node.getExpression().toString();
		String[] orNum = str.split("\\|\\|");
		String[] andNum = str.split("&&");
		
		method.setConditionNum( method.getConditionNum() + (orNum.length + andNum.length - 1) ); 

		return true;
	}
	
	//�������switch���Ķ��壬�������������жϵĸ���
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