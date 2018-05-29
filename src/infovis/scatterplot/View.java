package infovis.scatterplot;

import infovis.debug.Debug;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class View extends JPanel {
	     private Model model = null;
	     private Integer Matrix_size = 800;
	     private Integer Offset = 50;
	     private Double Value_size = 1.0;
	     private Rectangle2D markerRectangle = new Rectangle2D.Double(0,0,0,0);
	private Rectangle2D table_item;

	public Rectangle2D getMarkerRectangle() { return markerRectangle;}
	public void setMarkerRectangle(Rectangle2D rect){ markerRectangle = rect;}
		 
		@Override
		public void paint(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g2);
		 	for(int i = 0 ; i < model.getDim();++i)
			{
				//Draw Label Horizontal
				g2.setColor(Color.BLACK);
				g2.drawString(model.getLabels().get(i),
						i*Matrix_size/model.getDim() + Offset,
						Offset/2);

				//Draw Label Vertical
				AffineTransform orig = g2.getTransform();
				AffineTransform rotation = new AffineTransform();
				rotation.setToRotation(Math.PI / 2);
				g2.setTransform(rotation);
				g2.setColor(Color.BLACK);
				g2.drawString(model.getLabels().get(i),
						i*Matrix_size/ model.getDim()+Offset*2,
						-Offset/2);
				g2.setTransform(orig);

				for(int h = 0 ; h < model.getDim();++h)
				{
					//Draw Table
					Rectangle2D table_item= new Rectangle2D.Double(
							i*Matrix_size/model.getDim() + Offset,
							h*Matrix_size/model.getDim() + Offset,
							Matrix_size/model.getDim(),
							Matrix_size/model.getDim());
					g2.setColor(Color.BLACK);
					g2.draw(table_item);

					//Get Ranges
					Double x_range_min = model.getRanges().get(i).getMin();
					Double x_range_max = model.getRanges().get(i).getMax();
					Double y_range_min = model.getRanges().get(h).getMin();
					Double y_range_max = model.getRanges().get(h).getMax();

					//Fill Table with Values
					for(Data values : model.getList())
					{
						Double pos_x = i * Matrix_size/model.getDim()
								+ (Matrix_size/model.getDim()*(values.getValue(i)-x_range_min)/(x_range_max-x_range_min))
								+ (double)Offset;

						Double pos_y = (h+1) * Matrix_size/model.getDim()
								- (Matrix_size/model.getDim()*(values.getValue(h)-y_range_min)/(y_range_max-y_range_min))
								+ (double)Offset - Value_size;
						if(pos_y>=y_range_max)
						{
							pos_y = --pos_y;
						}
						if(pos_x>=x_range_max)
						{
							pos_x = --pos_x;
						}
						//Check if value is marked and add value to marked list
						if(markerRectangle.contains(pos_x,pos_y))
						{
							ArrayList<Data> marked = model.getMarked();
							marked.add(values);
						}
						g2.setColor(Color.BLACK);
						g2.draw(new Rectangle2D.Double(pos_x,pos_y,Value_size,Value_size));
					}
				}
			}

			for(int i = 0 ; i < model.getDim();++i)
			{
				//Draw Label Horizontal
				g2.setColor(Color.BLACK);
				g2.drawString(model.getLabels().get(i),
						i*Matrix_size/model.getDim() + Offset,
						Offset/2);

				//Draw Label Vertical
				AffineTransform orig = g2.getTransform();
				AffineTransform rotation = new AffineTransform();
				rotation.setToRotation(Math.PI / 2);
				g2.setTransform(rotation);
				g2.setColor(Color.BLACK);
				g2.drawString(model.getLabels().get(i),
						i*Matrix_size/ model.getDim()+Offset*2,
						-Offset/2);
				g2.setTransform(orig);

				for(int h = 0 ; h < model.getDim();++h)
				{
					//Draw Table
					Rectangle2D table_item= new Rectangle2D.Double(
							i*Matrix_size/model.getDim() + Offset,
							h*Matrix_size/model.getDim() + Offset,
							Matrix_size/model.getDim(),
							Matrix_size/model.getDim());
					g2.setColor(Color.BLACK);
					g2.draw(table_item);

					//Get Ranges
					Double x_range_min = model.getRanges().get(i).getMin();
					Double x_range_max = model.getRanges().get(i).getMax();
					Double y_range_min = model.getRanges().get(h).getMin();
					Double y_range_max = model.getRanges().get(h).getMax();

					//Fill Table with Values
					for(Data values : model.getList())
					{
						Double pos_x = i * Matrix_size/model.getDim()
								+ (Matrix_size/model.getDim()*(values.getValue(i)-x_range_min)/(x_range_max-x_range_min))
								+ (double)Offset;

						Double pos_y = (h+1) * Matrix_size/model.getDim()
								- (Matrix_size/model.getDim()*(values.getValue(h)-y_range_min)/(y_range_max-y_range_min))
								+ (double)Offset - Value_size;
						if(pos_y>=y_range_max)
						{
							pos_y = --pos_y;
						}
						if(pos_x>=x_range_max)
						{
							pos_x = --pos_x;
						}
						//Check if value is marked and add value to marked list
						if(markerRectangle.contains(pos_x,pos_y))
						{
							ArrayList<Data> marked = model.getMarked();
							marked.add(values);
						}
						//Set Color of unmarked and marked data
						if(model.getMarked().contains(values))
						{
							g2.setColor(Color.RED);
							g2.draw(new Rectangle2D.Double(pos_x,pos_y,Value_size,Value_size));
						}

					}
				}
			}
			g2.setColor(Color.RED);

			g2.draw(markerRectangle);

		 	/*
	        for (String l : model.getLabels()) {
				Debug.print(l);
				Debug.print(",  ");
				Debug.println("");
			}
			for (Range range : model.getRanges()) {
				Debug.print(range.toString());
				Debug.print(",  ");
				Debug.println("");
			}
			for (Data d : model.getList()) {
				Debug.print(d.toString());
				Debug.println("");
			}
	        */
			
		}
		public void setModel(Model model) {
			this.model = model;
		}
}
