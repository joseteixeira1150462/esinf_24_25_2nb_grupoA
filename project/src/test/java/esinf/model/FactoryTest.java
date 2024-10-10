package esinf.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FactoryTest {

    @Test
    void testCalculateTotalProductionWithValidItems() {
        List<Item> items = Arrays.asList(new Item("id1", "Low", Arrays.asList("cut", "clear")), new Item("id2", "High", Collections.singletonList("clear")));
        List<Machine> machines = Arrays.asList(new Machine("m1", "cut",10), new Machine("m2", "clear",20));
        Factory factory = new Factory(items, machines);

        Map<String, Integer> actual = factory.calculateTotalProductionInMinutes(factory.getItems());

        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("id1", 30);
        expected.put("id2", 20);

        assertEquals(expected, actual);
    }

    @Test
    void testCalculateTotalProductionWithEmptyItems() {
        List<Machine> machines = Arrays.asList(new Machine("m1", "cut",10), new Machine("m2", "clear",20));
        Factory factory = new Factory(new ArrayList<>(), machines);

        Map<String, Integer> actual = factory.calculateTotalProductionInMinutes(new ArrayList<>());

        assertNull(actual);
    }

    @Test
    void testCalculateTotalProductionWithDifferentItems() {
        List<Item> items = Collections.singletonList(new Item("id1", "Low", Arrays.asList("cut", "clear")));
        List<Machine> machines = Arrays.asList(new Machine("m1", "cut",10), new Machine("m2", "clear",20));

        Factory factory = new Factory(items, machines);

        Map<String, Integer> actual = factory.calculateTotalProductionInMinutes(Collections.singletonList(new Item("id2", "Low", Collections.singletonList("cut"))));

        assertNull(actual);
    }

}