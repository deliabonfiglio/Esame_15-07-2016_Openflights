package it.polito.tdp.flight;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.flight.model.AirportAndNum;
import it.polito.tdp.flight.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtDistanzaInput;

	@FXML
	private TextField txtPasseggeriInput;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCreaGrafo(ActionEvent event) {
		String kilom = txtDistanzaInput.getText();
		
		try {
			double km = Double.parseDouble(kilom);
			if(km>0){
				txtResult.clear();
				model.createGraph(km);
				
				if(model.grafoCompletamenteConnesso())
					txtResult.appendText("Grafo Completamente connesso.\n");
				else
					txtResult.appendText("Grafo NON completamente connesso.\n");
				
				txtResult.appendText("Aereoporto più distante, raggiungibile da Fiumicino: "+model.getMostDistantFromFiumicino(km).toString()+"\n");				
			}
			
		} catch (NumberFormatException e){
			txtResult.appendText("Inserire distanza con sole cifre.\n");
			return;
		}
		
	}

	@FXML
	void doSimula(ActionEvent event) {
		String kvalue = txtPasseggeriInput.getText();
		String kmetri= txtDistanzaInput.getText();
		try{
			int k = Integer.parseInt(kvalue);
			double km = Double.parseDouble(kmetri); 
			if(k>0 && km>0){
				model.createGraph(km);
				List<AirportAndNum> list = model.simula(k);
				for(AirportAndNum an : list){
					txtResult.appendText(an.toString()+"\n");
				}
			}
			
		} catch (NumberFormatException e){
			txtResult.appendText("Inserire numero numerico intero.\n");
		}
	}

	@FXML
	void initialize() {
		assert txtDistanzaInput != null : "fx:id=\"txtDistanzaInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtPasseggeriInput != null : "fx:id=\"txtPasseggeriInput\" was not injected: check your FXML file 'Untitled'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Untitled'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
