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
	public Rectangle2D marker = new Rectangle2D.Double();
	public Rectangle2D overviewRect = new Rectangle2D.Double();

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
		
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.clearRect(0, 0, getWidth(), getHeight());
		g2D.scale(scale,scale);
		paintDiagram(g2D);
		
		// Overview Rectangle
		g2D.clearRect(0, 0, 220, 200);
		g2D.scale(1/getScale(), 1/getScale());
		g2D.setColor(Color.black);
		overviewRect.setRect(0, 0, 220, 200);
		g2D.draw(overviewRect);

		// Graphics in the Overview
		g2D.scale((getScale()/4)/getScale(), (getScale()/4)/getScale());
		paintDiagram(g2D);

		// Marker
		g2D.scale(1/getScale(), 1/getScale());
		Color markerColor = new Color(255,0,0,80);
		g2D.setColor(markerColor);
		int w = getWidth();
		int h = getHeight();
		if (w > 220*4) {
			w = 220*4;
		}
		if (h > 200*4) {
			h = 200*4;
		}
		marker.setRect(translateX, translateY, w, h);
		g2D.fill(marker);
		g2D.draw(marker);
	

		

//		Double model_width = 0.0;
//		Double model_height = 0.0;
//
//		for (Vertex vertex: model.getVertices() )
//		{
//			if(vertex.getX()+ vertex.getWidth() > model_width)
//			{
//				model_width = vertex.getX() + vertex.getWidth();
//			}
//			if(vertex.getY() + vertex.getHeight()> model_height)
//			{
//				model_height = vertex.getY() + vertex.getHeight();
//			}
//		}
//
//		//Diagram
//		Graphics2D g2D = (Graphics2D) g;
//		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D.clearRect(0, 0, getWidth(), getHeight());
//
//		//Minimap Border
//		Graphics2D overview = (Graphics2D) g;
//
//		//Minimap
//		Graphics2D g2D_map = (Graphics2D) g;
//		g2D_map.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D_map.clearRect(0, 0, getWidth(), getHeight());
//
//		//Minimap Marker
//		Graphics2D marker_g = (Graphics2D) g;
//		g2D_map.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		g2D_map.clearRect(0, 0, getWidth(), getHeight());
//
//		g2D.scale(scale,scale);
//		paintDiagram(g2D);
//
//		overview.setColor(Color.white);
//		overviewRect.setRect(0,0, (int)(overview_x/scale),(int)(overview_y/scale));
//		overview.fillRect(0,0, (int)(overview_x/scale),(int)(overview_y/scale));
//		overview.setColor(Color.black);
//		overview.setStroke(new BasicStroke((float)(1/scale)));
//		overview.draw(overviewRect);
//
//		Double marker_width = overview_x;
//		Double marker_height = overview_y;
//		if(getWidth() < model_width)
//		{
//			marker_width = overview_x*getWidth()/model_width;
//			System.out.println(getWidth() +" "+ model_width +" "+ marker_width);
//		}
//
//		if(getHeight() < model_height)
//		{
//			marker_height = overview_y*getHeight()/model_height;
//			System.out.println(getHeight() +" "+ model_height +" "+ marker_height);
//		}
//
//		marker_g.setColor(Color.RED);
//		marker.setRect(0,0,(int)(marker_width/scale),(int)(marker_height/scale));
//		marker_g.setStroke(new BasicStroke((float)(1/scale)));
//		marker_g.draw(marker);
//
//		//g2D_map.cle
//		Double map_scale_x = overview_x/model_width;
//		Double map_scale_y = overview_y/model_height;
//		g2D_map.scale(map_scale_x/scale,map_scale_y/scale);
//		paintDiagram(g2D_map);

	}
	private void paintDiagram(Graphics2D g2D){
		for (Element element: model.getElements())
		{
			//element.setX(element.getY() +translateX*scale);
			//element.setY(element.getY() +translateY*scale);
			element.paint(g2D);
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
	public void updateMarker(int x, int y){
		marker.setRect(x, y, 16, 10);
	}
	public Rectangle2D getMarker(){
		return marker;
	}
	public boolean markerContains(int x, int y){
		return marker.contains(x, y);
	}
}