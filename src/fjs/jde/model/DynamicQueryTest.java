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
	        String hql = "from Clazz as clazz where clazz.commitID=:n";//from User查询的是对象User，而不是User表.这也是HQL与SQL的区别  
	        Query query = s.createQuery(hql);  
	        query.setString("n","74da0769266a8fd5832e558f1a6e0081895b9201"); //设置HQL语句中的:n对应的值  
	        //query.setFirstResult(200);//分页。记录开始数  
	       // query.setMaxResults(10);//分页。显示记录条数  
	        return query.list();//多条记录  
	          
	        /* 
	        如果确定返回的结果只有一条记录，可以使用query.uniqueResult(); 
	        但如果使用此方法，若超出一条记录，将抛出异常。 
	        */   
	    }finally{  
	        if(s != null)  
	            s.close();  
	    }  
	      
	}  
   
    
}