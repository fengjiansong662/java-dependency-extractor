package fjs.jde.form;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * 登录按钮
 * @author Guijin.Liang
 *
 */
public class OkButton extends SmallButton{

	/**
	 * 构造函数
	 * @param width 宽度
	 * @param height 高度
	 * @param borderColor 边框颜色
	 * @param fillColor  填充颜色
	 * @param roundValue 圆角大小
	 * @param isRectOrIsRound 圆角还是不圆角（1为圆角，0为不圆角）
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
    	Font font = new Font("某字体",Font.HANGING_BASELINE,14); //字体
    	g.setFont(font);
    	g.drawString("登    录", 10, 17);
	}
	
	public void mouseEntered(MouseEvent e) {
		
		//super.mouseEntered(e);
		Graphics g = this.getGraphics();
		g.setColor(new Color(255,0,0));
    	Font font = new Font("某字体",Font.HANGING_BASELINE,14); //字体
    	g.setFont(font);
    	g.drawString("登    录", 10, 17);
	}
	
	public void mouseExited(MouseEvent e) {
	
		//super.mouseExited(e);
		Graphics g = this.getGraphics();
		g.setColor(new Color(0,0,0));
    	Font font = new Font("某字体",Font.HANGING_BASELINE,14); //字体
    	g.setFont(font);
    	g.drawString("登    录", 10, 17);
	}
}
