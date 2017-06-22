package it.polito.tdp.flight.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flight.db.FlightDAO;

public class Model {

	private Set<Airport> airports;
	private Map<Integer, Airport> mapA;
	SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> graph;
	private static int VELOCITA = 800;
	private List<AirportsConnected> aconnected;
	private Simulator sim;
	
	public Set<Airport> getAirports(){
		if(airports==null){
			FlightDAO dao = new FlightDAO();
			mapA= new HashMap<>();
			
			airports = dao.getAllAirports();
			for(Airport a: airports)
				mapA.put(a.getAirportId(),a);
		}
		return airports;
	}
	
	public void createGraph(double km){
		graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(graph, this.getAirports());

		FlightDAO dao = new FlightDAO();
		
		if(aconnected==null)
			aconnected = dao.getAirportsConnected();
		
		for(AirportsConnected ac: aconnected){
			Airport a1= mapA.get(ac.getIdP());
			Airport a2 = mapA.get(ac.getIdA());
			
			if(!a1.equals(a2)){
				double distanza = LatLngTool.distance( a1.getCoords(), a2.getCoords(), LengthUnit. KILOMETER);
				
				if(distanza< km){
					double peso = distanza/VELOCITA;
					Graphs.addEdgeWithVertices(graph, a1, a2, peso);
					}
				}
			}
		System.out.println(graph.toString());
	}
	
	public boolean grafoCompletamenteConnesso(){
		ConnectivityInspector<Airport, DefaultWeightedEdge> ci = new ConnectivityInspector<>(graph);
		
		return ci.isGraphConnected();
	}
	
	public Airport getMostDistantFromFiumicino(double km){
		Airport dist=null;
		double max= 0;
		
		for(AirportsConnected ac: aconnected){
			Airport ap = mapA.get(ac.getIdP());
			
			if(ap.getName().compareTo("Fiumicino")==0){
				
				Airport a2 = mapA.get(ac.getIdA());				
				double distanza = LatLngTool.distance( ap.getCoords(), a2.getCoords(), LengthUnit. KILOMETER);
				
				if(distanza > max && distanza < km){
					max = distanza;
					dist = a2;
					System.out.println(dist.toString()+" "+max+"\n");
				}
			}
		}
		
		return dist;		
	}
	

	 public List<AirportAndNum> simula(int k){
		this.sim= new Simulator(this, k);

		return  sim.getPasseggeri();
	 }
	
}
