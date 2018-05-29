package infovis.paracoords;

import infovis.debug.Debug;
import infovis.scatterplot.Model;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.sound.sampled.Line;
import javax.swing.JPanel;

import infovis.scatterplot.Data;

public class View extends JPanel {
	private Model model = null;
	private int diagram_length = 1800;
	private int diagram_height = 800;
	private int diagram_offset = 50;
	private int label_offset = 20;
	public ArrayList<Line2D> line = new ArrayList<Line2D>();
	public ArrayList<Integer> line_offset = new ArrayList<Integer>();
	private boolean line_b =  true;
	public Point point = new Point();

	@Override
	public void paint(Graphics g)
	{
		//Initialize Lines
		if(line_b)
		{
			for(int i =0; i<model.getDim() ;++i)
			{
				line_offset.add(0);
			}
			line_b =  false;
		}

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		line = new ArrayList<Line2D>();
		for(int i =0; i<model.getDim() ;++i)
		{

			//Draw Lines
			g.setColor(Color.BLACK);
			line.add(new Line2D.Double(diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i), diagram_offset,
					diagram_length/model.getDim()*i + diagram_offset + line_offset.get(i),+diagram_height+diagram_offset));
			g2.draw(line.get(i));

			//Draw Labels
			g.setColor(Color.BLACK);
			g.drawString(model.getLabels().get(i),
					diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i), diagram_offset - label_offset);

			//Draw Range Max
			g.setColor(Color.BLACK);
			g.drawString(Double.toString(model.getRanges().get(i).getMax()),
					diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i), diagram_offset);
			//Draw Range Min
			g.setColor(Color.BLACK);
			g.drawString(Double.toString(model.getRanges().get(i).getMin()),
					diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i), diagram_height+diagram_offset+10);
		}

		for(Data values : model.getList())
		{
			//Draw Data
			for(int i =0; i< model.getDim()-1; ++i)
			{

				//Calculate first point
				int first_point_x = diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i);
				int first_point_y;
				try
				{
					first_point_y = diagram_height + diagram_offset - (int)(diagram_height*(values.getValue(i)- model.getRanges().get(i).getMin()) /
							(model.getRanges().get(i).getMax()-model.getRanges().get(i).getMin()));
				}
				catch(Exception e)
				{
					//Exception for divide by zero
					first_point_y = diagram_height + diagram_offset;
				}

				//Calculate second point
				int second_point_x = diagram_length/getModel().getDim()*(i+1) + diagram_offset+ line_offset.get(i+1);
				int second_point_y;
				try
				{
					second_point_y = diagram_height + diagram_offset - (int)(diagram_height*(values.getValue(i+1)- model.getRanges().get(i+1).getMin()) /
							(model.getRanges().get(i+1).getMax()-model.getRanges().get(i+1).getMin()));
				}
				catch(Exception e)
				{
					//Exception for divide by zero
					second_point_y = diagram_height+diagram_offset;
				}
				Line2D line = new Line2D.Double(first_point_x,first_point_y,second_point_x,second_point_y);
				if(line.intersects(point.x,point.y,2,2))
				{
					ArrayList<Data>  list = model.getMarked();
					list.add(values);
					model.setMarked(list);
				}
				g.setColor(Color.BLACK);


				g.drawLine(first_point_x,first_point_y,
						second_point_x,second_point_y);
			}
		}

		for(Data values : model.getList())
		{
			//Draw Data
			for(int i =0; i< model.getDim()-1; ++i)
			{

				//Calculate first point
				int first_point_x = diagram_length/getModel().getDim()*i + diagram_offset+ line_offset.get(i);
				int first_point_y;
				try
				{
					first_point_y = diagram_height + diagram_offset - (int)(diagram_height*(values.getValue(i)- model.getRanges().get(i).getMin()) /
							(model.getRanges().get(i).getMax()-model.getRanges().get(i).getMin()));
				}
				catch(Exception e)
				{
					//Exception for divide by zero
					first_point_y = diagram_height + diagram_offset;
				}

				//Calculate second point
				int second_point_x = diagram_length/getModel().getDim()*(i+1) + diagram_offset+ line_offset.get(i+1);
				int second_point_y;
				try
				{
					second_point_y = diagram_height + diagram_offset - (int)(diagram_height*(values.getValue(i+1)- model.getRanges().get(i+1).getMin()) /
							(model.getRanges().get(i+1).getMax()-model.getRanges().get(i+1).getMin()));
				}
				catch(Exception e)
				{
					//Exception for divide by zero
					second_point_y = diagram_height+diagram_offset;
				}
				Line2D line = new Line2D.Double(first_point_x,first_point_y,second_point_x,second_point_y);
				if(line.intersects(point.x,point.y,2,2))
				{
					ArrayList<Data>  list = model.getMarked();
					list.add(values);
					model.setMarked(list);
				}

				if(model.getMarked().contains(values))
				{
					g.setColor(Color.RED);
					g.drawLine(first_point_x,first_point_y,
							second_point_x,second_point_y);
				}



			}
		}

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
}
