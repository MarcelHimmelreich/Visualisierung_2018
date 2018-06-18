package infovis.paracoords;

import infovis.debug.Debug;
import infovis.scatterplot.Model;
import infovis.scatterplot.Data;
import java.awt.geom.Line2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class MouseController implements MouseListener, MouseMotionListener {
	private View view = null;
	private Model model = null;
	private int x ;
	private int y ;
	private boolean label_hit = false;
	private int label_index = 0;
	Shape currentShape = null;
	
	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
		//Check Label Line Intersection
		for(int i = 0; i < view.line.size();++i)
		{
			if(view.line.get(i).intersects(x,y,5,5))
			{
				label_index = i;
				label_hit = true;
				Debug.println("line hit");
			}
		}

		//Check Data Line Intersection
		for(int i = 0; i <view.line_data.size();++i)
		{
			for(Line2D.Double lines : view.line_data.get(i))
			{
				if(lines.intersects(x,y,5,5))
				{
					System.out.print("Line index: "+i);
					if(!view.line_data_index.contains((Integer)i))
					{
						view.line_data_index.add((Integer) i);
					}
					else
					{
						view.line_data_index.remove((Integer)i);
					}
				}
			}
		}
		view.repaint();

	}

	public void mouseReleased(MouseEvent e) {
		label_hit = false;
		view.repaint();

	}

	public void mouseDragged(MouseEvent e)
	{
		//Offset Label Movement
		if(label_hit)
		{
			int offset = e.getX() - x;
			view.line_offset.set(label_index,offset);
		}
		view.repaint();
	}

	public void mouseMoved(MouseEvent e) {

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
