import java.util.LinkedList;

public class BFS_findpath {
    /////BFS find the path from v to w and print it
    public static String findPath(int v, int w,Graph G) {
        LinkedList<Integer> q = new LinkedList<Integer>();
        boolean[] visited = new boolean[G.getNumVertices()];
        String[] pathTo = new String[G.getNumVertices()];/// pathto means the path
        q.add(v);
        pathTo[v] = v+" ";
        while(q.peek() != null) {
            if(runBFS(q.poll(),w,visited,G,q,pathTo))
                break;
        }
        return pathTo[w];
    }
    ////use BFS to find the path from v to w
    private static boolean runBFS(int v, int w, boolean[] visited,Graph G, LinkedList<Integer> q, String[] pathTo) {
        if(visited[v]) {
        }
        else if(v == w)
            return true;
        else {
            visited[v] = true;
            LinkedList<node> vi = G.getList(v);
            for(int i =0;i<vi.size();i++) {
                int nextVertex = vi.get(i).destination;
                pathTo[nextVertex] = pathTo[v] + nextVertex + " ";
                q.add(nextVertex);
            }
        }
        return false;
    }
}
