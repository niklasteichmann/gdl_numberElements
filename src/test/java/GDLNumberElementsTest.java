import org.junit.Test;
import org.s1ck.gdl.GDLHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by niklas on 25.11.15.
 */
public class GDLNumberElementsTest {
  @Test
  public void imLazyTest() {
    try {
      NumberElements numberElements = new NumberElements();
      GDLHandler handler = new GDLHandler.Builder()
        .buildFromFile("src/test/resources/social_network.gdl");
      numberElements
        .numberElements(handler.getVertices(), handler.getEdges(), 1L);
      Map<Long, Long> numberedVertices = numberElements.getNumberedVertices();
      Map<Long, Long> numberedEdges = numberElements.getNumberedEdges();
      List<Long> numeration = new ArrayList<>();
      numeration.addAll(numberedVertices.values());
      numeration.addAll(numberedEdges.values());
      Collections.sort(numeration);
      long maximumValue = numeration.get(numeration.size()-1) + 1L;
      long elementCount = numeration.size();
      assertEquals("maximum is incorrect", maximumValue, elementCount);

      long lastValue = -1;
      for(Long value : numeration){
        assertTrue("value occurs twice", value != lastValue);
        assertTrue("value is in wrong interval", value >= 0 && value <=
          maximumValue);
        lastValue = value;
      }
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }
}
