import java.util.Comparator;

public class comparator implements Comparator<vertex> {
    public int compare(vertex v0,vertex v1){
        return v0.key - v1.key;
    }
}
