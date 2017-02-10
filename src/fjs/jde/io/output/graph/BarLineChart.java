package fjs.jde.io.output.graph;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarLineChart extends DataChart {

	private static final long serialVersionUID = 1L;

	public BarLineChart(String title, String x, String y1, String y2,
			DefaultCategoryDataset barData, DefaultCategoryDataset lineData) {
		super(title);

		final CategoryItemRenderer barRenderer = new BarRenderer();		
		barRenderer.setBaseItemLabelsVisible(true);
        
		final CategoryPlot plot = new CategoryPlot();
		plot.setDataset(barData);
		plot.setRenderer(barRenderer);
	
		CategoryAxis domainAxis = new CategoryAxis(x); 
		plot.setDomainAxis(domainAxis);
		ValueAxis barRangeAxis = new NumberAxis(y1); 
		plot.setRangeAxis(barRangeAxis);
		
		ValueAxis lineRangeAxis = new NumberAxis(y2);
		plot.setRangeAxis(1, lineRangeAxis);
		plot.setDataset(1, lineData);
		
		final LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
		lineRenderer.setSeriesLinesVisible(0, false);
		lineRenderer.setSeriesShapesVisible(0, true);
		lineRenderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
		
		plot.setRenderer(1, lineRenderer);
        plot.mapDatasetToRangeAxis(1, 1);
		
		plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

		final JFreeChart chart = new JFreeChart(plot);
		chart.setTitle(title);

		ArrayList<Axis> numberAxisList = new ArrayList<Axis>();
        numberAxisList.add(barRangeAxis);
        numberAxisList.add(lineRangeAxis);
        
        configPlot(chart, plot, domainAxis, numberAxisList, true, true, null);
        barRangeAxis.setRange(0, 1.5);
        lineRangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());	
        
        setupPanelAndSaveGraphFile(chart);
	}

	public static void show(String title, String x, String y1, String y2,
			DefaultCategoryDataset data1, DefaultCategoryDataset data2, String dir) {
		setGraphDirectory(dir);
		BarLineChart blc = new BarLineChart(title, x, y1, y2, data1, data2);
		setVisible(blc);
	}
}
