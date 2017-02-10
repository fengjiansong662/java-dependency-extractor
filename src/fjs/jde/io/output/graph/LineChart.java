package fjs.jde.io.output.graph;

import java.awt.BasicStroke;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class LineChart extends DataChart {

	/**
	 *Ãı–ŒÕº
	 */
	private static final long serialVersionUID = 1L;

	public LineChart(String title, String xAxis, String yAxis, XYDataset dataset, String[] xLabels, String xType, boolean bLegend) {
		
		super(title);

		JFreeChart chart = null;

		if (xType.contentEquals("number")) {
			chart = ChartFactory.createXYLineChart(title, // chart title
					xAxis, // x axis label
					yAxis, // y axis label
					dataset, // data
					PlotOrientation.VERTICAL, 
					bLegend, // include legend
					true, // tooltips
					false // urls
					);
		} else if (xType.contentEquals("timeline")) {
			chart = ChartFactory.createTimeSeriesChart(title, // chart title
					xAxis, // x axis label
					yAxis, // y axis label
					dataset, // data
					bLegend, // include legend
					true, // tooltips
					false // urls
					);
		}

		XYPlot plot = (XYPlot) chart.getXYPlot();
		plot.setAxisOffset(new RectangleInsets(5.0, 10.0, 10.0, 5.0));

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		int num = dataset.getSeriesCount();

		for (int i = 0; i < num; i++){
			renderer.setSeriesLinesVisible(i, true);
			renderer.setBaseShapesVisible(true);
	        renderer.setBaseShapesFilled(true);
	        renderer.setBaseStroke(new BasicStroke(0));	        
		}
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setRange(0, getDateScope(dataset));
		
		if (xType.contentEquals("timeline")) {
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
			axis.setAutoTickUnitSelection(true);
			axis.setVerticalTickLabels(false);
		}
		else {
			if(xLabels != null) {
				SymbolAxis sa = new SymbolAxis(xAxis, xLabels);	
				sa.setGridBandsVisible(false);
				plot.setDomainAxis(sa);
			}
			else 
				plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		}
		
	    configPlot(chart, plot, plot.getDomainAxis(), rangeAxis, true, true, "standard");
	    setupPanelAndSaveGraphFile(chart);
	}
	
	private double getDateScope(XYDataset d) {
		double max = 0;
		for(int i=0; i<d.getSeriesCount(); i++) {
			for(int j=0; j<d.getItemCount(i); j++) {
				if(d.getYValue(i, j) > max)
					max = d.getYValue(i, j);
			}
		}
		return max * 1.1;
	}

	public static void show(String title, String x, String y,
			XYDataset dataset, String[] xLabels, String type, boolean bLegend, String dir) {
		
		setGraphDirectory(dir);
		LineChart lc = new LineChart(title, x, y, dataset, xLabels, type, bLegend);
		setVisible(lc);
	}
	
	public static void main(String[] k){
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("email");
		XYSeries series2 = new XYSeries("shit");
		String [] x = new String[45];

		for(int i=0;i<45;i++) {
			series.add(i, Math.random());
			series2.add(i, Math.random());
			x[i] = String.valueOf(i);
		}
		dataset.addSeries(series);
		dataset.addSeries(series2);
			
		LineChart.show("Commit Intervals for ",
				"Sequence of Commits",
				"Interval between neighboring commits (days)", dataset, x,
				"number", true, "d:");

	}
}
