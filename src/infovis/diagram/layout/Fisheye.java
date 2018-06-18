package infovis.diagram.layout;

import infovis.debug.Debug;
import infovis.diagram.Model;
import infovis.diagram.View;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Vertex;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * 
 */

public class Fisheye implements Layout{

	//Distortion Factor
	private double d = 5;
	private double c = 0;
	private double e = 0;
	private double VWcutoff = 0;

	public void setMouseCoords(int x, int y, View view) {
	}


	public Model transform(Model model, View view, double focus_point_x, double focus_point_y)
	{
		ArrayList<Vertex> new_vertex= new ArrayList<Vertex>();
		for(Vertex vertex: model.getVertices())
		{
			//Vertex Coordinates
			double p_norm_x = vertex.getX();
			double p_norm_y = vertex.getY();
			Point2D fish_point = F1(view, p_norm_x,p_norm_y,focus_point_x,focus_point_y);

			Point2D fish_point_Q = F1(view,p_norm_x+vertex.getWidth()/2,p_norm_y+vertex.getWidth()/2,focus_point_x,focus_point_y);

			double scale_x = (Math.abs(fish_point_Q.getX() - fish_point.getX()) );
			double scale_y = (Math.abs(fish_point_Q.getY() - fish_point.getY()) );
			double scale_fish = 2 * Math.min(scale_x,scale_y);
			vertex.setX(fish_point.getX());
			vertex.setY(fish_point.getY());
			//vertex.setHeight(scale_fish);
			//vertex.setWidth(scale_fish);
			new_vertex.add(vertex);
		}
		model.removeVertices(model.getVertices());
		model.addVertices(new_vertex);


		return model;
	}

	public Point2D F1(View view, double p_norm_x, double p_norm_y, double focus_point_x, double focus_point_y)
	{
		//Horizontal Distance between Screen  and focus in normal coordinates
		double d_max_x = 0;
		double p_bound_x = view.getBounds().x;
		if(p_norm_x > focus_point_x)
		{
			d_max_x = p_bound_x - focus_point_x;
		}
		else if(p_norm_x < focus_point_x)
		{
			d_max_x = 0 - focus_point_x;
		}

		//Vertical Distance between Screen  and focus in normal coordinates
		double d_max_y = 0;
		double p_bound_y = view.getBounds().y;
		if(p_norm_y > focus_point_y)
		{
			d_max_y = p_bound_y - focus_point_y;
		}
		else if(p_norm_y < focus_point_y)
		{
			d_max_y = 0 - focus_point_y;
		}

		//Distance between transformed point and focus
		double d_norm_x = p_norm_x - focus_point_x;
		double d_norm_y = p_norm_y - focus_point_y;

		//Fisheye Point
		double fish_point_x = G_function(d_norm_x/d_max_x) * d_max_x + focus_point_x;
		double fish_point_y = G_function(d_norm_y/d_max_y) * d_max_y + focus_point_y;

		return new Point2D.Double(fish_point_x,fish_point_y);
	}

	public double G_function(double x)
	{
		return ( (d+1)*x/(d*x+1) );
	}
	
}
