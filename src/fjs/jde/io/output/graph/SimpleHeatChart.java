package fjs.jde.io.output.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

public class SimpleHeatChart extends RevisedHeatChart {
	/**
	 * 简单的热表
	 * @param zValues
	 */

	public SimpleHeatChart(double[][] zValues) {
		super(zValues);
	}

	public void show(String fileName, String chartTitle, String xAxis, String yAxis, 
			Object[] xLabels, Object[] yLabels, String dir, boolean bShow) throws IOException {
		
		double [][] data = getZValues();
		
		int column = data[0].length;
		if(DataChart.isLargeColumnChart(column)) {
			setAxisValuesFont(DataChart.getScalableTickFont(column));
			setScalableCellSize(column);
		}
		else 
			setAxisValuesFont(DataChart.getTickFont());
		
		setAxisLabelsFont(DataChart.getAxisFont());
		setTitleFont(DataChart.getTitleFont());		
		setTitleColour(DataChart.getTitleColor());

		setTitle(chartTitle);
		setXAxisLabel(xAxis);
		setYAxisLabel(yAxis);
		if(yLabels != null)
			setYValues(yLabels);
		if(xLabels != null)
			setXValues(xLabels);
		
		setHighValueColour(Color.RED);
		setLowValueColour(Color.WHITE);
				
		if(DataChart.fileType.contentEquals("png")) {
			String pngFileName = dir + "\\" + fileName + "_HeatChart.png";
			saveToFile(new File(pngFileName));
		}
		else if(DataChart.fileType.contentEquals("eps")) {
			String epsFileName = dir + "\\" + fileName + "_HeatChart.eps";
	        saveChartToEPS(epsFileName);	    	
		}
		if(bShow && DataChart.SHOW_OR_NOT) 
			showHeatMap(chartTitle);
	}
	
	private void saveChartToEPS(String epsFileName) {
		// 首先去计算相关的尺寸
		getChartImage(false);
		
		EpsGraphics2D g = new EpsGraphics2D();	 
        drawChartOnGraphicsObject(g);
		try {
			Writer writer = new FileWriter(epsFileName);
			writer.write(g.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void showHeatMap(String chartTitle) {
		
		Dimension expectedSize = DataChart.getGraphSize();
		
		Image chart = getChartImage();
		JFrame window = new JFrame(chartTitle);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(expectedSize.width, expectedSize.height);
  
        ImageIcon imageIcon = new ImageIcon(chart);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jLabel.setSize(expectedSize.width, expectedSize.height);        

        JScrollPane thePane = new JScrollPane(jLabel);
        window.getContentPane().add(thePane, BorderLayout.CENTER);
        
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);	
	}

	public void setScalableCellSize(int column) {
		
		int oldWidth = getCellSize().width;
		int oldHeight = getCellSize().height;
		
		int scalableWidth = oldWidth * DataChart.MAX_X_TICKS / column;
		int scalableHeight = oldHeight * DataChart.MAX_X_TICKS / column;
		
		setCellSize(new Dimension(scalableWidth, scalableHeight));
	}
	
	public static void main(String[] a){
		
		double[][] b = new double[45][45];
		String[] xl = new String[45];
		String[] yl = new String[45];
		for(int i=0;i<45;i++) {
			for(int j=0;j<45;j++) 
				b[i][j] =Math.random();
			xl[i]=String.valueOf(i);
			yl[i]=String.valueOf(i);
		}
		try {
			SimpleHeatChart shc = new SimpleHeatChart(b);
			//shc.setCellSize(new Dimension(10,10));
			shc.show("pp", "fuck", "X", "Y", xl, yl, "d:", true);
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
