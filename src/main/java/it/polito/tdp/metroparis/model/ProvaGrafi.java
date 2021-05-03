package it.polito.tdp.metroparis.model;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProvaGrafi {

	public static void main(String[] args) {
		Graph<String, DefaultEdge> grafo= new SimpleGraph<>(DefaultEdge.class);

		grafo.addVertex("UNO");
		grafo.addVertex("DUE");
		grafo.addVertex("TRE");
		
		grafo.addEdge("UNO", "TRE");
		grafo.addEdge("DUE", "TRE");
		
		System.out.print(grafo);//stampa con le graffe perchè non mi interessa l'orientamento
		
	}

}
