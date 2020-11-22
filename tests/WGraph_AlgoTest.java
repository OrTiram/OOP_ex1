package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class WGraph_AlgoTest {
    @Test
    public void equalTest() {
        weighted_graph Gr1 = new_Graph(6);
        weighted_graph Gr2 = new_Graph(6);
        assertEquals(Gr1,Gr1);
        assertEquals(Gr1,Gr2);
        Gr1.connect(3,4,4);
        Gr1.connect(2,5,2);
        Gr1.connect(1,3,2);
        Gr2.connect(3,4,4);
        Gr1.removeEdge(2,5);
        Gr1.removeEdge(1,3);
        assertEquals(Gr1,Gr2);
        assertEquals(Gr1.edgeSize() , Gr2.edgeSize());
    }

    public weighted_graph new_Graph(int v_size) {
        weighted_graph g = new WGraph_DS();
        for (int i = 1; i <= v_size; i++) {
            g.addNode(i);
        }
        return g;
    }

    @Test
    void init() {
        weighted_graph Gr = new_Graph(20);
        for (int i = 1; i <= Gr.nodeSize(); i++) {
            Gr.connect(i * 2, i, 2);
        }
        weighted_graph_algorithms Graph = new WGraph_Algo();
        Graph.init(Gr);
        assertSame(Gr, Graph.getGraph());
        Gr.addNode(21);
        assertEquals(Gr.nodeSize(), Graph.getGraph().nodeSize());
        Gr.removeNode(14);
        assertNull(Graph.getGraph().getNode(14));
    }

    @Test
    void copy() {
        weighted_graph Gr = new_Graph(20);
        for (int i = 1; i <= Gr.nodeSize(); i++) {
            Gr.connect(i * 3, i, 2);
        }
        WGraph_Algo Graph = new WGraph_Algo();
        Graph.init(Gr);
        weighted_graph graph_copy = Graph.copy();
        assertEquals(graph_copy, Gr);
        assertEquals(graph_copy.edgeSize(), Gr.edgeSize());
        assertEquals(graph_copy.nodeSize(), Gr.nodeSize());
        assertEquals(graph_copy.getMC(), Gr.getMC());
        Gr.addNode(22);
        assertNotEquals(graph_copy.getNode(22), Gr.getNode(22));
        graph_copy.removeNode(15);
        assertNull(graph_copy.getNode(15));
        assertNotNull(Gr.getNode(15));
        assertEquals(graph_copy.getV(10).size(), Gr.getV(10).size());
    }

    @Test
    void isConnected() {
        weighted_graph Gr = new_Graph(20);
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr);
        for (int i = 1; i <= Graph.getGraph().nodeSize(); i++) {
            Graph.getGraph().connect(i, i + 1, 2);
        }
        assertTrue(Graph.isConnected());
        Graph.getGraph().removeNode(5);
        assertFalse(Graph.isConnected());
        for (int i = 1; i <= Graph.getGraph().nodeSize(); i++) {
            Graph.getGraph().removeNode(i);
        }
        assertTrue(Graph.isConnected());
        Graph.getGraph().addNode(22);
        assertFalse(Graph.isConnected());
        Graph.getGraph().connect(20, 22, 2);
        assertTrue(Graph.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph Gr = new_Graph(20);
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr);
        Graph.getGraph().connect(1, 2, 1);
        Graph.getGraph().connect(2, 3, 2.5);
        Graph.getGraph().connect(3, 4, 3);
        Graph.getGraph().connect(4, 13, 4.5);
        Graph.getGraph().connect(2, 13, 15);
        Graph.getGraph().connect(4, 14, 1);
        assertEquals(6.5, Graph.shortestPathDist(2, 14));
        assertEquals(11, Graph.shortestPathDist(1, 13));
        assertEquals(-1, Graph.shortestPathDist(5, 15));
        assertEquals(4, Graph.shortestPathDist(3, 14));
    }

    @Test
    void shortestPath1() {
        weighted_graph Gr = new_Graph(5);
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr);
        Gr.connect(1,2,3.5);
        Gr.connect(4,5,0);
        Gr.connect(2,3,5.5);
        Gr.connect(1,3,4);
        Gr.connect(3,5,2.5);
        Gr.connect(2,4,1.5);
        assertEquals(4 ,Graph.shortestPathDist(2,3));
        Gr.connect(4,5,6);
        assertEquals(5.5 , Graph.shortestPathDist(2,3));
        List<node_info> shotrPath = Graph.shortestPath(2,3);
        assertEquals(2 , shotrPath.size());
        assertEquals(2,shotrPath.get(0).getKey());
        shotrPath.remove(0);
        assertNotEquals(2,shotrPath.size());
        }

    @Test
    void shortestPath2() {
        weighted_graph Gr = new WGraph_DS();
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr);
        Gr.addNode(0);
        Gr.addNode(1);
        Gr.addNode(2);
        Gr.addNode(3);
        Gr.addNode(4);
        Gr.connect(0, 1, 0);
        Gr.connect(1, 2, 0);
        Gr.connect(2, 3, 0);
        Gr.connect(3, 4, 0);
        Gr.connect(0, 4, 1);
        Gr.connect(0, 3, 1);
        assertEquals(0.0, Graph.shortestPathDist(0, 4));
        List<node_info> l = Graph.shortestPath(0, 4);
        for (int i = 0; i < l.size(); i++) {
            assertEquals(i, l.get(i).getKey());
        }
    }
    @Test
    void copyRunTime(){
        weighted_graph Gr = new_Graph(1000000);
        for (int i = 0; i < 100000 ; i++)
            for (int j = 0; j < 11 ; j++)
                Gr.connect(i,j,1);
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr);
        assertTimeoutPreemptively(Duration.ofMillis(100000), () -> {
            weighted_graph graph_copy = Graph.copy();
            System.out.println(graph_copy.edgeSize());

        });
    }

    @Test
    void save_load() {
        weighted_graph Gr1 = new_Graph(50);
        Gr1.connect(1,2,3);
        Gr1.connect(2,3,4);
        weighted_graph_algorithms Graph = new WGraph_Algo(Gr1);
        String file = "graph";
        Graph.save(file);
        Graph.load(file);
        weighted_graph Gr2 = new_Graph(50);
        Gr2.connect(1,2,3);
        Gr2.connect(2,3,4);
        assertEquals(Gr1,Gr2);
        Gr1.removeEdge(1,2);
        //Gr2.removeEdge(1,2);
        assertNotEquals(Gr1,Gr2);
    }

}

