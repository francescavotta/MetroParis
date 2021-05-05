package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	Graph <Fermata, DefaultEdge> grafo;

	Map<Fermata, Fermata> predecessore;
	
	//interfaccia Graph
	public void creaGrafo() {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);

		MetroDAO dao = new MetroDAO();
		List <Fermata> fermate = dao.getAllFermate();

		/*		for(Fermata f: fermate) {
			this.grafo.addVertex(f);
		}*/

		//Graphs implementa metodi statici e sono più veloci
		//semplifica un po' la vita e come primo parametro hanno il grafo

		Graphs.addAllVertices(this.grafo, fermate);

		// Aggiungiamo gli archi
		//questo doppio ciclo for richiede molto tempo
		/*for(Fermata f1: this.grafo.vertexSet()) {
			for(Fermata f2: this.grafo.vertexSet()) {
				if(!f1.equals(f2) && dao.fermateCollegate(f1,f2)) {
					this.grafo.addEdge(f1, f2);
				}
			}
		}*/

		List<Connessione> connessioni = dao.getAllConnessioni(fermate);
		for(Connessione c: connessioni) {
			this.grafo.addEdge(c.getStazA(), c.getStazP());
		}

	}
	
	//Fermata f;
	/*Set<DefaultEdge> archi = this.grafo.edgesOf(f);
	
	for(DefaultEdge e: archi) {
		Fermata f1 = this.grafo.getEdgeSource(e);
		Fermata f2 = this.grafo.getEdgeTarget(e);
	
		f1 = Graphs.getOppositeVertex(grafo, e, f);
	}*/
	
	//List<Fermata> fermataAdiacente = Graphs.successorListOf(grafo, f);


	public List<Fermata> fermateRaggiungibili(Fermata partenza) {
	//A partire da una fermata cerco i vertici raggiungibili
	//1. AMPIEZZA, restituisce dal livello 0(partenza) in poi..fino al livello massimo
		BreadthFirstIterator<Fermata, DefaultEdge> bfv = new BreadthFirstIterator(this.grafo, partenza);
		
		this.predecessore = new HashMap<>();
		this.predecessore.put(partenza, null);
		
		bfv.addTraversalListener(new TraversalListener<Fermata, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {	
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
			//ogni volta che attraverso un arco viene chiamato questo metodo, il parametro 'e' contiene informazioni dell'evento stesso
			DefaultEdge arco = e.getEdge();
			//sorgente e target dipendono da come ho costruito il grafo
			//se il grafo fosse orientato questo problema non ci sarebbe!!
			Fermata a = grafo.getEdgeSource(arco);
			Fermata b = grafo.getEdgeTarget(arco);
			
			//ho scoperto 'a' arrivando da 'b', se 'b' lo conosco già
			if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
				predecessore.put(a, b);
				//System.out.println(a + "scoperto da " + b);
			}else if (predecessore.containsKey(a) && !predecessore.containsKey(b)){
				//di sicuro conoscevo 'a'
				predecessore.put(b, a);
				//System.out.println(b + "scoperto da " + a);
			}
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
				//Ogni volta che attraverso un vertice è richiamato questo metodo
				//System.out.println(e.getVertex());
				//Fermata nuova = e.getVertex();
				//Il precedente è già una key della mappa
				//Fermata precedente;
				//posso usare predecessore -> procedura di closer, chiusura
				//predecessore.put(nuova, precedente);
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {	
			}
		
		});
		List<Fermata> result = new ArrayList<>();
		
		while(bfv.hasNext()) {
			Fermata f = bfv.next();
			result.add(f);
		}
		
		return result;
	}
	
	public List<Fermata> fermateRaggiungibili2(Fermata partenza) {
		//A partire da una fermata cerco i vertici raggiungibili
		//2. Profondità
			DepthFirstIterator<Fermata, DefaultEdge> bfv = new DepthFirstIterator(this.grafo, partenza);
			
			List<Fermata> result = new ArrayList<>();
			
			while(bfv.hasNext()) {
				Fermata f = bfv.next();
				result.add(f);
			}
			
			return result;
		}
	
	public Fermata trovaFermata(String nome) {
		for(Fermata f: this.grafo.vertexSet()) {
			if(f.getNome().equals(nome)) {
				return f;
			}
		}
		return null;
	}
	
	public List<Fermata> trovaCammino(Fermata partenza, Fermata arrivo){
		this.fermateRaggiungibili(partenza);
		
		List<Fermata> result = new LinkedList<>();
		result.add(arrivo);
		Fermata f = arrivo;
		while(predecessore.get(f)!=null) {
			f = predecessore.get(f); //getParent(f);
			result.add(0,f);	
		}
		//lista di ordine inverso.. se modifico l'add sono a posto
		//preferire una LinkedList se aggiungo sempre in testa
		return result;
	}
}


