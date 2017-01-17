package fjs.jde.form;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * ��¼��ť
 * @author Guijin.Liang
 *
 */
public class OkButton extends SmallButton{

	/**
	 * ���캯��
	 * @param width ���
	 * @param height �߶�
	 * @param borderColor �߿���ɫ
	 * @param fillColor  �����ɫ
	 * @param roundValue Բ�Ǵ�С
	 * @param isRectOrIsRound Բ�ǻ��ǲ�Բ�ǣ�1ΪԲ�ǣ�0Ϊ��Բ�ǣ�
	 */
	public OkButton(int width, int height, Color borderColor,
			Color fillColor, int roundValue, int isRectOrIsRound) {
		super(width, height, borderColor, fillColor, roundValue, isRectOrIsRound);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(new Color(0,0,0));
    	Font font = new Font("ĳ����",Font.HANGING_BASELINE,14); //����
    	g.setFont(font);
    	g.drawString("��    ¼", 10, 17);
	}
	
	public void mouseEntered(MouseEvent e) {
		
		//super.mouseEntered(e);
		Graphics g = this.getGraphics();
		g.setColor(new Color(255,0,0));
    	Font font = new Font("ĳ����",Font.HANGING_BASELINE,14); //����
    	g.setFont(font);
    	g.drawString("��    ¼", 10, 17);
	}
	
	public void mouseExited(MouseEvent e) {
	
		//super.mouseExited(e);
		Graphics g = this.getGraphics();
		g.setColor(new Color(0,0,0));
    	Font font = new Font("ĳ����",Font.HANGING_BASELINE,14); //����
    	g.setFont(font);
    	g.drawString("��    ¼", 10, 17);
	}
}
