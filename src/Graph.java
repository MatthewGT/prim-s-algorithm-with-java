
import java.util.LinkedList;

public class Graph {
	// Keep track of the graph size
	private int numVertices;
	private int numEdges;
	private LinkedList<node>[] adjacencylist;

	public Graph(int numVertices, int numEdges) {
		this.numVertices = numVertices;
		this.numEdges = numEdges;
		//initialize adjacency lists for all the vertices
		this.adjacencylist = new LinkedList[numVertices];
		for (int i = 0; i < numVertices; i++) {
			adjacencylist[i] = new LinkedList<>();
		}

	}



	public void printGraph() {
		for (int i = 0; i < this.numVertices; i++) {
			LinkedList<node> list = this.adjacencylist[i];
			for (int j = 0; j < list.size(); j++) {
				System.out.println("vertex-" + i + " is connected to " +
						list.get(j).destination + " with weight " + list.get(j).weight);
			}
		}
	}

	public int getNumVertices(){
		return this.numVertices;
	}
	public int numEdges(){
		return this.numEdges;
	}
	public int getWeight(int source,int destination){
		if (source==destination)
			return 0;
		LinkedList<node> list = this.adjacencylist[source];
		for (int j = 0; j < list.size(); j++) {
			if(list.get(j).destination == destination)
				return list.get(j).weight;
		}
		return Integer.MAX_VALUE;
	}
	public void setEgde(int source, int destination, int weight) {

		node edge = new node(source, destination, weight);
		this.adjacencylist[source].addLast(edge);
	}
	public void deleteEgde(int source, int destination) {
		for (int i = 0; i < this.adjacencylist[source].size(); i++) {
			if (this.adjacencylist[source].get(i).destination == destination){
				this.adjacencylist[source].remove(i);
				break;
			}

		}
	}
	public LinkedList<node> getList(int index){
		return this.adjacencylist[index];
	}
	public int getAdjList(){
		return this.adjacencylist.length;
	}
}

