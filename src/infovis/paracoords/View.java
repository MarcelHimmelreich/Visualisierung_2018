package infovis.paracoords;

import infovis.debug.Debug;
import infovis.scatterplot.Model;

import java.awt.Graphics;

import javax.swing.JPanel;

import infovis.scatterplot.Data;

public class View extends JPanel {
	private Model model = null;
	private int diagram_length = 1800;
	private int diagram_height = 800;
	private int diagram_offset = 50;
	private int label_offset = 20;

	@Override
	public void paint(Graphics g)
	{


		for(int i =0; i<model.getDim() ;++i)
		{
			//Draw Lines
			g.drawLine(diagram_length/getModel().getDim()*i + diagram_offset, diagram_offset,
					diagram_length/model.getDim()*i + diagram_offset,+diagram_height+diagram_offset);

			//Draw Labels
			g.drawString(model.getLabels().get(i),
					diagram_length/getModel().getDim()*i + diagram_offset, diagram_offset - label_offset);

			//Draw Range Max
			g.drawString(Double.toString(model.getRanges().get(i).getMax()),
					diagram_length/getModel().getDim()*i + diagram_offset, diagram_offset);
			//Draw Range Min
			g.drawString(Double.toString(model.getRanges().get(i).getMin()),
					diagram_length/getModel().getDim()*i + diagram_offset, diagram_height+diagram_offset+10);
		}

		for(Data values : model.getList())
		{
			//Draw Data
			for(int i =0; i< model.getDim()-1; ++i)
			{

				//Calculate first point
				int first_point_x = diagram_length/getModel().getDim()*i + diagram_offset;
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
				int second_point_x = diagram_length/getModel().getDim()*(i+1) + diagram_offset;
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

				g.drawLine(first_point_x,first_point_y,
						second_point_x,second_point_y);
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
