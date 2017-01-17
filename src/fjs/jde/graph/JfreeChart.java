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
					new FileReader("E:\\�汾��.txt"));
		
		String line;
		String[] url = null;
			while ((line = br.readLine()) != null) {
				String versions[]=line.split("  ");
				versionName.put(versions[0], versions[1]);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(); // �������
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
				} else if (messages[1].contains("��")) {

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
		/*dataset.addValue(98, "��ѧ", "����"); 
		dataset.addValue(68, "��ѧ", "����"); 
		dataset.addValue(56, "��ѧ", "����");*/
		JFreeChart chart = ChartFactory.createLineChart(name, // �����������       "�û�ͳ�Ʊ���������λ��"
				"����",// X��ı�ǩ
				"����",// Y��ı�ǩ
				dataset, // ͼ����ʾ�����ݼ���
				PlotOrientation.VERTICAL, // ͼ�����ʾ��ʽ��ˮƽ���ߴ�ֱ��
				true,// �Ƿ���ʾ�ӱ���
				true,// �Ƿ�������ʾ�ı�ǩ
				true); // �Ƿ�����URL���� // ����ͼ���ϵ����� // ���������������
		chart.getTitle().setFont(new Font("����", Font.BOLD, 15)); // �����ӱ�������  NC�Ĵ�С
		chart.getLegend().setItemFont(new Font("����", Font.BOLD, 0)); // ��ȡͼ���������     ������ı�ʾ���ֵĴ�С
		CategoryPlot categoryPlot = (CategoryPlot) chart.getPlot(); // ��ȡX��Ķ���
		CategoryAxis categoryAxis = (CategoryAxis) categoryPlot.getDomainAxis(); // ��ȡY��Ķ���
		NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis(); // ����X���ϵ�����
		categoryAxis.setTickLabelFont(new Font("����", Font.BOLD, 12)); // ����X���������       x�����������Ĵ�С
		categoryAxis.setLabelFont(new Font("����", Font.BOLD, 10)); // ����Y���ϵ�����        ���������Ĵ�С
		numberAxis.setTickLabelFont(new Font("����", Font.BOLD, 10)); // ����Y���������       Y�����ֵĴ�С
		numberAxis.setLabelFont(new Font("����", Font.BOLD, 10)); // ����Y������ʾ�Ŀ̶ȣ���10��Ϊ1��      ���������Ĵ�С
		numberAxis.setAutoTickUnitSelection(false);
		NumberTickUnit unit = new NumberTickUnit(10);
		numberAxis.setTickUnit(unit); // ��ȡ��ͼ�������
		LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) categoryPlot
				.getRenderer(); // ��ͼ������ʾ����
		lineAndShapeRenderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		lineAndShapeRenderer.setBaseItemLabelsVisible(true);
		lineAndShapeRenderer.setBaseItemLabelFont(new Font("����", Font.BOLD, 0)); // ��ͼ�������ת�۵㣨ʹ��С������ʾ��
		Rectangle shape = new Rectangle(1, 1);
		lineAndShapeRenderer.setSeriesShape(0, shape);
		lineAndShapeRenderer.setSeriesShapesVisible(0, true); // ��D��Ŀ¼������ͼƬ
		File file = new File("chart1.jpg");
		try {
			ChartUtilities.saveChartAsJPEG(file, chart, 1000, 800);
		} catch (IOException e) {
			e.printStackTrace();
		} // ʹ��ChartFrame������ʾͼ��
		ChartFrame frame = new ChartFrame("xyz", chart);
		frame.setVisible(true);
		frame.pack();
	}
}
