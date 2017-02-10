package fjs.jde.io.output.graph;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import jde.util.Utils;

public class BarCode extends DataChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BarCode(String title, String xAxis, String yAxis, XYDataset dataset) {
		super(title);

		DateAxis domainAxis = new DateAxis(xAxis);
		domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);	
		domainAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
		domainAxis.setAutoTickUnitSelection(true);
		domainAxis.setVerticalTickLabels(false);
		
		NumberAxis rangeAxis = new NumberAxis(yAxis);
		
		StackedXYBarRenderer renderer = new StackedXYBarRenderer(0);
		renderer.setBaseItemLabelsVisible(false);
		renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		renderer.setShadowVisible(false);	

		XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);        
        plot.setDomainGridlinesVisible(false);
        plot.setDomainMinorGridlinesVisible(false);
        plot.setDomainCrosshairVisible(false);
        plot.setDomainCrosshairLockedOnData(false);
        plot.setDomainZeroBaselineVisible(false);

        plot.setRangeGridlinesVisible(false);
        plot.setRangeMinorGridlinesVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setRangeCrosshairLockedOnData(false);
        plot.setRangeZeroBaselineVisible(false);
        
		JFreeChart chart = new JFreeChart(title, plot);
		chart.removeLegend();
		LegendTitle legend = new LegendTitle(plot);
		legend.setFrame(new BlockBorder());
		legend.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(legend);

		configPlot(chart, plot, domainAxis, rangeAxis, true, false, "standard");
		setupPanelAndSaveGraphFile(chart);
	}
	
	public static void show(String t, String x, String y, TimeTableXYDataset d, String dir) {
		setGraphDirectory(dir);
		BarCode bc = new BarCode(t, x, y, d);
		setVisible(bc);
	}
}
