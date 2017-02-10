package fjs.jde.io.output.graph;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.render.ps.EPSTranscoder;
import org.apache.lucene.util.IOUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import org.jfree.chart.ChartFactory;  
 
import org.jfree.chart.plot.DatasetRenderingOrder;  

import org.jfree.chart.renderer.category.IntervalBarRenderer;  
 
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultIntervalCategoryDataset;  
import org.jfree.data.category.IntervalCategoryDataset;  
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;  
 

public class DataChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	private static final int titleFont = 20;
	private static final int axisFont = 16;
	private static final int tickFont = 14;
	private static final int legendFont = 14;
	private static final Color titleColor = Color.BLUE;
	private static final String font = "Tahoma";

	protected static final int width = 1100;
	protected static final int height = 800;
	public static final boolean SHOW_OR_NOT = true;
	public static final int MAX_X_TICKS = 45;
	public static final String fileType = "eps";


	protected static String graphDir;

	public DataChart(String title) {
		super(title);
	}

	protected static void setGraphDirectory(String dir) {
		graphDir = dir;
	}

	public static Font getTitleFont() {
		return new Font(font, Font.BOLD, titleFont);
	}

	public static Color getTitleColor() {
		return titleColor;
	}

	public static Font getAxisFont() {
		return new Font(font, Font.BOLD, axisFont);
	}

	public static Font getLegendFont() {
		return new Font(font, Font.PLAIN, legendFont);
	}

	public static Font getTickFont() {
		return new Font(font, Font.PLAIN, tickFont);
	}

	public static Dimension getGraphSize() {
		return new Dimension(width, height);
	}

	protected static void setVisible(DataChart dc) {

		// 统一控制所有图形是否展示在窗口
		if (!SHOW_OR_NOT)
			return;

		dc.pack();
		RefineryUtilities.centerFrameOnScreen(dc);
		dc.setVisible(true);
	}

	protected void setupPanelAndSaveGraphFile(JFreeChart chart) {
		final ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(width, height));
		setContentPane(panel);

		saveChartToFile(chart);
	}

	private void saveChartToFile(JFreeChart chart) {

		if (fileType.contentEquals("svg"))
			saveChartToSVG(chart);
		else if (fileType.contentEquals("png"))
			saveChartToPNG(chart);
		else if (fileType.contentEquals("jpg"))
			saveChartToJPG(chart);
		else if (fileType.contentEquals("eps"))
			saveChartToEPS(chart);
	}

	protected void configPlot(JFreeChart chart, Plot cp, Axis xAxis,
			ArrayList<Axis> yAxisList, boolean bShowXLabels,
			boolean bXLabelRotate, String bYLabelType) {

		// 设置标题
		setupChartTitle(chart);
		chart.setBackgroundPaint(Color.white);

		// 设置Legend
		setupLegendFont(chart);

		// 设置X轴
		if (bXLabelRotate) {
			if (xAxis instanceof CategoryAxis)
				((CategoryAxis) xAxis)
						.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		}

		xAxis.setTickLabelsVisible(bShowXLabels);
		setupAxisFont(xAxis);

		// 设置Y轴
		for (Axis yAxis : yAxisList) {
			setupAxisFont(yAxis);
			if (bYLabelType == null) {
			} else if (bYLabelType.contentEquals("standard"))
				((ValueAxis) yAxis).setStandardTickUnits(NumberAxis
						.createStandardTickUnits());
			else if (bYLabelType.contentEquals("integer"))
				((ValueAxis) yAxis).setStandardTickUnits(NumberAxis
						.createIntegerTickUnits());
		}

		// 设置图形
		cp.setBackgroundPaint(Color.white);
		if (cp instanceof CategoryPlot) {
			((CategoryPlot) cp).setOrientation(PlotOrientation.VERTICAL);
			((CategoryPlot) cp).setDomainGridlinePaint(Color.white);
			((CategoryPlot) cp).setRangeGridlinePaint(Color.white);

			// 此处针对CatgoryPlot，如果其横轴数据超过45个，则缩小显示横轴上的标签
			int column = ((CategoryPlot) cp).getDataset().getColumnCount();
			if (isLargeColumnChart(column))
				setupScalableTickFont(xAxis, column);
			
			CategoryItemRenderer renderer = ((CategoryPlot) cp).getRenderer(0);
			if(renderer instanceof BarRenderer) {
		        //不要显示bar后面的阴影
		        ((BarRenderer) renderer).setBarPainter(new StandardBarPainter());
		        ((BarRenderer) renderer).setShadowVisible(false);
			}
	        
		} else if (cp instanceof XYPlot) {
			((XYPlot) cp).setDomainGridlinePaint(Color.white);
			((XYPlot) cp).setRangeGridlinePaint(Color.white);
			
			XYItemRenderer renderer = ((XYPlot) cp).getRenderer();
			if(renderer instanceof XYBarRenderer) {
		        //去掉阴影
		        ((XYBarRenderer) renderer).setShadowVisible(false);
		        ((XYBarRenderer) renderer).setBarPainter(new StandardXYBarPainter());
		        ((XYBarRenderer) renderer).setMargin(0.2);
		        
			}
		}
	}

	public static Font getScalableTickFont(int column) {
		int scalableFont = tickFont * MAX_X_TICKS / column;
		return new Font(font, Font.PLAIN, scalableFont);
	}

	private void setupScalableTickFont(Axis axis, int column) {
		axis.setTickLabelFont(getScalableTickFont(column));
	}

	protected void setupLegendFont(JFreeChart chart) {
		LegendTitle legend = chart.getLegend();
		if (legend != null)
			legend.setItemFont(getLegendFont());
	}

	protected void setupAxisFont(Axis axis) {
		axis.setLabelFont(getAxisFont());
		axis.setTickLabelFont(getTickFont());
	}

	protected void setupChartTitle(JFreeChart chart) {
		chart.getTitle().setPaint(titleColor);
		chart.getTitle().setFont(getTitleFont());
	}

	protected void configPlot(JFreeChart chart, Plot cp, Axis xAxis,
			Axis yAxis, boolean bShowXLabels, boolean bXLabelRotate,
			String bYLabelType) {

		ArrayList<Axis> axisList = new ArrayList<Axis>();
		axisList.add(yAxis);
		configPlot(chart, cp, xAxis, axisList, bShowXLabels, bXLabelRotate,
				bYLabelType);
	}

	public static boolean isAntiAliasOn(JFreeChart chart) {
		boolean result = false;

		if (chart != null) {
			RenderingHints rh = chart.getRenderingHints();
			if (rh != null) {
				Object o = rh.get(RenderingHints.KEY_ANTIALIASING);
				if (o != null && o.equals(RenderingHints.VALUE_ANTIALIAS_ON)) {
					result = true;
				}
			}
		}

		return result;
	}

	static public final void saveChartToEPS(final JFreeChart chart) {

		String fileName = getGraphFileName(chart, ".eps");
		EpsGraphics2D g = new EpsGraphics2D();
		chart.draw(g, new Rectangle2D.Double(0, 0, width, height), new ChartRenderingInfo());
		try {
			Writer writer = new FileWriter(fileName);
			writer.write(g.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveChartToPNG(final JFreeChart chart) {
		String fileName = getGraphFileName(chart, ".png");
		FileOutputStream out;
		try {
			out = new FileOutputStream(fileName);
			ChartUtilities.writeChartAsPNG(out, chart, width, height);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveChartToJPG(final JFreeChart chart) {
		String fileName = getGraphFileName(chart, ".jpg");
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			ChartUtilities.writeChartAsJPEG(out, chart, width, height);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public final void saveChartToSVG(final JFreeChart chart) {

		String fileName = getGraphFileName(chart, ".svg");

		final DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		final Document document = domImpl.createDocument(null, "svg", null);
		final SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		final boolean antiAlias = isAntiAliasOn(chart);
		final RenderingHints rh = chart.getRenderingHints();

		if (antiAlias) {
			rh.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		} else {
			rh.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		/*chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height),
				new ChartRenderingInfo());*/
		chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, width, height), new ChartRenderingInfo());
		if (antiAlias) {
			rh.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		} else {
			rh.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		}

		final boolean useCSS = true;
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(new BufferedOutputStream(
					new FileOutputStream(new File(fileName), false)),
					"iso-8859-1");
			svgGenerator.stream(writer, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			svgGenerator.dispose();
			try {
				IOUtils.close(writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean transformSVG2EPS(String svg, String eps) {
		try {
			EPSTranscoder trans = new EPSTranscoder();
			OutputStream writer = new FileOutputStream(eps);

			TranscoderInput input = new TranscoderInput(new FileInputStream(svg));
			TranscoderOutput output = new TranscoderOutput(writer);

			trans.transcode(input, output);
			writer.flush();
			writer.close();
			
		} catch (TranscoderException e) {
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	private static String getGraphFileName(JFreeChart chart, String type) {

		String fileName = "";

		String chartTitle = chart.getTitle().getText();
		if (chartTitle != null && chartTitle != "")
			fileName = chartTitle.replace("/", "_");
		else
			fileName = "chart_" + Double.valueOf(Math.random());

		fileName = graphDir + "\\" + fileName + type;

		return fileName;
	}

	public static boolean isLargeColumnChart(int column) {
		if (column > MAX_X_TICKS)
			return true;
		return false;
	}
}