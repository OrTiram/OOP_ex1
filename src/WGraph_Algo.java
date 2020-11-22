package ex1.src;
import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private HashMap<Integer,Integer> shortPath1 = new HashMap<Integer, Integer>();
    private weighted_graph Gr;

//constructor
    public WGraph_Algo() {
        this.Gr = new WGraph_DS();
    }

    //copy constructor
    public WGraph_Algo(weighted_graph g) {
        this.Gr = g;
    }

    // Initialize the graph on which this set of algorithms operates on
    @Override
    public void init(weighted_graph g) {
        this.Gr = g;
    }

//return this Graph
    @Override
    public weighted_graph getGraph() {
        return this.Gr;
    }

//deep copy
    @Override
    public weighted_graph copy() {
        weighted_graph G = new WGraph_DS(this.Gr);//Create a new WGraph_DS graph
        return G;
    }

//Checks if the graph is linked
    @Override
    public boolean isConnected() {
        //If there are no vertices or one vertex the graph is linked
        if (Gr.getV().size() == 1 || Gr.getV().size() == 0) return true;
        for (node_info node : Gr.getV()) {
            return Bfs(node.getKey());
        }
        return false;
    }

    public boolean Bfs(int key) {
        for (node_info node : Gr.getV()) {
            //We will first perform all the vertices in white and update their value to infinity
            node.setInfo("White");
            node.setTag(Integer.MAX_VALUE);
        }
        //The vertex obtained according to the key we will work on, we will color it gray and update its value to 0
        Gr.getNode(key).setTag(0);
        Gr.getNode(key).setInfo("Gray");
        //We'll create a queue that contains the vertex's neighbors
        Queue<node_info> QNi = new LinkedList<>();
        QNi.add(Gr.getNode(key));
        node_info u;

        while (!QNi.isEmpty()) {
            u = QNi.peek();//The first vertex in line
            for (node_info node2 : Gr.getV(u.getKey())) {//We will go over the neighbors of this vertex
                if (node2.getInfo().equals("White")) {
                    node2.setInfo("Gray");//Each vertex that is discovered turns gray
                    node2.setTag(Math.min(node2.getTag(), u.getTag() + 1));//We will update his tag to a minimum between him and his parent vertex
                    QNi.add(node2);//After updating each vertex we will add it to the queue
                }
            }
            //We went through all the neighbors of the vertex and will update it to black
            u.setInfo("Black");
            //We'll take him out of line and move on to the next vertex
            QNi.poll();
        }
        //If there is a vertex that we have not passed and it remains white, the graph is not a link
        for (node_info node : Gr.getV()) {
            if (node.getInfo().equals("White")) return false;
        }
        //the graph is link
        return true;
    }

//returns the length of the shortest path between src to dest
    @Override
    public double shortestPathDist(int src, int dest) {
        PriorityQueue<node_info> shorPath = new PriorityQueue<>(new WGraph_DS());
        Iterator<node_info> it = Gr.getV().iterator();
       // If one of the nodes is not in the graph
        if (Gr.getNode(dest) == null || Gr.getNode(src) == null ) return -1 ;
        //Same vertex
        if (src == dest) return 0;
        while (it.hasNext()) {
            it.next().setTag(Double.MAX_VALUE);
        }
        Gr.getNode(src).setTag(0);
        shorPath.add(Gr.getNode(src));
        while (!shorPath.isEmpty()) {
         // Iterator passing through all the neighbors
           Iterator<node_info> itN = Gr.getV(shorPath.peek().getKey()).iterator();
            while (itN.hasNext()) {
                node_info temp = itN.next();
                // Find the smallest tag
                if (shorPath.peek().getTag() + Gr.getEdge(shorPath.peek().getKey(), temp.getKey()) < temp.getTag()) {
               // We would like to update the neighbor's tag to the smallest tag we found
                    temp.setTag(shorPath.peek().getTag() + Gr.getEdge(shorPath.peek().getKey(), temp.getKey()));
                    shorPath.add(temp);
        // We'll keep in the hashmap every neighbor's key and his parent's key, will help us print the path
                   shortPath1.put(temp.getKey(), shorPath.peek().getKey());
                }
            }
            //When we have finished going through all of Node's neighbors we will delete it from the queue and return to the next vertex until the queue is empty
            if (!shorPath.isEmpty()) shorPath.remove();
        }
// If we have not reached the top of the dest, that is, there is no path between them
     if(Gr.getNode(dest).getTag() == Double.MAX_VALUE) return -1;
      // The dest tag is the length of the shortest path
       return Gr.getNode(dest).getTag();
    }

    //returns the the shortest path between src to dest
    @Override
    public List<node_info> shortestPath(int src, int dest) {
     // If there is no path between them we will return null
     if (shortestPathDist(dest, src) == -1) {
            return null;
        }
        // We will create a linkedlist that will hold the nodes who are on the path
        LinkedList<node_info> shorPath = new LinkedList<>();
        //Same vertex, we return one of them
        if (src == dest) {
            shorPath.add(Gr.getNode(src));
            return shorPath;
        }
        node_info N = Gr.getNode(src);
        shorPath.add(Gr.getNode(src));
        //As long as we have not yet reached the last vertex in the path
        while (!(N.equals(Gr.getNode(dest)))) {
        //We will add to the path the node from the hashmap we created in the shortestPathDist
        shorPath.add(Gr.getNode(shortPath1.get(N.getKey())));
            //We will advance N to the next node on the path
            N = Gr.getNode(shortPath1.get(N.getKey()));
        }
        return shorPath;
    }


    @Override
    //https://www.geeksforgeeks.org/serialization-in-java/
    // Saves this weighted (undirected) graph to the given file name
    public boolean save(String file) {
        try {
            //Saving of object in a file
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // Method for serialization of object
            out.writeObject(Gr);
            out.close();
            fileOut.close();

        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    @Override
//https://www.geeksforgeeks.org/serialization-in-java/
    //load a graph to this graph algorithm
    public boolean load(String file) {
        try {
            // Reading the object from a file
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            // Method for deserialization of object
            Gr = (WGraph_DS) in.readObject();
            in.close();
            fileIn.close();

        } catch (IOException ex) {
            return false;
        } catch (ClassNotFoundException ex) {
            return false;
        }
        return true;

    }
}

