package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private Graph<ArtObject,DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
			//id dell'artobj
	private Map<Integer,ArtObject> idMap;//Per non fare piu' la new del dao (guardo model di MetroParis)
						//in altro esercizio sara un altro javabean
	public Model() {
		this.dao=new ArtsmiaDAO();//me ne serve solo uno quindi lo faccio qui, nel costruttore
		idMap=new HashMap<Integer,ArtObject>();
	}
	
	public void creaGrafo() {
		
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	
		//RIEMPIMENTO GRAFO
		//aggiunta vertici-->voglio tutti gli oggetti, dato dal testo
		//1. Recupero tutti gli ArtObject dal db
		//2. Li inserisco come vertici
		dao.listObjects(idMap);//abbiamo sempre una new richiamando questo metodo...basterebbe solo una volta-->identity map!
		Graphs.addAllVertices(grafo, idMap.values());
		
		//3. Aggiungo gli archi
		
		/*APPROCCIO 1-->non fa arrivare alla visualizzazione del grafo!NON VA BENE!!
		//Doppio ciclo for sui vertici
		//Dati due vertici controllo se devono essere collegati
		for(ArtObject a1:this.grafo.vertexSet()) {//ritorna tutti i vertici del grafo
			for(ArtObject a2:this.grafo.vertexSet()) {
				if(!a1.equals(a2) && !this.grafo.containsEdge(a1,a2)) {//Siamo in un grafo non orientato, basta guardare se non ci sia gia l'arco
					//devo collegare a1 ad a2?
					int peso=dao.getPeso(a1,a2);
					if(peso>0) {
						Graphs.addEdge(this.grafo, a1, a2, peso);
					}
				}
			}
		}
		*/
		
		//APPROCCIO 3, il 2 non andava bene anche (perche' e' intermedio)
		for(Adiacenza a:dao.getAdiacenze()) {
				//ho escluso coppie duplicate quindi non c'era ancora sicuramente l'arco, e il peso sara sicuramente >0
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		System.out.println("Grafo creato!");
		System.out.println("#Vertici: "+grafo.vertexSet().size());
		System.out.println("#Archi: "+grafo.edgeSet().size());
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}

	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
}
