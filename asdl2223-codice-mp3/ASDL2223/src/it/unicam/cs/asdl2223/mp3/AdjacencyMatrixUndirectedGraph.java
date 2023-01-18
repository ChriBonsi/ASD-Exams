/**
 *
 */
package it.unicam.cs.asdl2223.mp3;

import java.util.*;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * <p>
 * I nodi sono indicizzati da 0 a nodeCount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * <p>
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase d'inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * <p>
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * <p>
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase d'inserimento.
 *
 * @author Luca Tesei (template)
 * Christian Bonsignore christian.bonsignore@studenti.unicam.it (implementazione)
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null od oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        //basta contare gli elementi nell'indice dei nodi
        return this.nodesIndex.keySet().size();
    }

    @Override
    public int edgeCount() {
        //conto gli elementi non nulli nella matrice
        int cont = 0;
        for (ArrayList<GraphEdge<L>> arr : matrix) {
            for (GraphEdge<L> elem : arr) {
                if (elem != null) cont++;
            }
        }
        //aggiungo il modulo per includere eventuali archi fra lo stesso nodo rimasti dispari
        return cont / 2 + cont % 2;
    }

    @Override
    public void clear() {
        //reinizializzo
        this.matrix = new ArrayList<>();
        this.nodesIndex = new HashMap<>();
    }

    @Override
    public boolean isDirected() {
        //Dobbiamo usare grafi non direzionati
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        //controllo se il nodo è duplicato
        if (this.nodesIndex.containsKey(node)) return false;

        //aggiungo il nodo nell'indice e poi aggiungo una "colonna" nella matrice
        this.nodesIndex.put(node, nodeCount());
        this.matrix.add(new ArrayList<>());

        //aggiungo una casella null in ogni colonna già esistente e un numero nodeCount in quella appena aggiunta
        for (ArrayList<GraphEdge<L>> temp : this.matrix) {
            for (int i = temp.size(); i < this.nodeCount(); i++) {
                temp.add(null);
            }
        }
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        if (label == null) {
            throw new NullPointerException("Label nulla.");
        }
        //mi basta chiamare l'altro metodo creando un'istanza locale di GraphNode
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null) {throw new NullPointerException("Nodo nullo.");}
        int a = getNodeIndexOf(node);

        //rimuovo la colonna e la riga dalla matrice, gli indici qui si sistemano da soli, quindi dovrò solo
        //aggiornare quelli dell'indice in modo da farli ricorrispondere
        this.matrix.remove(a);
        for (int i = 0; i < this.matrix.get(a).size(); i++) {this.matrix.get(i).remove(a);}

        //rimuovo il nodo dall'indice
        this.nodesIndex.remove(node);

        //creo nuova map per l'indice, inserisco tutti i valori dalla prima aggiornando gli
        //indici e la rimetto al posto della prima
        Map<GraphNode<L>, Integer> temp = new HashMap<>();
        for (Map.Entry<GraphNode<L>, Integer> elem : this.nodesIndex.entrySet()) {
            if (elem.getValue() < a) {temp.put(elem.getKey(), elem.getValue());}
            else if (elem.getValue() > a) {temp.put(elem.getKey(), elem.getValue() - 1);}
        }
        this.nodesIndex = temp;
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null) {
            throw new NullPointerException("Label nulla.");
        }
        //non ho bisogno di ulteriori check perché sono già tutti nel costruttore o nel metodo che chiamo
        removeNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i >= this.nodeCount()) {
            throw new IndexOutOfBoundsException("Non esiste un nodo con questo indice.");
        }
        //non ho bisogno di altri check perché sono già tutti nel costruttore o nel metodo che chiamo

        //scelgo arbitrariamente di rimandare all'altro metodo anche se qui ho già l'indice perché
        //in quello con il nodo dovrò chiamare getNodeIndexOf alla riga 149, ma in questo dovrei chiamare getNode
        removeNode(getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Nodo nullo.");
        }

        return getNode(node.getLabel());
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null) {
            throw new NullPointerException("Label nulla.");
        }

        //mi asta filtrare per i nodi dell'indice cercandone uno con la label uguale
        for (GraphNode<L> temp : nodesIndex.keySet()) {
            if (temp.getLabel().equals(label)) {
                return temp;
            }
        }

        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i >= this.nodeCount()) {
            throw new IndexOutOfBoundsException("Non esiste un nodo con questo indice.");
        }

        //qui invece devo scorrere fra tutte le entries dell'indice per associare indice e nodo
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getValue() == i) return entry.getKey();
        }

        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        if (!this.nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Questo nodo non esiste.");
        }
        //procedimento uguale ma inverso di getNode(int i)
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet()) {
            if (entry.getKey().equals(node)) {
                return entry.getValue();
            }
        }
        return -1;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) {
            throw new NullPointerException("Label nulla.");
        }
        if (getNode(label) == null) {
            throw new IllegalArgumentException("Non esiste un nodo con questa label");
        }

        return getNodeIndexOf(getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Arco nullo.");
        }
        if (edge.isDirected()) {
            throw new IllegalArgumentException("Arco direzionato in grafo non orientato");
        }

        if (!this.matrix.isEmpty() && this.matrix.get(getNodeIndexOf(edge.getNode1())).contains(edge)) return false;

        //comodità
        int i = getNodeIndexOf(edge.getNode1());
        int j = getNodeIndexOf(edge.getNode2());

        //inserisco nella matrice solo una volta se è un arco rivolto verso sé stesso, altrimenti la
        //seconda volta in maniera simmetrica
        this.matrix.get(i).set(j, edge);
        if (i != j) {
            this.matrix.get(j).set(i, edge);
        }
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        if (!this.nodesIndex.containsKey(node1)) {
            throw new IllegalArgumentException("Il nodo 1 non è presente.");
        }
        if (!this.nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Il nodo 2 non è presente.");
        }
        return addEdge(new GraphEdge<>(node1, node2, false));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return addEdge(new GraphEdge<>(node1, node2, false, weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return addEdge(new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), false));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return addWeightedEdge(new GraphNode<>(label1), new GraphNode<>(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return addEdge(new GraphEdge<>(getNode(i), getNode(j), false));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return addWeightedEdge(getNode(i), getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        //non ho bisogno di ulteriori check perché sono già tutti nel costruttore o nel metodo che chiamo
        removeEdge(getNodeIndexOf(edge.getNode1()), getNodeIndexOf(edge.getNode2()));
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 != null && node2 != null) {
            if (!this.nodesIndex.containsKey(node1)) {
                throw new IllegalArgumentException("Il nodo 1 non è presente.");
            }
            if (!this.nodesIndex.containsKey(node2)) {
                throw new IllegalArgumentException("Il nodo 2 non è presente.");
            }

            //non ho bisogno di ulteriori check perché sono già tutti nel costruttore o nel metodo che chiamo

            removeEdge(getNodeIndexOf(node1), getNodeIndexOf(node2));
        } else {
            throw new NullPointerException("Nodo nullo.");
        }
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Label nulla.");
        }
        //non ho bisogno di ulteriori check perché sono già tutti nel costruttore o nel metodo che chiamo

        removeEdge(getNodeIndexOf(new GraphNode<>(label1)), getNodeIndexOf(new GraphNode<>(label2)));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (this.matrix.get(i).get(j) == null) {
            throw new IllegalArgumentException("L'arco non esiste.");
        }
        if (!this.nodesIndex.containsKey(getNode(i))) {
            throw new IllegalArgumentException("Il nodo 1 non è presente.");
        }
        if (!this.nodesIndex.containsKey(getNode(j))) {
            throw new IllegalArgumentException("Il nodo 2 non è presente.");
        }
        //rimuovo entrambi gli archi
        this.matrix.get(i).set(j, null);
        this.matrix.get(j).set(i, null);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("Arco nullo");
        }
        if (!this.nodesIndex.containsKey(edge.getNode1()) || !this.nodesIndex.containsKey(edge.getNode2())) {
            throw new IllegalArgumentException("Almeno uno dei due nodi non esiste.");
        }

        //scorro tutti gli archi con i due foreach
        for (ArrayList<GraphEdge<L>> arr : matrix) {
            for (GraphEdge<L> elem : arr) {
                if (elem != null && elem.equals(edge)) return elem;
            }
        }
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //Visto che il grafo non è orientato, non è importante l'ordine dei vertici
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getEdge(new GraphEdge<>(node1, node2, false));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        //Visto che il grafo non è orientato, non è importante l'ordine dei vertici
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getEdge(new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), false));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        //Visto che il grafo non è orientato, non è importante l'ordine dei vertici
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getEdge(getNode(i), getNode(j));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();

        //scorro gli archi non nulli e prendo tutti quelli collegati
        for (GraphEdge<L> temp : this.getEdgesOf(node)) {
            if (temp != null) {
                //questo if-else serve per prendere l'altro nodo in caso io abbia trovato prima la versione simmetrica
                if (!node.equals(temp.getNode2())) {
                    adjacentNodes.add(temp.getNode2());
                } else {
                    adjacentNodes.add(temp.getNode1());
                }
            }
        }
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getAdjacentNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getAdjacentNodesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getEdgesOf(getNodeIndexOf(node));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        //non ho bisogno di check perché sono già tutti nel costruttore o nel metodo che chiamo
        return getEdgesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        Set<GraphEdge<L>> temp = new HashSet<>();
        //trovo indice in matrix
        //scorro tutti gli elementi collegati a i e li aggiungo al set se non sono nulli

        for (GraphEdge<L> elem : matrix.get(i)) {
            if (elem != null) {
                temp.add(elem);
            }
        }
        return temp;
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> graphEdges = new HashSet<>();

        //scorro tutti gli elementi della matrice e li aggiungo se non sono nulli
        for (ArrayList<GraphEdge<L>> elem : matrix) {
            for (GraphEdge<L> el : elem) {
                if (!(el == null || this.matrix.contains(el))) {
                    graphEdges.add(el);
                }
            }
        }
        return graphEdges;
    }

    /*
    Metodi non supportati
     */

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }
}
