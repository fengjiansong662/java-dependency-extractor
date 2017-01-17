package fjs.jde.graph;

import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class JfreeChart {
	public static void main(String[] args) {
		
		BufferedReader br;
		Map<String,String> versionName=new HashMap<String,String>();
		try {
			br = new BufferedReader(
					new FileReader("E:\\版本号.txt"));
		
		String line;
		String[] url = null;
			while ((line = br.readLine()) != null) {
				String versions[]=line.split("  ");
				versionName.put(versions[0], versions[1]);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(); // 添加数据
		String name=null;
		String txt="E:\\LOC.txt";
		try {
			br = new BufferedReader(
					new FileReader(txt));
		
		String line;
		String[] url = null;
		int count=0;
		List<String> contaion=new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String messages[]=line.split(":");
				//System.out.println(messages[1].contains("("));
				if (messages[1].contains("(")) {
					
					name = messages[1].split("\\(")[0];
					String classname = messages[1].substring(name.length() + 1,
							messages[1].length() - 2);
					String shortClassNames[] = classname.split("\\.");
					String shortClassName = shortClassNames[shortClassNames.length - 1];
					System.out.println(shortClassName);
					double a = 0;
					try {
						a = Double.parseDouble(messages[2]);// Integer.parseInt(messages[2])
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					System.out.println(messages[0].substring(0, 6));

					/*
					 * if(!contaion.contains(shortClassName)){ if(count<9){
					 * count++; System.out.println(count);
					 * dataset.addValue(a,shortClassName
					 * ,messages[0].substring(0, 6)); } }else{
					 * dataset.addValue(a
					 * ,shortClassName,messages[0].substring(0, 6)); }
					 */

					/*
					 * if(!contaion.contains(shortClassName)){ if(count<20){
					 * count++;
					 * 
					 * }else if(count<40){ count++; System.out.println(count);
					 * dataset
					 * .addValue(a,shortClassName,messages[0].substring(0, 6));
					 * } }else{
					 * dataset.addValue(a,shortClassName,messages[0].substring
					 * (0, 6)); }
					 */

					// if(shortClassName.equals("comalibabaottercanalparsedrivermysqlpacketsCommandPacket")||shortClassName.equals("comalibabaottercanalcommonzookeeperZooKeeperx")||shortClassName.equals("comalibabaottercanalprotocolBuilder")||shortClassName.equals("comalibabaottercanalparseindexPeriodMixedLogPositionManager")||shortClassName.equals("comalibabaottercanalparsedrivermysqlutilsByteHelper")){//shortClassName.equals("SessionAuditAction")||shortClassName.equals("ScriptAction")||shortClassName.equals("SessionOutput")||
					// dataset.addValue(a,shortClassName,versionName.get(messages[0]));
					dataset.addValue(a, shortClassName,
							messages[0].substring(0, 6));

					// }
				} else if (messages[1].contains("的")) {

				} else {
					name = txt.split("\\\\")[txt.split("\\\\").length - 1]
							.substring(
									0,
									txt.split("\\\\")[txt.split("\\\\").length - 1]
											.length() - 4);
					double a = 0;
					try {
						a = Double.parseDouble(messages[2]);// Integer.parseInt(messages[2])
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
					dataset.addValue(a, name, messages[0].substring(0, 6));
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		/*dataset.addValue(98, "数学", "张三"); 
		dataset.addValue(68, "数学", "李四"); 
		dataset.addValue(56, "数学", "王五");*/
		JFreeChart chart = ChartFactory.createLineChart(name, // 主标题的名称       "用户统计报表（所属单位）"
				"类名",// X轴的标签
				"数量",// Y轴的标签
				dataset, // 图标显示的数据集合
				PlotOrientation.VERTICAL, // 图像的显示形式（水平或者垂直）
				true,// 是否显示子标题
				true,// 是否生成提示的标签
				true); // 是否生成URL链接 // 处理图形上的乱码 // 处理主标题的乱码
		chart.getTitle().setFont(new Font("宋体", Font.BOLD, 15)); // 处理子标题乱码  NC的大小
		chart.getLegend().setItemFont(new Font("宋体", Font.BOLD, 0)); // 获取图表区域对象     最下面的标示的字的大小
		CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot(); // 获取X轴的对象
		CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis(); // 获取Y轴的对象
		NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis(); // 处理X轴上的乱码
		categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 处理X轴外的乱码       x轴上坐标名的大小
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 10)); // 处理Y轴上的乱码        “类名”的大小
		numberAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 10)); // 处理Y轴外的乱码       Y轴数字的大小
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 10)); // 处理Y轴上显示的刻度，以10作为1格      “数量”的大小
		numberAxis.setAutoTickUnitSelection(false);
		NumberTickUnit unit = new NumberTickUnit(10);
		numberAxis.setTickUnit(unit); // 获取绘图区域对象
		LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) categoryPlot
				.getRenderer(); // 在图形上显示数字
		lineAndShapeRenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineAndShapeRenderer.setBaseItemLabelsVisible(true);
		lineAndShapeRenderer.setBaseItemLabelFont(new Font("宋体", Font.BOLD, 0)); // 在图形上添加转折点（使用小矩形显示）
		Rectangle shape = new Rectangle(1, 1);
		lineAndShapeRenderer.setSeriesShape(0, shape);
		lineAndShapeRenderer.setSeriesShapesVisible(0, true); // 在D盘目录下生成图片
		File file = new File("chart1.jpg");
		try {
			ChartUtilities.saveChartAsJPEG(file, chart, 1000, 800);
		} catch (IOException e) {
			e.printStackTrace();
		} // 使用ChartFrame对象显示图像
		ChartFrame frame = new ChartFrame("xyz", chart);
		frame.setVisible(true);
		frame.pack();
	}
}
