package fjs.jde.form;



import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



/**
 * 小按钮基类
 * @author Guijin.liang
 *
 */
public class SmallButton extends MyBasicPanel
                         implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public SmallButton(int width, int height, Color borderColor,
			Color fillColor, int roundValue, int isRectOrIsRound) {
		super(width, height, borderColor, fillColor, roundValue, isRectOrIsRound);
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
