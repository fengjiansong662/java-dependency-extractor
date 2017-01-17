package fjs.jde.sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
 
 
/*public class MySqlConnection {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        String sql;
        // MySQL��JDBC URL��д��ʽ��jdbc:mysql://�������ƣ����Ӷ˿�/���ݿ������?����=ֵ
        // ������������Ҫָ��useUnicode��characterEncoding
        // ִ�����ݿ����֮ǰҪ�����ݿ����ϵͳ�ϴ���һ�����ݿ⣬�����Լ�����
        // �������֮ǰ��Ҫ�ȴ���javademo���ݿ�
        String url = "jdbc:mysql://localhost:3306/mysql?"
                + "user=root&password=root&useUnicode=true&characterEncoding=UTF8";
 
        try {
            // ֮����Ҫʹ������������䣬����ΪҪʹ��MySQL����������������Ҫ��������������
            // ����ͨ��Class.forName�������ؽ�ȥ��Ҳ����ͨ����ʼ������������������������ʽ������
            Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql����
            // or:
            // com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
            // or��
            // new com.mysql.jdbc.Driver();
 
            System.out.println("�ɹ�����MySQL��������");
            // һ��Connection����һ�����ݿ�����
            conn = DriverManager.getConnection(url);
            // Statement������кܶ෽��������executeUpdate����ʵ�ֲ��룬���º�ɾ����
            Statement stmt = conn.createStatement();
            sql = "create table student(NO char(20),name varchar(20),primary key(NO))";
            int result = stmt.executeUpdate(sql);// executeUpdate���᷵��һ����Ӱ����������������-1��û�гɹ�
            if (result != -1) {
                System.out.println("�������ݱ�ɹ�");
                sql = "insert into student(NO,name) values('2012001','��ΰ��')";
                result = stmt.executeUpdate(sql);
                sql = "insert into student(NO,name) values('2012002','��С��')";
                result = stmt.executeUpdate(sql);
                sql = "select * from student";
                ResultSet rs = stmt.executeQuery(sql);// executeQuery�᷵�ؽ���ļ��ϣ����򷵻ؿ�ֵ
                System.out.println("ѧ��\t����");
                while (rs.next()) {
                    System.out
                            .println(rs.getString(1) + "\t" + rs.getString(2));// ��������ص���int���Ϳ�����getInt()
                }
            }
        } catch (SQLException e) {
            System.out.println("MySQL��������");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
 
    }
 
}*/

public class MySqlConnection {
	public static Connection conn = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;

	public void getCon() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/mysql?"
						+ "user=root&password=123456&useUnicode=true&characterEncoding=UTF8");
	}

	public void closeCon(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();

	}

	public static void main(String[] args) {
		MySqlConnection dbc = new MySqlConnection();
		try {
			dbc.getCon();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from user");
			while (rs.next()) {
				System.out.println(rs.getInt("USERID"));
				System.out.println(rs.getString("USERNAME"));
				System.out.println(rs.getString("USERPASSWORD"));
				// System.out.println(rs.getString("USERMAIL"));
				// System.out.println(rs.getDate("USERTIME"));
				// System.out.println(rs.getString("USERINTEGRAL"));
				// System.out.println(rs.getString("USERSEX"));
				System.out.println();
			}
			System.out.println("conn:" + MySqlConnection.conn);
			System.out.println("mysql���Գɹ�");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}