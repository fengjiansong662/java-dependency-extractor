package fjs.jde.io.output.graph;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart extends DataChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BarChart(String title, String xAxis, String yAxis, CategoryDataset bardataset, boolean bShowXLabels) {
		super(title);

		JFreeChart barchart = ChartFactory.createBarChart(
				title, 						// Title
				xAxis, 						// X-axis Label
				yAxis, 						// Y-axis Label
				bardataset, 				// Dataset
				PlotOrientation.VERTICAL, 	// Plot orientation
				false, 						// Show legend
				true, 						// Use tooltips
				false 						// Generate URLs
				);

		CategoryPlot cp = barchart.getCategoryPlot(); // Get the Plot object for a bar graph
        CategoryAxis domainAxis = (CategoryAxis)cp.getDomainAxis();
        
        ArrayList<Axis> numberAxisList = new ArrayList<Axis>();
        numberAxisList.add((NumberAxis) cp.getRangeAxis());

        configPlot(barchart, cp, domainAxis, numberAxisList, bShowXLabels, true, null);
        setupPanelAndSaveGraphFile(barchart);
	}

	public BarChart(String title, String x, String y1, String y2,
			DefaultCategoryDataset dataset1, DefaultCategoryDataset dataset2,
			boolean bShowXLabels) {
		
		super(title);
		
        final CategoryAxis domainAxis = new CategoryAxis(x);
        final NumberAxis y1Axis = new NumberAxis(y1);
        final BarRenderer renderer1 = new BarRenderer();
        renderer1.setShadowVisible(false);
        renderer1.setBarPainter(new StandardBarPainter());
        
        final CategoryPlot plot = new CategoryPlot(dataset1, domainAxis, y1Axis, renderer1) {
        	
            public LegendItemCollection getLegendItems() {
                final LegendItemCollection result = new LegendItemCollection();
                final CategoryDataset data = getDataset();
                if (data != null) {
                    final CategoryItemRenderer r = getRenderer();
                    if (r != null) {
                        final LegendItem item = r.getLegendItem(0, 0);
                        result.add(item);
                    }
                }

                final CategoryDataset dset2 = getDataset(1);
                if (dset2 != null) {
                    final CategoryItemRenderer renderer2 = getRenderer(1);
                    ((BarRenderer) renderer2).setShadowVisible(false);
                    ((BarRenderer) renderer2).setBarPainter(new StandardBarPainter());
                    if (renderer2 != null) {
                        final LegendItem item = renderer2.getLegendItem(1, 1);
                        result.add(item);
                    }
                }
                return result;
            }            
        };              
        ((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());

        final JFreeChart chart = new JFreeChart(title, plot);
        
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        
        final NumberAxis y2Axis = new NumberAxis(y2);
        plot.setRangeAxis(1, y2Axis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        final BarRenderer renderer2 = new BarRenderer();
        plot.setRenderer(1, renderer2);
                
        ArrayList<Axis> numberAxisList = new ArrayList<Axis>();
        numberAxisList.add(y1Axis);
        numberAxisList.add(y2Axis);
        
        configPlot(chart, plot, domainAxis, numberAxisList, bShowXLabels, true, null);
        setupPanelAndSaveGraphFile(chart);
	}

	public static void show(String title, String x, String y,
			DefaultCategoryDataset bardataset, boolean bShowXLabels, String dir) {
		setGraphDirectory(dir);
		BarChart bc = new BarChart(title, x, y, bardataset, bShowXLabels);
		setVisible(bc);
	}

	public static void show(String title, String x, String y1,
			String y2, DefaultCategoryDataset dataset1, DefaultCategoryDataset dataset2, 
			boolean bShowXLabels, String dir) {
		setGraphDirectory(dir);
		BarChart bc = new BarChart(title, x, y1, y2, dataset1, dataset2, bShowXLabels);
		setVisible(bc);
	}
}
