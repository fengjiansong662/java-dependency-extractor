package fjs.jde.io.output.graph;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class HistoGraph extends DataChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HistoGraph(String title, String xAxis, String yAxis, HistogramDataset dataset) {
		super(title); 

        JFreeChart chart = ChartFactory.createHistogram(
	    		   title, 
	    		   xAxis, 
	    		   yAxis, 
	               dataset, 
	               PlotOrientation.VERTICAL, 
	               false, 
	               true, 
	               false);
	    
	    XYPlot cp = chart.getXYPlot();
	    ValueAxis domainAxis = (ValueAxis)cp.getDomainAxis();
        NumberAxis numberAxis = (NumberAxis) cp.getRangeAxis();
        domainAxis.setAutoTickUnitSelection(true);
                        
	    configPlot(chart, cp, domainAxis, numberAxis, true, false, null);
	    setupPanelAndSaveGraphFile(chart);   
	}
	
	public static void show(String title, String x, String y, double[] data, int numBinHistograms, String dir) {
		setGraphDirectory(dir);
		HistogramDataset dataset = createHistogramDataset(data, numBinHistograms);
		HistoGraph hg = new HistoGraph(title, x, y, dataset);
		setVisible(hg);
	}
	
	public static HistogramDataset createHistogramDataset(double[] data, int bin) {
		HistogramDataset dataset = new HistogramDataset();
	    dataset.setType(HistogramType.RELATIVE_FREQUENCY);
	    dataset.addSeries("Histogram",data,bin);
	    return dataset;
	}
}