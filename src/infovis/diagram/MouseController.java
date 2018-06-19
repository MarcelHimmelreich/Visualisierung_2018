package infovis.diagram;

import infovis.debug.Debug;
import infovis.diagram.elements.DrawingEdge;
import infovis.diagram.elements.Edge;
import infovis.diagram.elements.Element;
import infovis.diagram.elements.GroupingRectangle;
import infovis.diagram.elements.None;
import infovis.diagram.elements.Vertex;
import infovis.diagram.layout.Fisheye;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseController implements MouseListener,MouseMotionListener {
	 private Model model;
	 private View view;
	 public Fisheye fish;
	 private Element selectedElement = new None();
	 private List<Vertex>  vertices  = new ArrayList<Vertex>();
	 private double mouseOffsetX;
	 private double mouseOffsetY;
	 public double offsetx;
	 public double offsety;
	 public double focus_point_x;
	 public double focus_point_y;
	 private boolean marker_hit = false;
	 private boolean overview_hit = false;
	 private boolean edgeDrawMode = false;
	 private DrawingEdge drawingEdge = null;
	 private boolean fisheyeMode;
	 private GroupingRectangle groupRectangle;
	/*
	 * Getter And Setter
	 */
	 public Element getSelectedElement(){
		 return selectedElement;
	 }
    public Model getModel() {
		return model;
	}
	public void setModel(Model diagramModel) {
		this.model = diagramModel;
	}
	public View getView() {
		return view;
	}
	public void setView(View diagramView) {
		this.view = diagramView;
	}
	public void setFish(Fisheye fish){ this.fish = fish;}
	public Fisheye getFish(){return fish;}
	/*
     * Implements MouseListener
     */
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		double scale = view.getScale();
		
		
		
		if (e.getButton() == MouseEvent.BUTTON3){
			/*
			 * add grouped elements to the model
			 */
			Vertex groupVertex = (Vertex)getElementContainingPosition(x/scale,y/scale);
			for (Iterator<Vertex> iter = groupVertex.getGroupedElements().iteratorVertices();iter.hasNext();){
				model.addVertex(iter.next());
			}
			for (Iterator<Edge> iter = groupVertex.getGroupedElements().iteratorEdges();iter.hasNext();){
				model.addEdge(iter.next());
			}
			/*
			 * remove elements
			 */
			List<Edge> edgesToRemove = new ArrayList<Edge>();
			for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();){
				Edge edge = iter.next();
				if (edge.getSource() == groupVertex || edge.getTarget() == groupVertex){
					edgesToRemove.add(edge);
				}
			}
			model.removeEdges(edgesToRemove);
			model.removeElement(groupVertex);
			
		}
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();
		mouseOffsetX = x;
		mouseOffsetY = y;
		double scale = view.getScale();

		if(view.markerContains(x,y))
		{
			x = (int)(x*(1/scale));
			y = (int)(y*(1/scale));
			marker_hit = true;
			Debug.println("Drag Marker");
			Debug.println(view.marker.getBounds2D().toString() + ":" +x+" "+y);
		}
		else
		{
			marker_hit = false;
			Debug.println("Release Marker");
		}
		System.out.println("View: "+ view.overviewRect);
		System.out.println("Point: "+ x/scale +"  "+y/scale);
		if(!marker_hit &&view.overviewContains((int)(x/scale),(int)(y/scale)))
		{
			overview_hit = true;
			Debug.println("Drag Overview");
		}
		else
		{
			overview_hit = false;
			Debug.println("Release Overview");
		}

	   if (edgeDrawMode){
			drawingEdge = new DrawingEdge((Vertex)getElementContainingPosition(x/scale,y/scale));
			model.addElement(drawingEdge);
		} else if (fisheyeMode){
			//model.removeVertices(model.getVertices());
			//model.addVertices(vertices);
			model = fish.transform(model,view,(double)x,(double)y);
			view.repaint();
		}
		else {
			
			selectedElement = getElementContainingPosition(x/scale,y/scale);
			/*
			 * calculate offset
			 */
			mouseOffsetX = x - selectedElement.getX() * scale ;
			mouseOffsetY = y - selectedElement.getY() * scale ;
		}
		
	}

	public void mouseReleased(MouseEvent arg0){
		int x = arg0.getX();
		int y = arg0.getY();
		Debug.println("Release Overview");
		Debug.println("Release Marker");
		marker_hit = false;
		overview_hit = false;
		//model.removeVertices(model.getVertices());
		//model.addVertices(vertices);
		if (drawingEdge != null){
			Element to = getElementContainingPosition(x, y);
			model.addEdge(new Edge(drawingEdge.getFrom(),(Vertex)to));
			model.removeElement(drawingEdge);
			drawingEdge = null;
		}
		if (groupRectangle != null){
		    Model groupedElements = new Model();
			for (Iterator<Vertex> iter = model.iteratorVertices(); iter.hasNext();) {
				Vertex vertex = iter.next();
				if (groupRectangle.contains(vertex.getShape().getBounds2D())){
					Debug.p("Vertex found");
					groupedElements.addVertex(vertex);	
				}
			}
			if (!groupedElements.isEmpty()){
				model.removeVertices(groupedElements.getVertices());
				
				Vertex groupVertex = new Vertex(groupRectangle.getCenterX(),groupRectangle.getCenterX());
				groupVertex.setColor(Color.ORANGE);
				groupVertex.setGroupedElements(groupedElements);
				model.addVertex(groupVertex);
				
				List<Edge> newEdges = new ArrayList(); 
				for (Iterator<Edge> iter = model.iteratorEdges(); iter.hasNext();) {
					Edge edge =  iter.next();
				    if (groupRectangle.contains(edge.getSource().getShape().getBounds2D()) 
				    	&& groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
				    		groupVertex.getGroupedElements().addEdge(edge);
                            Debug.p("add Edge to groupedElements");	
                            //iter.remove(); // Warum geht das nicht!
				    } else if (groupRectangle.contains(edge.getSource().getShape().getBounds2D())){
				    	groupVertex.getGroupedElements().addEdge(edge);
				    	newEdges.add(new Edge(groupVertex,edge.getTarget()));
				    } else if (groupRectangle.contains(edge.getTarget().getShape().getBounds2D())){
				    	groupVertex.getGroupedElements().addEdge(edge);
				    	newEdges.add(new Edge(edge.getSource(),groupVertex));
				    }
				}
				model.addEdges(newEdges);
				model.removeEdges(groupedElements.getEdges());
			}
			model.removeElement(groupRectangle);
			groupRectangle = null;
		}
		view.repaint();
	}
	
	public void mouseDragged(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();
		double scale = view.getScale();

		if(marker_hit)
		{
			MarkerOffset(x,y);
		}
		else if(overview_hit)
		{
			OverviewOffset(x,y);
		}
		if (fisheyeMode){
			model = fish.transform(model,view,x,y);
			view.repaint();
		} else if (edgeDrawMode){
			drawingEdge.setX(e.getX());
			drawingEdge.setY(e.getY());
		}else if(selectedElement != null){
			selectedElement.updatePosition((e.getX()-mouseOffsetX)/scale, (e.getY()-mouseOffsetY) /scale);
		}
		view.repaint();
	}
	public void mouseMoved(MouseEvent e) {
	}

	public boolean isDrawingEdges() {
		return edgeDrawMode;
	}

	public void setDrawingEdges(boolean drawingEdges) {
		this.edgeDrawMode = drawingEdges;
	}
	
	public void setFisheyeMode(boolean b) {
		fisheyeMode = b;
		if (b){
			Debug.p("new Fisheye Layout");
			/*
			 * handle fish eye initial call
			 */
			view.repaint();
		} else {
			Debug.p("new Normal Layout");
			view.setModel(model);
			view.repaint();
		}
	}

	public void MarkerOffset(double x, double y)
	{
		double offset_x =  x;
		double offset_y =  y;
		offset_y = offset_y - mouseOffsetY;
		offset_x = offset_x - mouseOffsetX;

		//Calculate local offset
		if(offset_x + view.getTranslateX() < 0)
		{
			//Lower Bound
			offset_x = 0;
			System.out.println("lower bound:  "+offset_y);
		}
		else
		{
			//New Offset between bounds
			offset_x = offset_x + view.getTranslateX();
		}
		if((view.getMarker().getWidth()+offset_x)>= view.overview_width)
		{
			//Upper Bound
			offset_x = view.overview_width - view.getMarker().getWidth();

		}

		if(offset_y + view.getTranslateY() < 0)
		{
			offset_y = 0;
			System.out.println("lower bound:  "+offset_y);
		}
		else
		{
			offset_y = offset_y + view.getTranslateY();
		}
		if((view.getMarker().getHeight()+offset_y)>= view.overview_height)
		{
			offset_y = view.overview_height - view.getMarker().getHeight();

		}

		view.setTranslateX(offset_x);
		view.setTranslateY(offset_y);;
	}

	public void CreateVertices()
	{
		vertices = model.getVertices();
	}

	public void OverviewOffset(double x,double y)
	{
		double offset_x =  x;
		double offset_y =  y;

		//Calculate Boundaries of overview
		if(offset_x < 0)
		{
			offset_x = 0;
		}
		else if(offset_x + view.getOverviewRect().getWidth() > view.getWidth())
		{
			offset_x = view.getWidth() - view.getOverviewRect().getWidth();
		}

		if(offset_y < 0)
		{
			offset_y = 0;
		}
		else if(offset_y + view.getOverviewRect().getHeight() > view.getHeight())
		{
			offset_y = view.getHeight() - view.getOverviewRect().getHeight();
		}
		view.setOverviewOffsetX(offset_x);
		view.setOverviewOffsetY(offset_y);

	}
	
	/*
	 * private Methods
	 */
	private Element getElementContainingPosition(double x,double y){
		Element currentElement = new None();
		Iterator<Element> iter = getModel().iterator();
		while (iter.hasNext()) {
		  Element element =  iter.next();
		  if (element.contains(x, y)) currentElement = element;  
		}
		return currentElement;
	}
	
    
}
