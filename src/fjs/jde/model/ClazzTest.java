package fjs.jde.model;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//import fjs.jde.sql.SQLMethod;     //写p2p时注释掉，以后还要放开


public class ClazzTest {
	
    public ClazzTest() {
		
		
	}

	public static void main(String[] args) {
		//System.out.println(DynamicQueryTest.query().size());
        Clazz c = new Clazz();
        //c.setId(3);
        c.setName("Clazz");
        c.setPkg("fjs.jde.model");
        c.setFileName("model");
        //c.setCommitID("dddddddddddddfdsvf");
        JavaFile f=new JavaFile();
        f.setFileName("model");
        f.setCommitID("dsfdsvrevsvgwvev");
        Package p=new Package();
        p.setPath("fjs.jde.model");
        p.setCommitID("ddddddddddddddddddd");
        Configuration cfg = new Configuration();
        SessionFactory sf = cfg.configure().buildSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(f);
        session.save(c);
        session.save(p);
        session.getTransaction().commit();
        Clazz  re= (Clazz) session.get(Clazz.class, 1);
        System.out.println(re.getName());
        System.out.println(re.getId());
        //写p2p时注释掉，以后还要放开
        /*  List l=SQLMethod.queryPackage("74da0769266a8fd5832e558f1a6e0081895b9201");
        for(int i=0;i<SQLMethod.queryPackage("74da0769266a8fd5832e558f1a6e0081895b9201").size();i++){
        
        }*/
    
        session.close();
        sf.close();
		//System.out.println(SQLMethod.queryPackage("74da0769266a8fd5832e558f1a6e0081895b9201").size());    //写p2p时注释掉，以后还要放开
    }

}



