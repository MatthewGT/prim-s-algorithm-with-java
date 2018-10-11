/*
CSE6140 HW2
This is an example of how your experiments should look like.
Feel free to use and modify the code below, or write your own experimental code, as long as it produces the desired output.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;



public class RunExperiments {
	public static Graph parseEdges(String graphFile) throws IOException{
		// Open the file and read the number of vertices/edges
		BufferedReader br = new BufferedReader(new FileReader(graphFile));
		String firstLine = br.readLine();
		int numVertices = Integer.parseInt(firstLine.split(" ")[0]);
		int numEdges = Integer.parseInt(firstLine.split(" ")[1]);
		// Then, create the new graph with the appropriate size
		Graph g = new Graph(numVertices, numEdges);
		String line = br.readLine();
        int temp_u = Integer.parseInt(line.split(" ")[0]);
        int temp_v = Integer.parseInt(line.split(" ")[1]);
        int temp_weight = Integer.parseInt(line.split(" ")[2]);
		while ((line = br.readLine()) != null ) {
			int u = Integer.parseInt(line.split(" ")[0]);
			int v = Integer.parseInt(line.split(" ")[1]);
			int weight = Integer.parseInt(line.split(" ")[2]);
            if((u == temp_u) && (v==temp_v)){
                if (weight < temp_weight)
                    temp_weight = weight;
            }
            else{
                g.setEgde(temp_u,temp_v,temp_weight);
                g.setEgde(temp_v,temp_u,temp_weight);
                temp_u = u;
                temp_v = v;
                temp_weight = weight;
            }
		}
		g.setEgde(temp_u,temp_v,temp_weight);
        g.setEgde(temp_v,temp_u,temp_weight);
//		g.printGraph();
		return g;
	}
	/////comparator

	////computeMST function
	public static int computeMST(Graph G,Graph G_copy){
		int numVer = G.getNumVertices();
		int MST_sum = 0;
        Boolean[] visitied = new Boolean[numVer];
        vertex[] verticesList = new vertex[numVer];
        int[] parent = new int[numVer];
        ////use priority queue to keep track unvisited vertices
        PriorityQueue<vertex> queue = new PriorityQueue<>(numVer, new comparator());
        ///Initialize
        for (int o = 0; o < numVer; o++) {
            verticesList[o] = new vertex(Integer.MAX_VALUE,o);
            visitied[o] = false;
            parent[o] = -1;
            queue.add(verticesList[o]);
        }
        // Start from 0 vertex
        visitied[0] = true;
        verticesList[0].key = 0;
        // do until the queue is empy
        while (!queue.isEmpty()) {
            vertex node0 = queue.poll();
            visitied[node0.index] = true;
            for (node ite : G.getList(node0.index)) {
                if (visitied[ite.destination] == false) {
                    if (verticesList[ite.destination].key > ite.weight) {
                        queue.remove(verticesList[ite.destination]);
                        verticesList[ite.destination].key = ite.weight;
                        queue.add(verticesList[ite.destination]);
                        parent[ite.destination] = node0.index;
                    }
                }
            }
        }
        /// copy MST to G_copy
        for (int i = 1; i < numVer; i++) {
            G_copy.setEgde(parent[i],i,G.getWeight(parent[i],i));
            G_copy.setEgde(i,parent[i],G.getWeight(i,parent[i]));
        }
        // Prints the vertex pair of mst
        for (int i = 1; i < G.getNumVertices(); i++)
            MST_sum += G.getWeight(parent[i],i);

        return MST_sum;
	}



	////find edge with maximum weight in the  path from u to v in MST
	/////////finish the recomputeMST method withing using computeMST method
	public static int recomputeMST(int u,int v,int weight,Graph G,Graph G_copy,int MSTweight){
	    int number_vertex = G_copy.getNumVertices();
	    int reMST = 0;
        if (u > number_vertex || v > number_vertex){
            G.setEgde(u,v,weight);
            G.setEgde(v,u,weight);
            return computeMST(G,G_copy);
        }
        String path = BFS_findpath.findPath(u,v,G_copy);
        String[] list = path.split(" ");
        int maxWeight = 0,position_x = 0, position_y = 0;
        for (int k = 0; k < list.length-1; k++) {
            if(maxWeight<G_copy.getWeight(Integer.parseInt(list[k]),Integer.parseInt(list[k+1]))){
                maxWeight = G_copy.getWeight(Integer.parseInt(list[k]),Integer.parseInt(list[k+1]));
                position_x = Integer.parseInt(list[k]);
                position_y = Integer.parseInt(list[k+1]);
            }
        }
        if(weight<maxWeight){
            G_copy.deleteEgde(position_x,position_y);
            G_copy.deleteEgde(position_y,position_x);
            G_copy.setEgde(v,u,weight);
            G_copy.setEgde(u,v,weight);
            reMST = MSTweight - maxWeight + weight;
        }
        else
            reMST = MSTweight;
        return reMST;


    }

	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
            System.err.println("Unexpected number of command line arguments");
            System.exit(1);
        }
		String graph_file = args[0];
		String change_file = args[1];
		String output_file = args[2];

		PrintWriter output;
		output = new PrintWriter(output_file, "UTF-8");

		Graph G = parseEdges(graph_file);
		Graph G_copy = new Graph(G.getNumVertices(),G.getNumVertices()-1);

		long startMST = System.nanoTime();
		int MSTweight = computeMST(G,G_copy);
		long finishMST = System.nanoTime();

		//Subtract the start time from the finish time to get the actual algorithm running time
		double MSTtotal = (finishMST - startMST)/1000000.0;

		//Write to output file the initial MST weight and time
		output.println(Integer.toString(MSTweight) + " " + Double.toString(MSTtotal));

		//Iterate through changes file
		BufferedReader br = new BufferedReader(new FileReader(change_file));
		String line = br.readLine();
		String[] split = line.split(" ");
		int num_changes = Integer.parseInt(split[0]);
		int u, v, weight;
        int newMST_weight;
        int count =0;
        double newMST_total = 0;
		while ((line = br.readLine()) != null) {
			split = line.split(" ");
			u = Integer.parseInt(split[0]);
			v = Integer.parseInt(split[1]);
			weight = Integer.parseInt(split[2]);
			//Run your recomputeMST function to recalculate the new weight of the MST given the addition of this new edge
			//Note: you are responsible for maintaining the MST in order to update the cost without recalculating the entire MST
            long start_newMST = System.nanoTime();
            newMST_weight = recomputeMST(u,v,weight,G,G_copy,MSTweight);
            ////change MSTweight to next recompute
            MSTweight = newMST_weight;
            long finish_newMST = System.nanoTime();

            newMST_total = (finish_newMST - start_newMST)/1000000.0;
            //Write new MST weight and time to output file
            output.println(Integer.toString(newMST_weight) + " " + Double.toString(newMST_total));
            // count++;
            // if (count == 999){
            //     System.out.println(newMST_weight+"------"+newMST_total);
            // }
        }

		output.close();
		br.close();

	}
}

