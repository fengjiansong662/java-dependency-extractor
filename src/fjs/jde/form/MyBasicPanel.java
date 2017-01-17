package fjs.jde.form;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * 基面板
 * @author Guijin.Liang
 *
 */
public class MyBasicPanel extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int isRectOrIsRount;//方形还是圆角方形
	
	private Color fillColor;//填充颜色
	private Color borderColor;//边框颜色
	
	private int width;//宽度;
	private int height;//高度
	
	private int roundValue;//圆角值
	
	public MyBasicPanel(int width
			          , int height
			          , Color borderColor
			          , Color fillColor
			          , int roundValue
			          , int isRectOrIsRound)
	{
		this.width = width;
		this.height = height;
		
		this.fillColor = fillColor;
		this.borderColor = borderColor; 
		this.isRectOrIsRount = isRectOrIsRound;
		this.roundValue = roundValue;
	}
	
    public void paintComponent(Graphics g){
	
        g.setColor(this.borderColor);
		
		if(this.isRectOrIsRount == 1){ //圆角方形
		    g.drawRoundRect(0, 0, this.width - 1, this.height - 1, this.roundValue, this.roundValue);
		}else if(this.isRectOrIsRount == 0){ //方形
			g.drawRect(0, 0, this.width - 1, this.height - 1);
		}
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				                               RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D g2 = (Graphics2D)g;

		g2.addRenderingHints(rh);//消除锯齿
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));//不透明
		g2.setPaint(new GradientPaint(this.getWidth()/2
				                      , 1
				                      , new Color(255,255,255)
		                              , this.getWidth()/2
		                              , this.getHeight() -1
		                              , this.fillColor));
		
		if(this.isRectOrIsRount == 1){//圆角方形
		    g2.fillRoundRect(1, 1, this.width - 2, this.height - 2, this.roundValue, this.roundValue);
		}else if(this.isRectOrIsRount == 0){ //方形
			g2.fillRect(1, 1, this.width - 2, this.height - 2);
		}
    }
}
