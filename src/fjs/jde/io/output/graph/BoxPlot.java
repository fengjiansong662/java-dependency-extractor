package fjs.jde.io.output.graph;

import java.util.ArrayList;

import jde.analysis.AnalysisConfig;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;


public class BoxPlot extends DataChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BoxPlot(String title, String xAxis, String yAxis, String[] xTickLabel, String[] index, double[][] data) {
		super(title);
		
		final DefaultBoxAndWhiskerCategoryDataset boxDataset = createBoxPlotDataset(data, xTickLabel, index);
        
		JFreeChart boxChart = ChartFactory.createBoxAndWhiskerChart(
				title, 
				xAxis,
				yAxis,
				boxDataset, 
				false);   
				
		CategoryPlot plot = (CategoryPlot)boxChart.getPlot();      				
		BoxAndWhiskerRenderer br = (BoxAndWhiskerRenderer) plot.getRenderer();
		br.setMaximumBarWidth(AnalysisConfig.BOX_PLOT_BAR_RATIO); 
		
		NumberAxis axis = (NumberAxis)plot.getRangeAxis();   
		axis.setAutoRangeIncludesZero(true);
	
		configPlot(boxChart, plot, plot.getDomainAxis(), axis, true, true, "standard");
	    setupPanelAndSaveGraphFile(boxChart);
	}
	
	private DefaultBoxAndWhiskerCategoryDataset createBoxPlotDataset(double[][] data, String[] xTickLabel, String[] index) {

		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		
		for(int i=0; i<data.length; i++) {
			ArrayList list = new ArrayList();   
			for(int j=0; j<data[i].length; j++) 
				list.add(new Double(data[i][j]));
			dataset.add(list, index[i], xTickLabel[i]);
		}
		
		return dataset;
	}

	public static void show(String title, String x, String y, String [] xTickLabel,
			String[] index, double[][] data, String dir) {

		setGraphDirectory(dir);
		BoxPlot bp = new BoxPlot(title, x, y, xTickLabel, index, data);
		setVisible(bp);
	}
}