package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    public weighted_graph newGraph(int nodeSize) {
        weighted_graph G = new WGraph_DS();
        for (int i = 0; i < nodeSize; i++) {
            G.addNode(i);
        }
        return G;
    }

    @Test
    void getNode() {
        weighted_graph graph = new WGraph_DS();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        assertNull(graph.getNode(5));
        assertNotNull(graph.getNode(3));
    }

    @Test
    void hasEdge() {
        weighted_graph graph = new WGraph_DS();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.connect(1, 2, 4);
        graph.connect(3, 4, 4);
        graph.connect(5, 2, 4);
        graph.connect(3, 3, 13);
        assertTrue(graph.hasEdge(1, 2));
        assertFalse(graph.hasEdge(5, 2));
        assertFalse(graph.hasEdge(3, 3));
    }

    @Test
    void getEdge() {
        weighted_graph graph = new WGraph_DS();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);
        graph.addNode(6);
        graph.addNode(7);
        graph.addNode(8);
        graph.addNode(9);
        graph.addNode(10);
        graph.connect(1, 10, 4);
        graph.connect(3, 4, 20);
        graph.connect(5, 2, 45);
        graph.connect(3, 3, 13);
        graph.connect(8, 9, 2);
        graph.connect(7, 5, 1);
        graph.connect(10, 2, 23);
        graph.connect(4, 6, 15);
        assertEquals(4, graph.getEdge(1, 10));
        assertEquals(-1, graph.getEdge(8, 6));
        assertEquals(-1, graph.getEdge(1, 9));
        assertEquals(23, graph.getEdge(2, 10));
        assertEquals(-1, graph.getEdge(3, 3));
        assertEquals(45, graph.getEdge(5, 2));
        graph.removeEdge(1, 10);
        assertEquals(-1, graph.getEdge(1, 10));
    }

    @Test
    void addNode() {
        weighted_graph Gr = newGraph(15);
        node_info D = Gr.getNode(4);
        node_info T = Gr.getNode(18);
        node_info Y = Gr.getNode(22);
        assertSame(D, Gr.getNode(4));
        assertNull(T);
        assertNull(Y);
        assertNotNull(Gr.getNode(10));
    }

    @Test
    void connect() {
        weighted_graph Gr = newGraph(20);
        Gr.connect(3, 4, 5);
        Gr.connect(0, 2, 0);
        Gr.connect(5, 8, 1);
        Gr.connect(7, 10, 7);
        assertEquals(true, Gr.hasEdge(3, 4));
        assertEquals(-1, Gr.getEdge(7, 4));
        assertEquals(7, Gr.getEdge(7, 10));
        Gr.removeEdge(3, 4);
        assertEquals(false, Gr.hasEdge(3, 4));
        assertEquals(false, Gr.hasEdge(30, 30));
    }

    @Test
    void getV() {
        weighted_graph Gr = newGraph(15);
        assertEquals(0, Gr.getV(4).size());
        Gr.connect(1, 2, 1);
        Gr.connect(2, 3, 2);
        Gr.connect(3, 4, 3);
        Gr.connect(4, 5, 4);
        assertNotEquals(5, Gr.getV(1));
        Gr.connect(3, 5, 4);
        assertEquals(3, Gr.getV(3).size());
        assertEquals(2, Gr.getV(4).size());
        Gr.removeNode(3);
        assertEquals(1, Gr.getV(4).size());
    }


    @Test
    void removeNode() {
        weighted_graph Gr = newGraph(25);
        Gr.removeNode(30);
        Gr.removeNode(14);
        Gr.removeNode(1);
        assertEquals(null, Gr.getNode(30));
        assertEquals(null, Gr.getNode(14));
        assertEquals(null, Gr.getNode(1));
    }

    @Test
    void removeEdge() {
        weighted_graph Gr = newGraph(15);
        Gr.connect(1, 2, 3.14);
        Gr.connect(3, 4, 0);
        Gr.connect(4, 5, 2);
        Gr.connect(6, 10, 4.5);
        Gr.connect(5, 10, 1);
        assertNotEquals(4, Gr.getEdge(4, 5));
        assertEquals(4.5, Gr.getEdge(6, 10));
        Gr.removeEdge(4, 5);
        assertEquals(-1, Gr.getEdge(4, 5));
    }

    @Test
    void nodeSize() {
        weighted_graph Gr = newGraph(45);
        Gr.removeNode(10);
        Gr.removeNode(20);
        assertEquals(43, Gr.nodeSize());
        Gr.addNode(15);
        assertEquals(43, Gr.nodeSize());
        Gr.removeNode(60);
        assertEquals(43, Gr.nodeSize());
        Gr.removeNode(40);
        assertEquals(42, Gr.nodeSize());
    }

    @Test
    void edgeSize() {
        weighted_graph Gr = newGraph(25);
        Gr.connect(0, 1, 3);
        Gr.connect(4, 8, 5);
        Gr.connect(14, 20, 10);
        Gr.connect(3, 7, 1);
        assertEquals(4, Gr.edgeSize());
        Gr.connect(0, 1, 3);
        assertEquals(4, Gr.edgeSize());
        Gr.removeNode(14);
        assertNotEquals(4, Gr.edgeSize());
    }

    @Test
    void getMC() {
        weighted_graph Gr = newGraph(25);
        Gr.connect(0, 1, 3);
        Gr.connect(2, 3, 2);
        Gr.connect(3, 4, 5);
        Gr.connect(6, 7, 1);
        assertEquals(29, Gr.getMC());
        Gr.removeNode(0);
        Gr.removeEdge(2, 3);
        assertEquals(32, Gr.getMC());
        Gr.connect(3, 4, 4);
        assertEquals(33, Gr.getMC());
    }

    @Test
    public void BuildRunTime() {
        assertTimeoutPreemptively(Duration.ofMillis(10000), () -> {
            weighted_graph Gr = newGraph(1000000);
            for (int i = 0; i < Gr.nodeSize(); i++)
                for (int k = 0; k < 11; k++)
                    Gr.connect(i, k, 1);

        });
    }
}