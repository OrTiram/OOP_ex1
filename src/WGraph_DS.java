package ex1.src;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Comparator;

public class WGraph_DS implements weighted_graph,Comparator<node_info> {
   private HashMap<Integer ,node_info> Vertics ;
   private HashMap<Integer,HashMap<Integer,Double>> Edge ;
   private int mc;
   private int edge;

    @Override
    public int compare(node_info o1, node_info o2) {
        if(o1.getTag() > o2.getTag()) return 1;
        if(o1.getTag() < o2.getTag()) return -1;
        if(o1 == o2) return 0;
        return 0;
    }

    //constructor
   public WGraph_DS() {
    this.Vertics = new HashMap<Integer ,node_info>();
    this.Edge = new HashMap<Integer,HashMap<Integer,Double>>();
    this.mc = 0 ;
    this.edge = 0 ;
   }

  //Checks whether two objects are identical
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        if ((wGraph_ds.nodeSize() != nodeSize() || (wGraph_ds.edgeSize() != edge))) {
            return false;
        }
        for (node_info n : this.getV()) {
            if (!(wGraph_ds.getNode(n.getKey()).equals(n))) {
                return false;
            }
        }
        for (node_info N : this.Vertics.values()) {
            if (wGraph_ds.getNode(N.getKey()) == null) return false;
            for (node_info node : this.getV(N.getKey())) {
                if (this.getEdge(N.getKey(), node.getKey()) != wGraph_ds.getEdge(N.getKey(), node.getKey()))
                    return false;
            }
        }
        return true;
    }

    //copy constructor
    public WGraph_DS(weighted_graph g) {
        if (g != null) {
            this.Vertics = new HashMap<Integer, node_info>();
            this.Edge = new HashMap<Integer, HashMap<Integer, Double>>();
            for (node_info N : g.getV()) {
                this.Vertics.put(N.getKey(), new NodeInfo(N));//Copy the vertices
            }

            for (node_info I : g.getV()) {
                this.Edge.put(I.getKey(), new HashMap<Integer, Double>());
                for (node_info K : g.getV(I.getKey())) {
                    //Copies the Edge and connects the vertices
                    this.Edge.get(I.getKey()).put(K.getKey(), g.getEdge(I.getKey(), K.getKey()));
                }
            }
            this.edge = g.edgeSize();
            this.mc = g.getMC();
        }
    }
//An internal class that creates the vertices of the graph
    private class NodeInfo implements node_info{
        private int key;
        private String info;
        private double tag;

        public NodeInfo(int key) {
            this.key = key;
            this.info = "";
            this.tag = 0;
        }
        //copy constructor
        public NodeInfo(node_info node){
            this.key = node.getKey();
            this.tag = node.getTag();
            this.info = node.getInfo();

        }
   //Checks whether two objects are identical
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof node_info){
                if (this.tag == ((node_info)obj).getTag() && this.info == ((node_info) obj).getInfo() && this.key == ((node_info) obj).getKey())
                    return true;
            }
            return false;
        }

        @Override
        public int getKey() {
            return this.key;
        }
    //Return the comment (metadata) belonging to this node
        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s ;
        }
    //return the temporary data
        @Override
        public double getTag() {
            return this.tag;
        }
    //Updating the node tag
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "key = " + this.key +
                ", info = ' " + this.info + '\'' +
                ", tag = " + this.tag +
                '}';
    }
}

    //Returns the corresponding node according to the key obtained
    @Override
    public node_info getNode(int key) {
       if (Vertics.containsKey(key)) {
           return Vertics.get(key);
       }
       return null;
    }

    //Returns whether there is a edge between two vertices
    @Override
    public boolean hasEdge(int node1, int node2) {
        //We will first check if node1 and node2 is contained in the list of vertices
        //and check if node2 contains in Edge of node1
        return Vertics.containsKey(node1) && Vertics.containsKey(node2) && Edge.get(node1).containsKey(node2);

    }

//Returns the weight of the edge between two vertices
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1 , node2) == true){//If there is a edge between them
          return  Edge.get(node1).get(node2).doubleValue();//We will return the value double from the hashmap

        }
        //no edge between them
        return -1;
    }

//Add Node to vertices
    @Override
    public void addNode(int key) {
        if (!Vertics.containsKey(key)){
            Vertics.put(key,new NodeInfo(key));/// add the node to the vertices according to his key
            Edge.put(key,new HashMap<Integer, Double>());//add him to the Edge hashmap
            mc++;//We will count a change in the graph
        }
    }

    //Connection between two nodes
    @Override
    public void connect(int node1, int node2, double w) {
            if (node1 != node2 && Vertics.containsKey(node1) && Vertics.containsKey(node2)) {
                //no rib between them
                if (hasEdge(node1, node2) == false) {
                    Edge.get(node1).put(node2, w);//connect between the nodes and update the weight
                    Edge.get(node2).put(node1, w);
                    edge++;//add a rib
                    mc++;// count change
                }
               // There is a edge between them but the weight is not up to date
                else if (hasEdge(node1, node2) == true && getEdge(node1,node2) != w){
                    Edge.get(node1).put(node2, w);
                    Edge.get(node2).put(node1, w);
                    mc++;//We just updated the weight without adding a rib
                }
        }
    }

    //Returns the collection that contains the vertices
    @Override
    public Collection<node_info> getV() {
        if (Vertics != null) {
            return Vertics.values();
        }
        return null;
    }

    //Returns the neighbors of a particular vertex
    @Override
    public Collection<node_info> getV(int node_id){
        if (!Vertics.containsKey(node_id)) return null;
        Collection<node_info> temp = new ArrayList<>();
        for (Integer N: Edge.get(node_id).keySet()) {//We will go over the key in the edge hashmap that are the neighbors
            temp.add(Vertics.get(N));
        }
        return temp;
    }

    //Removes the resulting node from the current list
    @Override
    public node_info removeNode(int key) {
        if (Vertics.containsKey(key)){
            //We will go over his list of neighbors
            for (node_info N: this.getV(key)) {
                //We will remove it from each of its neighbors
                Edge.get(N.getKey()).remove(key);
                edge--;
                mc++;
            }

            Edge.remove(key);
            mc++;
            return Vertics.remove(key);
        }
        return  null;
    }

    @Override
    public void removeEdge(int node1, int node2) {
        //If they are in the graph and also neighbors
        if(Vertics.containsKey(node1) && Vertics.containsKey(node2) && hasEdge(node1,node2)) {
            Edge.get(node1).remove(node2);
            Edge.get(node2).remove(node1);
            mc++;//We will count a change in the graph
            edge--;//Removal of rib
        }
    }

//return the number of vertices in the graph.
    @Override
    public int nodeSize() {
        if (Vertics != null) return Vertics.size();
        return  0;//the vertices empty
    }

//return the number of edge in the graph.
    @Override
    public int edgeSize() {
    return edge;
    }

 //return the mode Count - for changes in the graph.
    @Override
    public int getMC() {
        return mc;
    }

    @Override
    public String toString() {
    StringBuilder S = new StringBuilder();
     S.append("Num of Nodes = " + nodeSize() + "\n");
     S.append("Num of Edges = " + edgeSize() + "\n");
     S.append("\n");
     S.append("HashMap of Nodes : " + "\n" );
        for (node_info N : getV()) {
            S.append(N.toString());
            S.append("\n");
        }
        S.append("\n");
        S.append("HashMap of Edge : " + "\n");
        for (Integer K: Edge.keySet()) {
            S.append("Key = " + K + "\n");
            for (node_info N : getV(K)) {
                S.append(" The neighbor = " + N.getKey());
                S.append(", The edge between them is : " + getEdge(N.getKey() , K) + "\n" );
            }
         S.append("\n");
        }
     return S.toString();
    }
}
