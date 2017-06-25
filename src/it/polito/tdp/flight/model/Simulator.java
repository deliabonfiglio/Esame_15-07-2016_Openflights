package it.polito.tdp.flight.model;

import java.util.*;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class Simulator {

	private Model model;
	private int K;
	private int ore[]=new int[18];
	private SimpleDirectedWeightedGraph<Airport,DefaultWeightedEdge> graph;
	private Map<Airport,Integer> presenti;
	private PriorityQueue<Event> queue;
	
	
	public Simulator(Model model, int k) {
		this.model=model;
		this.graph=model.graph;
		this.K=k;
		
		queue=new PriorityQueue<>();
		presenti=new HashMap<Airport,Integer>();
		
		for(Airport atemp: graph.vertexSet()){
			presenti.put(atemp, 0);
		}
		
		int i=7;
		for(int pos=0; pos<18; pos++){
			ore[pos]=i;
			i=i+2;
		}
	}


	public List<AirportAndNum> getPasseggeri() {
		this.completeAirport();
		this.run();
		
		List<AirportAndNum> lista = new ArrayList<>();
		
		for(Airport a: this.presenti.keySet()){
			if(presenti.get(a)!=0){
				//ci sono persone in quell'aereoporto
				lista.add(new AirportAndNum(a, presenti.get(a)));
			}
		}
		
		Collections.sort(lista);
		return lista;
	}

	private void completeAirport() {
		while(K!=0){
			//finche non faccio tutti i passeggeri
			
			for(Airport a: graph.vertexSet()){
				if(K!=0){
					double prob= Math.random();
					if(prob>0.5){
						presenti.put(a, presenti.get(a)+1);
						K--;
					}
				}
			}
		}
		for(Airport atemp: presenti.keySet()){
			if(presenti.get(atemp)!=0){
				//inizializzo gli eventi alle 6
				for(int i =0; i<presenti.get(atemp); i++)
					queue.add(new Event(atemp, 6));
			}
		}
		
	}

	private void run() {
		while(!queue.isEmpty()){
			Event e = queue.poll();
			
			double tempo = e.getTempo();
			double tpartenza =0;
			for(int i=1; i<=ore.length; i++){
				if(i>=tempo){
					tpartenza=i;
					break;
				}
			}
			
			Airport arrivo= e.getAereoporto();
			Set<DefaultWeightedEdge> raggiungibili= graph.outgoingEdgesOf(arrivo);
			
			if(raggiungibili.size()!=0){
				int destID= (int)(Math.random()*raggiungibili.size());
				
				//metto gli archi in una lista altrimenti non posso posso fare accesso posizionale da un set
				List<DefaultWeightedEdge> ltemp=new ArrayList<>();
			
				for(DefaultWeightedEdge arco:raggiungibili){
					ltemp.add(arco);
				}
				
				Airport destinazione=graph.getEdgeTarget(ltemp.get(destID));
				
				//ci puo essere un evento di partenza
					if(tpartenza!=0){
						int tArrivo=(int)(tpartenza+graph.getEdgeWeight(ltemp.get(destID)));
					    
						queue.add(new Event(destinazione,tArrivo));
						
						//tolgo il passeggero dall'aereoporto dove stava e lo metto nell'altro.
						presenti.put(arrivo, presenti.get(arrivo)-1);
						presenti.put(destinazione, presenti.get(destinazione)+1);
					}
			}
				
		}	
	}

}
