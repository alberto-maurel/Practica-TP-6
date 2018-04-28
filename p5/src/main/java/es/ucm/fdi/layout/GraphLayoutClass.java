package es.ucm.fdi.layout;

import javax.swing.*;
import es.ucm.fdi.extra.graphlayout.*;
import es.ucm.fdi.model.Junction;
import es.ucm.fdi.model.Road;
import es.ucm.fdi.model.TrafficSimulator.Listener;
import es.ucm.fdi.model.TrafficSimulator.UpdateEvent;
import es.ucm.fdi.model.Vehicle;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class GraphLayoutClass extends JPanel implements Listener {
	
	private GraphComponent _graphComp;
    
    private Map<String, Edge> aristas;
    private Map<String, Node> vertices; //Clave - identificador del nodo (el mismo que el del vértice), Valor - nodo al que corresponde
   
	public GraphLayoutClass() {
		_graphComp = new GraphComponent();
		add(_graphComp);
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	protected void generateGraph(UpdateEvent ue) {
		Graph g = new Graph();
		//Convertimos las junctions a nodos
		List<Junction> auxJunct = ue.getJunctions();
		vertices = new HashMap<>(); //Limpiamos los vertices anteriores
		for(int i = 0; i < auxJunct.size(); ++i) {
			String idNodoAct = auxJunct.get(i).getId();
			Node nodoAux = new Node(idNodoAct);
			vertices.put(idNodoAct, nodoAux);
			g.addNode(nodoAux);
		}
				
		//Idem para convertir las carreteras a vertices
		List<Road> auxRoad = ue.getRoads();
		aristas = new HashMap<>(); //Limpiamos los vertices anteriores
		for(int i = 0; i < auxRoad.size(); ++i) {
			String idAristaAct = auxRoad.get(i).getId();
			String idSrcNode = auxRoad.get(i).idSourceJunction();
			String idTargetNode = auxRoad.get(i).idTargetJunction();			
			
			Edge aristaAct = new Edge(idAristaAct, vertices.get(idSrcNode), 
					vertices.get(idTargetNode),auxRoad.get(i).getLength(), auxRoad.get(i).estaEnVerde());
			aristas.put(idAristaAct, aristaAct);
			g.addEdge(aristaAct);
		}
		
		//Y cogemos los vehículos para crear los puntos y los metemos en las aristas pertinentes
		List<Vehicle> auxVeh = ue.getVehicles();
		for(int i = 0; i < auxVeh.size(); ++i) {
			if(auxVeh.get(i).getPosicionActual() != -1) {
				String idVehicle = auxVeh.get(i).getId();
				int vehicleLocation = auxVeh.get(i).getPosicionActual();
			
				Dot puntoAct = new Dot(idVehicle, vehicleLocation);
				//Y ahora introducimos los puntos en el Edge adecuado
				String idArista = auxVeh.get(i).getIdCarreteraAct();
				aristas.get(idArista).addDot(puntoAct);
			}
		}
		_graphComp.setGraph(g);
	}

	public void registered(UpdateEvent ue) {
		generateGraph(ue);
	}
	public void reset(UpdateEvent ue) {
		generateGraph(ue);
	}
	public void newEvent(UpdateEvent ue) {
		generateGraph(ue);
	}
	public void advanced(UpdateEvent ue) {
		generateGraph(ue);
	}
	public void error(UpdateEvent ue, String error) {}
}