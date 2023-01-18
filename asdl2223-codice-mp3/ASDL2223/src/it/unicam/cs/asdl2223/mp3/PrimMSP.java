package it.unicam.cs.asdl2223.mp3;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

import java.util.ArrayList;
import java.util.List;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * <p>
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * <p>
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 *
 * @param <L> tipo delle etichette dei nodi del grafo
 * @author Luca Tesei (template)
 * Christian Bonsignore christian.bonsignore@studenti.unicam.it (implementazione)
 */

//userò nei commenti lo stesso pseudo codice datoci dalla Prof.ssa Merelli, spero non influisca con l'antiplagio
public class PrimMSP<L> {

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */
    List<GraphNode<L>> queue;

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        queue = new ArrayList<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @param s il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *          dell'albero di copertura minimo. Tale nodo sarà la radice
     *          dell'albero di copertura trovato
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        if (g == null || s == null) {
            throw new NullPointerException("Grafo o/e nodo sorgente nullo.");
        }
        if (!g.getNodes().contains(s)) {
            throw new IllegalArgumentException("Nodo sorgente non esiste nel grafo.");
        }
        if (g.isDirected()) {
            throw new IllegalArgumentException("Grafo orientati non ammessi.");
        }
        for (GraphEdge<L> tmp : g.getEdges()){
            if (!tmp.hasWeight() || tmp.isDirected() || tmp.getWeight() < 0){
                throw new IllegalArgumentException("Prensente un edge non pesato o con pesi negativi");}}


        //foreach nodo v ∈ V[G]
        for (GraphNode<L> node : g.getNodes()) {
            //do key[v] = ∞
            node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            //π[v] = NIL
            node.setPrevious(null);
            //setto v non visitato
            node.setColor(GraphNode.COLOR_WHITE);
        }

        //key[r] = 0
        g.getNode(s).setFloatingPointDistance(0D);

        //Q = V[G]
        queue.addAll(g.getNodes());

        //while Q ≠ ∅
        while (this.queue.size() > 0) {

            //do u = EXTRACT-MIN(Q)
            GraphNode<L> minNode = getMin();

            //setto u a visitato
            minNode.setColor(GraphNode.COLOR_BLACK);

            //for each v ∈ Adj[u]
            for (GraphNode<L> compareNode : g.getAdjacentNodesOf(minNode)) {

                //edge (u,v) ∈ v
                GraphEdge<L> nodesEdge = g.getEdge(minNode, compareNode);

                //if v ∈ Q and w(u,v) < key[v]
                if (compareNode.getColor() == GraphNode.COLOR_WHITE && nodesEdge.getWeight() < compareNode.getFloatingPointDistance()) {
                    //then π[v] = u
                    compareNode.setPrevious(minNode);
                    //key[v] = w(u,v)
                    compareNode.setFloatingPointDistance(nodesEdge.getWeight());
                }
            }
        }
    }

    //visto che non sto usando una vera e propria coda, ma una lista come se fosse una coda, posso comunque
    //scorrerla tutta e cercare l'elemento minimo
    private GraphNode<L> getMin() {
        GraphNode<L> out = queue.get(0);
        for (GraphNode<L> node: queue) {
            if(Double.compare(node.getFloatingPointDistance(), out.getFloatingPointDistance()) < 0) {
                out = node;
            }
        }
        queue.remove(out);
        return out;
    }
}
