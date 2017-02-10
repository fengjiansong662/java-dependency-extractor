package fjs.jde.io.output.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

public class StackedBarChart extends DataChart {

	/**
	 * ¶ÑµþÌõÐÎÍ¼
	 */
	private static final long serialVersionUID = 1L;

	public StackedBarChart(final String title, String x, String y,
			CategoryDataset dataset, boolean bShowXLabels) {
		super(title);

		final JFreeChart chart = ChartFactory.createStackedBarChart(title, 																		
				x,
				y,
				dataset, 
				PlotOrientation.VERTICAL,
				true, 
				true,
				false 
				);

		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.HORIZONTAL));

		SubCategoryAxis domainAxis = new SubCategoryAxis(x);
		domainAxis.setCategoryMargin(0.05);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainAxis(domainAxis);
		plot.setRenderer(renderer);
		plot.setFixedLegendItems(createLegendItems());
		
		configPlot(chart, plot, plot.getDomainAxis(), (NumberAxis) plot.getRangeAxis(), bShowXLabels, true, null);
		setupPanelAndSaveGraphFile(chart);		
	}

	private LegendItemCollection createLegendItems() {
		LegendItemCollection result = new LegendItemCollection();
		return result;
	}

	public static void show(String title, String x, String y, CategoryDataset dataset, boolean bShowXLabels, String dir) {
		setGraphDirectory(dir);
		StackedBarChart bc = new StackedBarChart(title, x, y, dataset, bShowXLabels);
		setVisible(bc);
	}
}