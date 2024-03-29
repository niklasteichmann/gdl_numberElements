import org.s1ck.gdl.model.Edge;
import org.s1ck.gdl.model.Vertex;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * Created by niklas on 25.11.15.
 */

/**
 * Class used to assign a Long to each GraphElement (vertex and edge) in two
 * Collections. Ignores unreachable elements and starts at a vertex with a
 * given id. Uses depth-first search.
 */
public class NumberElements {

  /**
   * Map containing a number for each vertex.
   */
  private Map<Long, Long> numberedVertices;
  /**
   * Map containing a number for each edge.
   */
  private Map<Long, Long> numberedEdges;
  /**
   * Getter for vertexID - number map
   * @return map containing a number for each vertex
   */
  public Map<Long, Long> getNumberedVertices() {
    return numberedVertices;
  }
  /**
   * Getter for edgeID - number map
   * @return map containing a number for each edge
   */
  public Map<Long, Long> getNumberedEdges() {
    return numberedEdges;
  }

  /**
   * creates a new NumberElements object with empty maps
   */
  public NumberElements() {
    this.numberedVertices = new HashMap<>();
    this.numberedEdges = new HashMap<>();
  }

  /**
   * constructs a map, that contains all edges of a vertex
   * @param edges the edges of the graph
   * @return a map containing all edges per vertexID
   */
  public Map<Long, Set<Edge>> constructEdgesOfVertexMap(
    Collection<Edge> edges) {
    //this map is needed to quickly get all ingoing
    // and outgoing edges of a vertex
    Map<Long, Set<Edge>> edgesOfVertexMap = new HashMap<>();
    for (Edge edge : edges) {
      Long source = edge.getSourceVertexId();
      Long target = edge.getTargetVertexId();
      //ignore loops
      if (!source.equals(target)) {
        //add the edge to both its source and target vertex id
        Set<Edge> connectedEdges = edgesOfVertexMap.get(source);
        if (connectedEdges == null) {
          connectedEdges = new HashSet<>();
        }
        connectedEdges.add(edge);
        edgesOfVertexMap.put(source, connectedEdges);
        connectedEdges = edgesOfVertexMap.get(target);
        if (connectedEdges == null) {
          connectedEdges = new HashSet<>();
        }
        connectedEdges.add(edge);
        edgesOfVertexMap.put(target, connectedEdges);
      }
    }
    return edgesOfVertexMap;
  }

  /**
   * Method for computing the numbers for each element
   * @param vertices vertices of the graph
   * @param edges edges of the graph
   * @param startingVertexID ID of the vertex, from which the depth search
   *                         starts
   */
  public void numberElements(Collection<Vertex> vertices,
    Collection<Edge> edges, Long startingVertexID) {
    Map<Long, Set<Edge>> edgesOfVertexMap = constructEdgesOfVertexMap(edges);
    //fill the result maps, -1 means unreachable
    for (Vertex vertex : vertices) {
      numberedVertices.put(vertex.getId(), -1L);
    }
    for (Edge edge : edges) {
      numberedEdges.put(edge.getId(), -1L);
    }
    Long counter = 0L;
    //first step, the vertex is number 0, his edges are added to the queue
    numberedVertices.put(startingVertexID, counter++);
    ArrayDeque<Edge> edgeQueue = new ArrayDeque<>(0);
    for (Edge edge : edgesOfVertexMap.get(startingVertexID)) {
      edgeQueue.addLast(edge);
      numberedEdges.put(edge.getId(), counter++);
    }
    //go through all reachable edges
    while (!edgeQueue.isEmpty()) {
      Edge currentEdge = edgeQueue.removeFirst();
      //because edge direction is ignored, you must make sure to
      //not walk in circles
      Long currentVertexID = currentEdge.getTargetVertexId();
      if (numberedVertices.get(currentVertexID) >= 0) {
        currentVertexID = currentEdge.getSourceVertexId();
      }
      //if the new vertex was visited before, ignore it
      if (numberedVertices.get(currentVertexID) < 0) {
        numberedVertices.put(currentVertexID, counter++);
        //add all not already visited edges to the queue
        for (Edge edge : edgesOfVertexMap.get(currentVertexID)) {
          if (numberedEdges.get(edge.getId()) < 0) {
            edgeQueue.addLast(edge);
            numberedEdges.put(edge.getId(), counter++);
          }
        }
      }
    }
  }
}

