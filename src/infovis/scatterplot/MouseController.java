package infovis.scatterplot;

import org.w3c.dom.css.Rect;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class MouseController implements MouseListener, MouseMotionListener {

	private Model model = null;
	private View view = null;
	private double press_x;
	private double press_y;

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		press_x = arg0.getX();
		press_y = arg0.getY();

		//Iterator<Data> iter = model.iterator();
		//view.getMarkerRectangle().setRect(x,y,w,h);
		view.repaint();
	}

	public void mouseReleased(MouseEvent arg0)
	{
		double x = arg0.getX();
		double y = arg0.getY();
		model.setMarked(new ArrayList<Data>());
		//clear Marker
		//Rectangle2D rect = new Rectangle2D.Double(0,0,0,0);
		//view.setMarkerRectangle(rect);
		view.repaint();

	}

	public void mouseDragged(MouseEvent arg0)
	{
		view.setMarkerRectangle(new Rectangle2D.Double(press_x,press_y,arg0.getX()-press_x,arg0.getY()-press_y));
		view.repaint();
	}

	public void mouseMoved(MouseEvent arg0)
	{

	}

	public void setModel(Model model) {
		this.model  = model;	
	}

	public void setView(View view) {
		this.view  = view;
	}

}
