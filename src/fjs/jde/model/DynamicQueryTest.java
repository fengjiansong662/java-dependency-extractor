package fjs.jde.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.hibernate.HibernateUtil;
import net.sf.ehcache.search.expression.Criteria;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.junit.Before;
import org.junit.Test;

public class DynamicQueryTest {
	public static List query(){  
	    Configuration cfg = new Configuration();  
	    cfg.configure();  
	    SessionFactory sf = null;  
	    Session s = null;  
	    try{  
	        sf = cfg.buildSessionFactory();  
	        s = sf.openSession();  
	        String hql = "from Clazz as clazz where clazz.commitID=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
	        Query query = s.createQuery(hql);  
	        query.setString("n","74da0769266a8fd5832e558f1a6e0081895b9201"); //����HQL����е�:n��Ӧ��ֵ  
	        //query.setFirstResult(200);//��ҳ����¼��ʼ��  
	       // query.setMaxResults(10);//��ҳ����ʾ��¼����  
	        return query.list();//������¼  
	          
	        /* 
	        ���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
	        �����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
	        */   
	    }finally{  
	        if(s != null)  
	            s.close();  
	    }  
	      
	}  
   
    
}