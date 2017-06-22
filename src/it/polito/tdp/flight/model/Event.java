package it.polito.tdp.flight.model;

public class Event implements Comparable<Event>{
	
	
	private Airport aereoporto;
	private double tempo;
	
	
	
	
	
	public Event(Airport aereoporto, double tempo) {
		super();
		this.aereoporto = aereoporto;
		this.tempo = tempo;
	}





	public Airport getAereoporto() {
		return aereoporto;
	}





	public void setAereoporto(Airport aereoporto) {
		this.aereoporto = aereoporto;
	}





	public double getTempo() {
		return tempo;
	}





	public void setTempo(double tempo) {
		this.tempo = tempo;
	}





	@Override
	public int compareTo(Event other) {
	
		return Double.compare(this.tempo, other.getTempo());
	}
	
	
	
	

}