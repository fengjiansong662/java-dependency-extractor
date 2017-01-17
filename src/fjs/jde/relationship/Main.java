package fjs.jde.relationship;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import javax.swing.JFrame;

public class Main extends JFrame {

	/**
	 * @param args
	 */
	
	  private void setupReceiver(MyTableModel aModel)
	  {
	    int port = 4445;
	    String strRep = System.getProperty("chainsaw.port");
	    if (strRep != null) {
	      try
	      {
	        port = Integer.parseInt(strRep);
	      }
	      catch (NumberFormatException nfe)
	      {
	        LOG.fatal("Unable to parse chainsaw.port property with value " + strRep + ".");
	        
	        JOptionPane.showMessageDialog(this, "Unable to parse port number from '" + strRep + "', quitting.", "CHAINSAW", 0);
	        
	        System.exit(1);
	      }
	    }
	    try
	    {
	      LoggingReceiver lr = new LoggingReceiver(aModel, port);
	      lr.start();
	    }
	    catch (IOException e)
	    {
	      LOG.fatal("Unable to connect to socket server, quiting", e);
	      JOptionPane.showMessageDialog(this, "Unable to create socket on port " + port + ", quitting.", "CHAINSAW", 0);
	      
	      System.exit(1);
	    }
	  }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
