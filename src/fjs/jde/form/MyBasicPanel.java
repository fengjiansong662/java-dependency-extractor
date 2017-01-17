package fjs.jde.form;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

/**
 * �����
 * @author Guijin.Liang
 *
 */
public class MyBasicPanel extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int isRectOrIsRount;//���λ���Բ�Ƿ���
	
	private Color fillColor;//�����ɫ
	private Color borderColor;//�߿���ɫ
	
	private int width;//���;
	private int height;//�߶�
	
	private int roundValue;//Բ��ֵ
	
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
		
		if(this.isRectOrIsRount == 1){ //Բ�Ƿ���
		    g.drawRoundRect(0, 0, this.width - 1, this.height - 1, this.roundValue, this.roundValue);
		}else if(this.isRectOrIsRount == 0){ //����
			g.drawRect(0, 0, this.width - 1, this.height - 1);
		}
		
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				                               RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D g2 = (Graphics2D)g;

		g2.addRenderingHints(rh);//�������
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));//��͸��
		g2.setPaint(new GradientPaint(this.getWidth()/2
				                      , 1
				                      , new Color(255,255,255)
		                              , this.getWidth()/2
		                              , this.getHeight() -1
		                              , this.fillColor));
		
		if(this.isRectOrIsRount == 1){//Բ�Ƿ���
		    g2.fillRoundRect(1, 1, this.width - 2, this.height - 2, this.roundValue, this.roundValue);
		}else if(this.isRectOrIsRount == 0){ //����
			g2.fillRect(1, 1, this.width - 2, this.height - 2);
		}
    }
}
