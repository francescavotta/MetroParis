package it.polito.tdp.metroparis.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		m.creaGrafo();

		Fermata p = m.trovaFermata("La Fourche");
		if(p==null)
			System.out.println("Fermata non trovata");
		else {
			List<Fermata> raggiungibiliAmpiezza = m.fermateRaggiungibili(p);
			System.out.println(raggiungibiliAmpiezza.size());
			System.out.println(raggiungibiliAmpiezza);
			
			//List<Fermata> raggiungibiliProfondita = m.fermateRaggiungibili2(p);
			//System.out.println(raggiungibiliProfondita);
		// è un grafo connesso, tutti i vertici sono raggiungibili.
		}
		
		Fermata arrivo = m.trovaFermata("Temple");
		List <Fermata> percorso = m.trovaCammino(p, arrivo);
		System.out.println(percorso);
	}

}
