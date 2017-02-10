package fjs.jde.io.output.graph;

import java.awt.Color;
import java.awt.Shape;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;

public class ScatterChart extends DataChart {

	/**
	 * É¢²¼Í¼
	 */
	private static final long serialVersionUID = 1L;

	public ScatterChart(String title, String x, String y, XYDataset data) {
		super(title);
		
	    JFreeChart chart = ChartFactory.createScatterPlot(
	           title, x, y, data,
	           PlotOrientation.VERTICAL, 
	           false, 
	           true, 
	           false);
	    
	    Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.red);
	    
        configPlot(chart, plot, plot.getDomainAxis(), plot.getRangeAxis(), true, true, null);
        setupPanelAndSaveGraphFile(chart);
	}
	
	public static void show(String title, String x, String y, XYDataset data, String dir) {	
		setGraphDirectory(dir);
		ScatterChart bp = new ScatterChart(title, x, y, data);
		setVisible(bp);
	}
}