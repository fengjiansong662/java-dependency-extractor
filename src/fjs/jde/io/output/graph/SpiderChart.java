package fjs.jde.io.output.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Paint;

import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.*;

public class SpiderChart extends DataChart {
	/**
		 * ÷©÷ÎÕº
		 */
	private static final long serialVersionUID = 1L;

	public SpiderChart(String title, CategoryDataset dataset) {
		super(title);

		SpiderWebPlot spiderwebplot = new SpiderWebPlot(dataset);
		
		JFreeChart chart = new JFreeChart(title, TextTitle.DEFAULT_FONT, spiderwebplot, false);
		LegendTitle legendtitle = new LegendTitle(spiderwebplot);
		legendtitle.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(legendtitle);
		
		configSpiderPlot(chart, spiderwebplot);
        setupPanelAndSaveGraphFile(chart);
	}
	
	private void configSpiderPlot(JFreeChart chart, SpiderWebPlot plot) {
		
		setupChartTitle(chart);
		setupLegendFont(chart);
		
		chart.setBackgroundPaint(Color.white);
		chart.setBorderVisible(false);
		
		plot.setLabelFont(getAxisFont());
		plot.setSeriesOutlineStroke(new BasicStroke(1));
		plot.setSeriesOutlinePaint(Color.BLACK);
		plot.setAxisLineStroke(new BasicStroke(1));
	}
	
	public static void show(String title, DefaultCategoryDataset dataset, String dir) {

		setGraphDirectory(dir);
		SpiderChart chart = new SpiderChart(title, dataset);
		setVisible(chart);
	}
	
	public static void main(String args[]) {
		DefaultCategoryDataset d = createDataset();
		show("Spider Chart Demo", d, "d:");
	}
	
	private static DefaultCategoryDataset createDataset() {
		String s = "First";
		String s1 = "Second";
		String s2 = "Third";
		String s3 = "Category 1";
		String s4 = "Category 2";
		String s5 = "Category 3";
		String s6 = "Category 4";
		String s7 = "Category 5";
		String s8 = "Category 6";
		String s9 = "Category 7";
		String s10 = "Category 8";
		
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		defaultcategorydataset.addValue(1.0D, s, s3);
		defaultcategorydataset.addValue(4D, s, s4);
		defaultcategorydataset.addValue(3D, s, s5);
		defaultcategorydataset.addValue(5D, s, s6);
		defaultcategorydataset.addValue(5D, s, s7);
		defaultcategorydataset.addValue(5D, s1, s3);
		defaultcategorydataset.addValue(7D, s1, s4);
		defaultcategorydataset.addValue(6D, s1, s10);
		defaultcategorydataset.addValue(8D, s1, s6);
		defaultcategorydataset.addValue(4D, s1, s7);
		defaultcategorydataset.addValue(4D, s2, s3);
		defaultcategorydataset.addValue(3D, s2, s8);
		defaultcategorydataset.addValue(2D, s2, s9);
		defaultcategorydataset.addValue(3D, s2, s6);
		defaultcategorydataset.addValue(6D, s2, s7);
		return defaultcategorydataset;
	}
}
