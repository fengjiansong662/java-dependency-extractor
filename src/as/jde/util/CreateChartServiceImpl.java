package as.jde.util;  
  
import java.awt.Color;  
import java.awt.Font;  
import java.io.File;  
import java.io.FileOutputStream;  
 
  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartUtilities;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.CategoryAxis;  

import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;  

 
import org.jfree.chart.plot.CategoryPlot;   
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;


/** 
 * ʵ��ȡɫ��ʱ��һ��Ҫ16λ�ģ������Ƚ�׼ȷ 
 *  
 * @author new 
 */  
public class CreateChartServiceImpl {  
  
   private static final String CHART_PATH = "E:/test/";  
  
    public static void main(String[] args) {  
        CreateChartServiceImpl pm = new CreateChartServiceImpl();  
      
        // ��������ͼ  
        pm.makeLineAndShapeChart();  
    }  
  
    /** 
     * ��������ͼ 
     */  
    public void makeLineAndShapeChart() {  
        double[][] data = new double[][]{  
            {672, 766, 223, 540, 126},  
            {325, 521, 210, 340, 106},  
            {332, 256, 523, 240, 526}  
        };  
        String[] rowKeys = {"ƻ��", "����", "����"};  
        String[] columnKeys = {"����", "�Ϻ�", "����", "�ɶ�", "����"};  
        CategoryDataset dataset = getBarData(data, rowKeys, columnKeys);  
        createTimeXYChar("����ͼ", "x��", "y��", dataset, "lineAndShap.png");  
    }  
  
  
   
  
    /** 
     * ����ͼ 
     *  
     * @param chartTitle 
     * @param x 
     * @param y 
     * @param xyDataset 
     * @param charName 
     * @return 
     */  
    public String createTimeXYChar(String chartTitle, String x, String y,  
            CategoryDataset xyDataset, String charName) {  
  
        JFreeChart chart = ChartFactory.createLineChart(chartTitle, x, y,  
                xyDataset, PlotOrientation.VERTICAL, true, true, false);  
  
        chart.setTextAntiAlias(false);  
        chart.setBackgroundPaint(Color.WHITE);  
        // ����ͼ�����������������title  
        Font font = new Font("����", Font.BOLD, 25);  
        TextTitle title = new TextTitle(chartTitle);  
        title.setFont(font);  
        chart.setTitle(title);  
        // �����������  
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);  
  
        chart.setBackgroundPaint(Color.WHITE);  
  
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();  
        // x�� // �����������Ƿ�ɼ�  
        categoryplot.setDomainGridlinesVisible(true);  
        // y�� //�����������Ƿ�ɼ�  
        categoryplot.setRangeGridlinesVisible(true);  
  
        categoryplot.setRangeGridlinePaint(Color.WHITE);// ����ɫ��  
  
        categoryplot.setDomainGridlinePaint(Color.WHITE);// ����ɫ��  
  
        categoryplot.setBackgroundPaint(Color.lightGray);  
  
        // ����������֮��ľ���  
        // categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));  
  
        CategoryAxis domainAxis = categoryplot.getDomainAxis();  
  
        domainAxis.setLabelFont(labelFont);// �����  
  
        domainAxis.setTickLabelFont(labelFont);// ����ֵ  
  
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // �����ϵ�  
        // Lable  
        // 45����б  
        // ���þ���ͼƬ��˾���  
  
        domainAxis.setLowerMargin(0.0);  
        // ���þ���ͼƬ�Ҷ˾���  
        domainAxis.setUpperMargin(0.0);  
  
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();  
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
        numberaxis.setAutoRangeIncludesZero(true);  
  
        // ���renderer ע���������������͵�lineandshaperenderer����  
        LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot.getRenderer();  
  
        lineandshaperenderer.setBaseShapesVisible(true); // series �㣨�����ݵ㣩�ɼ�  
  
        lineandshaperenderer.setBaseLinesVisible(true); // series �㣨�����ݵ㣩�������߿ɼ�  
  
        // ��ʾ�۵�����  
        // lineandshaperenderer.setBaseItemLabelGenerator(new  
        // StandardCategoryItemLabelGenerator());  
        // lineandshaperenderer.setBaseItemLabelsVisible(true);  
  
        FileOutputStream fos_jpg = null;  
        try {  
            isChartPathExist(CHART_PATH);  
            String chartName = CHART_PATH + charName;  
            fos_jpg = new FileOutputStream(chartName);  
  
            // ��������Ϊpng�ļ�  
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 500, 510);  
  
            return chartName;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
            try {  
                fos_jpg.close();  
                System.out.println("create time-createTimeXYChar.");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    // ��״ͼ,����ͼ ���ݼ�  
    public CategoryDataset getBarData(double[][] data, String[] rowKeys,  
            String[] columnKeys) {  
        return DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);  
  
    }  
    /** 
     * �ж��ļ����Ƿ���ڣ�������������½� 
     * @param chartPath 
     */  
    private void isChartPathExist(String chartPath) {  
        File file = new File(chartPath);  
        if (!file.exists()) {  
            file.mkdirs();  
        // log.info("CHART_PATH="+CHART_PATH+"create.");  
        }  
    }  
   
}  