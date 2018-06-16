package infovis.diagram;

import infovis.debug.Debug;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Element;
import infovis.diagram.elements.Vertex;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JFrame;



public class View extends JPanel{
	private Model model = null;
	private Color color = Color.BLUE;
	private double scale = 1;
	private double translateX= 0;
	private double translateY=0;
	private double overview_offset_x = 0;
	private double overview_offset_y = 0;
	public int overview_height = 200;
	public int overview_width = 200;
	public Rectangle2D marker = new Rectangle2D.Double();
	public Rectangle2D overviewRect = new Rectangle2D.Double();
	public Rectangle2D overviewHat = new Rectangle2D.Double();

	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.scale(scale,scale);
		paintDiagram(g2D,false);
		
		// Overview Rectangle
		//Check
		g2D.clearRect((int)(overview_offset_x/getScale()), (int)(overview_offset_y/getScale()), (int)(overview_width/getScale()), (int)(overview_height/getScale()));
		g2D.setColor(Color.black);
		overviewRect.setRect(overview_offset_x/getScale(), overview_offset_y/getScale(), overview_width/getScale(), overview_height/getScale());
		g2D.setStroke(new BasicStroke((float)(1/getScale())));
		g2D.draw(overviewRect);

		g2D.clearRect((int)(overview_offset_x/getScale()), (int)(overview_offset_y/getScale()), (int)(overview_width/getScale()), (int)(overview_height/getScale()));
		g2D.setColor(Color.black);
		overviewRect.setRect(overview_offset_x/getScale(), (overview_height+overview_offset_y)/getScale(), overview_width/getScale(), 10/scale);
		g2D.setStroke(new BasicStroke((float)(1/getScale())));
		g2D.draw(overviewRect);
		g2D.fill(overviewRect);


		//Calculation
		double model_width = model.getModelWidth()* scale;
		double model_height = model.getModelHeight() * scale;
		System.out.println("Model Height: "+model_height);


		// Marker
		double marker_width = overview_width;
		double marker_height = overview_height;

		if(model_width < getWidth()) {  }
		else
			{
				marker_width = getWidth()/model_width;
				if(marker_width>1) { marker_width = 1; }
				marker_width = overview_width * marker_width;
			}

		if(model_height < getHeight()) {  marker_height = overview_height;}
		else
			{
				marker_height = getHeight()/model_height;
				if(marker_height>1) { marker_height = 1;}
				marker_height = overview_height * marker_height;

			}

		Color markerColor = new Color(255,0,0,80);
		g2D.setColor(markerColor);
		g2D.setStroke(new BasicStroke((float)(1/getScale())));
		g2D.scale(1/scale,1/scale);
		g2D.clearRect((int)(translateX+overview_offset_x), (int)(translateY+overview_offset_y), (int)(marker_width/scale), (int)(marker_height/scale));
		marker.setRect(translateX+overview_offset_x, translateY+overview_offset_y, marker_width/scale, marker_height/scale);
		g2D.fill(marker);
		g2D.draw(marker);


		// Graphics in the Overview
		g2D.scale((overview_width/model_width*getScale())/getScale(),(overview_height/model_height*getScale())/getScale());
		paintDiagram(g2D,true);


	}

	private void paintDiagram(Graphics2D g2D, boolean minimap){
		for (Element element: model.getElements())
		{
			double x = element.getX();
			double y = element.getY();

			if(!minimap)
			{
				//Translate Elements
				element.setX(element.getX() -translateX*(model.getModelWidth()/overview_width*scale));
				element.setY(element.getY() -translateY*(model.getModelHeight()/overview_width*scale));
			}
			else
			{
				//Translate Minimap Elements
				element.setX(element.getX() +overview_offset_x/(overview_width/model.getModelWidth()) );
				element.setY(element.getY() +overview_offset_y/(overview_width/model.getModelHeight()) );
			}
			element.paint(g2D);
			element.setX(x);
			element.setY(y);
		}
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale(){
		return scale;
	}
	public double getTranslateX() {
		return translateX;
	}
	public void setTranslateX(double translateX) {
		this.translateX = translateX;
	}
	public double getTranslateY() {
		return translateY;
	}
	public void setTranslateY(double tansslateY) {
		this.translateY = tansslateY;
	}
	public void updateTranslation(double x, double y){
		setTranslateX(x);
		setTranslateY(y);
	}

	public double getOverviewOffsetX(){return overview_offset_x;}
	public double getOverviewOffsetY(){return overview_offset_y;}
	public void setOverviewOffsetX(double offset){overview_offset_x = offset;}
	public void setOverviewOffsetY(double offset){overview_offset_y = offset;}

	public void updateMarker(int x, int y){
		marker.setRect(x, y, 16, 10);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}

	public Rectangle2D getOverviewRect(){return overviewRect;}
	public boolean overviewContains(int x, int y){ return overviewRect.contains(x, y); }
}