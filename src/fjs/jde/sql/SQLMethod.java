package fjs.jde.sql;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.function.ClassicAvgFunction;
import org.hibernate.dialect.function.ClassicCountFunction;
import org.hibernate.dialect.function.ClassicSumFunction;

import fjs.jde.model.A2A;
import fjs.jde.model.ChangeEdge;
import fjs.jde.model.Clazz;
import fjs.jde.model.ClazzEdge;
import fjs.jde.model.HibernateSessionFactory;
import fjs.jde.model.JavaFile;
import fjs.jde.model.Package;
import fjs.jde.model.PackageEdge;

public class SQLMethod{
	
	 public SQLMethod() {
	 } 
	 public static void insertClazz(Clazz clazz){
		 Clazz c=new Clazz();
		 c.setFileName(clazz.getFileName());
		 c.setName(clazz.getName());
		 c.setPkg(clazz.getPkg());
		 
		 c.setLoc(clazz.getLoc());
		 
		 c.setCommitID(clazz.getCommitID());
		 Configuration cfg = new Configuration();
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        session.save(c);
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	
	  //����֮��ıߴ������ݿ���
		 public static void insertClazzEdge(ClazzEdge clazzEdge){
			 ClazzEdge cEdge=new ClazzEdge();
			 cEdge.setStartpoint(clazzEdge.getStartpoint());
			 cEdge.setEndpoint(clazzEdge.getEndpoint());
			 cEdge.setReleaseName(clazzEdge.getReleaseName());
			 cEdge.setCommitID(clazzEdge.getCommitID());
			 cEdge.setType(clazzEdge.getType());
			 
			 Configuration cfg = new Configuration();
			 cfg.configure();  
			    SessionFactory sf = null;  
			    Session session = null;  
			    try{  
			        sf = cfg.buildSessionFactory();  
			        session = sf.openSession();   
		        //SessionFactory sf = cfg.configure().buildSessionFactory();
		        //Session session = sf.openSession();
		        session.beginTransaction();
		        String hql = "from ClazzEdge as clazzedge where clazzedge.startpoint=:a and clazzedge.endpoint=:b and clazzedge.releaseName=:c and clazzedge.commitID=:d and clazzedge.type=:f";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = session.createQuery(hql);  
		        query.setString("a",clazzEdge.getStartpoint()); 
		        query.setString("b",clazzEdge.getEndpoint()); 
		        query.setString("c",clazzEdge.getReleaseName());
		        query.setString("d",clazzEdge.getCommitID()); //����HQL����е�:n��Ӧ��ֵ  
		        query.setString("f",clazzEdge.getType());
		        if(query.list().size()==0){
		        	cEdge.setNum(1);
		        	session.save(cEdge);
		        }else{
		        	
		 	         int num =queryClazzEdgeNum(clazzEdge);
		        	 String hql3 = "update ClazzEdge as clazzedge set clazzedge.num=:g where clazzedge.startpoint=:h and clazzedge.endpoint=:m and clazzedge.releaseName=:n and clazzedge.commitID=:q and clazzedge.type=:w";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
				     Query query3 = session.createQuery(hql3);  
				     
				     query3.setInteger("g", num+1);
				     query3.setString("h", clazzEdge.getStartpoint());
				     query3.setString("m", clazzEdge.getEndpoint());
				     query3.setString("n", clazzEdge.getReleaseName());
				     query3.setString("q", clazzEdge.getCommitID());
				     query3.setString("w", clazzEdge.getType());
				     query3.executeUpdate();	
		        }
			

		        session.getTransaction().commit();
		        session.close();
			 }finally{  
			    if(sf != null)  
			            sf.close();  
			    }  
		 }
		

	 
	 //����֮��ıߴ������ݿ���
	 public static void insertPackageEdge(PackageEdge packageEdge){
		 PackageEdge pEdge=new PackageEdge();
		 pEdge.setStartP(packageEdge.getStartP());
		 pEdge.setEndP(packageEdge.getEndP());
		 pEdge.setReleaseName(packageEdge.getReleaseName());
		 pEdge.setCommitID(packageEdge.getCommitID());
		 pEdge.setType(packageEdge.getType());
		 pEdge.setNum(packageEdge.getNum());
		 
		 Configuration cfg = new Configuration();
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        session.save(pEdge);
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	 
	 //��changeEdge��ֵ�������ݿ���
	 public static void insertChangeEdge(String releaseName,String commitID,List<Integer> changeEdge){
		 ChangeEdge e=new ChangeEdge();
		 e.setCommitID(commitID);
		 e.setReleaseName(releaseName);
		 e.setAddEdge(changeEdge.get(0));
		 e.setRemEdge(changeEdge.get(1));
		 e.setAddNEdge(changeEdge.get(2));
		 Configuration cfg = new Configuration();
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        session.save(e);
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	 
	 public static void insertA2A(String releaseName,String commitID1,String commitID2,double value){
		 A2A a=new A2A();
		 a.setCommitID(commitID1);
		 a.setNextCommitID(commitID2);
		 a.setValue(value);
		 a.setReleaseName(releaseName);
		 Configuration cfg = new Configuration();
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        session.save(a);
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	 
	 public static void insertClazzs(List<Clazz> clazzs){
		 HibernateSessionFactory sessionFactory;
			Session session = HibernateSessionFactory.getSession();   
			 Transaction tx = session.beginTransaction();
			// tx.begin(); //��������
			 for ( int i=0; i<clazzs.size(); i++ ) {
				 Clazz c=new Clazz();
			 
				 c.setFileName(clazzs.get(i).getFileName());
				 c.setName(clazzs.get(i).getName());
				 c.setPkg(clazzs.get(i).getPkg());
				 c.setAllPath(clazzs.get(i).getAllPath());
				 c.setLoc(clazzs.get(i).getLoc());
				 c.setReleaseName(clazzs.get(i).getReleaseName());
				 c.setCommitID(clazzs.get(i).getCommitID());
			     
			        
			     session.setCacheMode(CacheMode.IGNORE);  
			     
			     session.save(c);
		
			     if ( i % 50 == 0 ) {   
			           //����������Ķ�������д�����ݿⲢ�ͷ��ڴ�   
			           session.flush();   
			           session.clear();   
			     }   
			 }   
			 tx.commit();   
			 session.close();
	 }
	 public static void insertPackage(Package pkg){
		
		 Package p=new Package();
	     p.setPath(pkg.getPath());
	     p.setReleaseName(pkg.getReleaseName());
	     p.setCommitID(pkg.getCommitID());
		 Configuration cfg = new Configuration();
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        session.save(p);
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	 
	 
	 /*
	  * Hibernate������������
	  * @param recordList
	  */
/*	 public void saveRecordByList(final List<Package> recordList){
	   getHibernateTemplate().execute(new HibernateCallback() {
	   public Object doInHibernate(Session session) throws HibernateException,
	     SQLException {
	    session.beginTransaction();
	    // ÿ���ύ�������
	    final int batchSize = 50;
	    int count = 0;
	    for (Package record : recordList) {
	     session.save(record);
	     // ÿ200�������ύһ��
	     if (++count % batchSize == 0) {
	      session.getTransaction().commit();
	                  session.flush();
	                  session.clear();
	                  session.beginTransaction();
	     }
	    }
	    // �ύʣ�������
	    session.beginTransaction();
	    return null;
	   }
	  });
	 }*/
	 public static void insertP(List<Package> packages){
		HibernateSessionFactory sessionFactory;
		Session session = HibernateSessionFactory.getSession();   
		 Transaction tx = session.beginTransaction();  
		 //tx.begin(); //��������
		 for ( int i=0; i<packages.size(); i++ ) {
			 Package p = new Package();  
		     p.setPath(packages.get(i).getPath());
		     p.setReleaseName(packages.get(i).getReleaseName());
		     p.setCommitID(packages.get(i).getCommitID());
		     p.setAllPath(packages.get(i).getAllPath());
		     p.setSize(packages.get(i).getSize());
		   /*  String hql = "from Package as package where package.commitID=:n and package.releaseName=:y and package.path=:m and package.size=:x and package.allPath=:g";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		     Query query = session.createQuery(hql);  
		     query.setString("n",p.getCommitID()); 
		     query.setString("y", p.getReleaseName());
		     query.setString("m",p.getPath());//����HQL����е�:n��Ӧ��ֵ  
		     query.setLong("x",p.getSize());
		     query.setString("g",p.getAllPath());*/   
		     session.setCacheMode(CacheMode.IGNORE);  
		     //if(query.list().size()==0){
		        	session.save(p);
		     //}
		     if ( i % 50 == 0 ) {   
		           //����������Ķ�������д�����ݿⲢ�ͷ��ڴ�   
		           session.flush();   
		           session.clear();   
		     }   
		 }   
		 tx.commit();   
		 session.close();  
	 }
	 
	 public static void insertClazzEdegs(Map<String,ClazzEdge> clazzEdges){
			HibernateSessionFactory sessionFactory;
			Session session = HibernateSessionFactory.getSession();   
			 Transaction tx = session.beginTransaction();  
			 //tx.begin(); //��������
			 
			 int i=0;
			 for (Map.Entry<String, ClazzEdge> entry : clazzEdges.entrySet()) {
				 i++;
				 ClazzEdge c=entry.getValue(); 
				 String hql = "from ClazzEdge as clazzedge where clazzedge.startpoint=:h and clazzedge.endpoint=:m and clazzedge.releaseName=:n and clazzedge.commitID=:q and clazzedge.type=:w and clazzedge.num=:g";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
			     Query query = session.createQuery(hql);  
			     
			     query.setString("h", c.getStartpoint());
			     query.setString("m", c.getEndpoint());
			     query.setString("n", c.getReleaseName());
			     query.setString("q", c.getCommitID());
			     query.setString("w", c.getType());
			     query.setInteger("g", c.getNum());
			     
			     session.setCacheMode(CacheMode.IGNORE);  
			     if(query.list().size()==0){
			        	session.save(c);
			     }
			     if ( i % 50 == 0 ) {   
			           //����������Ķ�������д�����ݿⲢ�ͷ��ڴ�   
			           session.flush();   
			           session.clear();   
			     }   
			}  
			 tx.commit();   
			 session.close();  
		 }
	 
	 public static void insertFile(JavaFile file){
			
		 JavaFile f=new JavaFile();
	     f.setFileName(file.getFileName());
	     f.setReleaseName(file.getReleaseName());
	     f.setCommitID(file.getCommitID());
	     f.setPkg(file.getPkg());
	     
		 Configuration cfg = new Configuration();
		 
	        SessionFactory sf = cfg.configure().buildSessionFactory();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        String hql = "from JavaFile as javafile where javafile.commitID=:n and javafile.releaseName=:x and javafile.fileName=:m";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
	        Query query = session.createQuery(hql);  
	        query.setString("n",file.getCommitID()); 
	        query.setString("x",file.getReleaseName()); 
	        query.setString("m",file.getFileName()); //����HQL����е�:n��Ӧ��ֵ  
	       
	        if(query.list().size()==0){
	        	session.save(f);
	        }
	        session.getTransaction().commit();
	        session.close();
	        sf.close();
	 }
	 
	 public static List<JavaFile> queryFiles(String releaseName){  
		    Configuration cfg = new Configuration();  
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "from JavaFile as javafile where javafile.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		        //query.setFirstResult(200);//��ҳ����¼��ʼ��  
		       // query.setMaxResults(10);//��ҳ����ʾ��¼����  
		        return query.list();//������¼  
		            
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        }  
		    }  
		      
		}  
	 
	 public static int queryClazzEdges(String releaseName){  
		 Configuration cfg = new Configuration();    
		 cfg.addSqlFunction( "count", new ClassicCountFunction());   
		/* classicCfg.addSqlFunction( "avg", new ClassicAvgFunction());   
		 classicCfg.addSqlFunction( "sum", new ClassicSumFunction());   */
		 
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null; 
		 
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "select count(*) from ClazzEdge as clazzedge where clazzedge.releaseName=:n";

		        Query query = s.createQuery(hql);
		        query.setString("n",releaseName);

		        int count = ((Integer) query.iterate().next()).intValue();
		      
		        return count;  
		       
		            
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        } 
		    }  
		      
		}  
	 
	 public static int queryClazzEdgeNum(ClazzEdge clazzEdge){  
		 ClazzEdge cEdge=new ClazzEdge();
		 cEdge.setStartpoint(clazzEdge.getStartpoint());
		 cEdge.setEndpoint(clazzEdge.getEndpoint());
		 cEdge.setReleaseName(clazzEdge.getReleaseName());
		 cEdge.setCommitID(clazzEdge.getCommitID());
		 cEdge.setType(clazzEdge.getType());
		 Configuration cfg = new Configuration();    
		 
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null; 
		 
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "select clazzedge.num from ClazzEdge as clazzedge where clazzedge.startpoint=:a and clazzedge.endpoint=:b and clazzedge.releaseName=:c and clazzedge.commitID=:d and clazzedge.type=:f";

		        Query query = s.createQuery(hql);
		       
		        query.setString("a",clazzEdge.getStartpoint()); 
		        query.setString("b",clazzEdge.getEndpoint()); 
		        query.setString("c",clazzEdge.getReleaseName());
		        query.setString("d",clazzEdge.getCommitID()); //����HQL����е�:n��Ӧ��ֵ  
		        query.setString("f",clazzEdge.getType());
		        int num = ((Integer) query.iterate().next()).intValue();
		      
		        return num;  
		       
		            
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        }
		    }  
		      
		}  
	 
	 public static ChangeEdge queryChangeEdge(String releaseName){  
		    Configuration cfg = new Configuration();  
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        String hql = "from ChangeEdge as changeedge where changeedge.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		  
		        return (ChangeEdge) query.uniqueResult();//һ����¼  
		          
		         
		        //���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		       // �����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        }  
		    }  
		      
		}  
	

	 
	 public static List<Clazz> queryClazz(String releaseName){  
		    Configuration cfg = new Configuration();  
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        String hql = "from Clazz as clazz where clazz.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		  
		        return query.list();//������¼  
		          
		         
		        //���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		       // �����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        }
		    }
		}  
	 
	 //��ѯÿ���汾��ÿ������������clazz
	 public static List<Clazz> queryClazzOfPackage(String releaseName,String allPath){  
		    Configuration cfg = new Configuration();  
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        String hql = "from Clazz as clazz where clazz.releaseName=:n and clazz.allPath=:m";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		        query.setString("m",allPath);
		        return query.list();//������¼  
		          
		         
		        //���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		       // �����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        }
		    }
		}  
	 
	 //�������P2Pֵ�������ݿ���
	 public static void updatePackage(String releaseName,String packageName,double p2pValue){
		 Configuration cfg = new Configuration();   
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.configure().buildSessionFactory();  
		        s = sf.openSession();  
		        s.beginTransaction();
		        String hql = "update Package as package set package.p2pValue=:a where package.releaseName =:b and package.allPath=:c";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setDouble("a", p2pValue);
		        query.setString("b", releaseName);
		        query.setString("c", packageName);
		        query.executeUpdate();
		     
		    }finally{  
		        if(s != null)  {
		        	s.getTransaction().commit();
		            s.close(); 
		            sf.close();
		        }
		    }  
		      
	 } 
	 
	 //������ıߵ�P2Pֵ�������ݿ���
	 public static void updatePackageEdge(String releaseName,String startP,String endP,double p2pValue){
		 Configuration cfg = new Configuration();    
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.configure().buildSessionFactory();  
		        s = sf.openSession();  
		        s.beginTransaction();
		        String hql = "update PackageEdge as packageedge set packageedge.p2pValue=:a where packageedge.releaseName =:b and packageedge.startP=:c and packageedge.endP=:d";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setDouble("a", p2pValue);
		        query.setString("b", releaseName);
		        query.setString("c", startP);
		        query.setString("d", endP);
		        query.executeUpdate();
		           
		    }finally{  
		        if(s != null){  
		        	s.getTransaction().commit();
		            s.close();
		            sf.close();
		            }  
		    }  
		      
	 } 
	 
	 //���������֮��ߵ�P2Pֵ�������ݿ���
	 public static void updateClazzEdge(String releaseName,String startpoint,String endpoint,String type,double p2pValue){
		 Configuration cfg = new Configuration();  
		    cfg.configure();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.buildSessionFactory();  
		        s = sf.openSession();  
		        s.beginTransaction();
		        String hql = "update ClazzEdge as clazzedge set clazzedge.p2pValue=:a where clazzedge.releaseName =:b and clazzedge.startpoint=:c and clazzedge.endpoint=:d and clazzedge.type=:e";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setDouble("a", p2pValue);
		        query.setString("b", releaseName);
		        query.setString("c", startpoint);
		        query.setString("d", endpoint);
		        query.setString("e", type);
		        query.executeUpdate();
		        
		           
		    }finally{  
		        if(s != null){  
		        	s.getTransaction().commit();
		            s.close();
		            sf.close();
		        }
		    }  
		      
	 } 
	 
	//���������P2Pֵ�������ݿ���
		 public static void updateClazz(String releaseName,String name,String allPath,double p2pValue){
			 Configuration cfg = new Configuration();  
			    cfg.configure();  
			    SessionFactory sf = null;  
			    Session s = null;  
			    try{  
			        sf = cfg.buildSessionFactory();  
			        s = sf.openSession();  
			        s.beginTransaction();
			        String hql = "update Clazz as clazz set clazz.p2pValue=:a where clazz.releaseName =:b and clazz.name=:c and clazz.allPath=:d";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
			        Query query = s.createQuery(hql);  
			        query.setDouble("a", p2pValue);
			        query.setString("b", releaseName);
			        query.setString("c", name);
			        query.setString("d", allPath);
			        query.executeUpdate();
			        
			           
			    }finally{  
			        if(s != null){  
			        	s.getTransaction().commit();
			            s.close();
			            sf.close();
			        }  
			    }  
			      
		 } 
		 
	 
	 public static List<PackageEdge> queryPackageEdge(String releaseName){  
		    Configuration cfg = new Configuration();  
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf =  cfg.configure().buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "from PackageEdge as packageedge where packageedge.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		        //query.setFirstResult(200);//��ҳ����¼��ʼ��  
		       // query.setMaxResults(10);//��ҳ����ʾ��¼����  
		        return query.list();//������¼  
		          
		         
		       // ���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		        //�����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		        if(s != null){  
		            s.close(); 
		        	sf.close();
		        }
		    }  
		      
		}  
	 public static List<ClazzEdge> queryClazzEdge(String releaseName){  
		    Configuration cfg = new Configuration();   
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf =cfg.configure().buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "from ClazzEdge as clazzedge where clazzedge.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		        //query.setFirstResult(200);//��ҳ����¼��ʼ��  
		       // query.setMaxResults(10);//��ҳ����ʾ��¼����  
		        return query.list();//������¼  
		          
		         
		       // ���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		        //�����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        } 
		    }  
		      
		}  
	 
	 public static List<Package> queryPackage(String releaseName){  
		    Configuration cfg = new Configuration();   
		    SessionFactory sf = null;  
		    Session s = null;  
		    try{  
		        sf = cfg.configure().buildSessionFactory();  
		        s = sf.openSession();  
		        
		        String hql = "from Package as package where package.releaseName=:n";//from User��ѯ���Ƕ���User��������User��.��Ҳ��HQL��SQL������  
		        Query query = s.createQuery(hql);  
		        query.setString("n",releaseName); //����HQL����е�:n��Ӧ��ֵ  
		        //query.setFirstResult(200);//��ҳ����¼��ʼ��  
		       // query.setMaxResults(10);//��ҳ����ʾ��¼����  
		        return query.list();//������¼  
		          
		         
		       // ���ȷ�����صĽ��ֻ��һ����¼������ʹ��query.uniqueResult(); 
		        //�����ʹ�ô˷�����������һ����¼�����׳��쳣�� 
		           
		    }finally{  
		    	 if(s != null){  
			            s.close(); 
			        	sf.close();
			        } 
		    }  
		      
		}  
	 

}
