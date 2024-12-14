package esinf.model;

import esinf.model.ActivityVertex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ActivityVertexTest {
    @Test
    void testConstructorAndGetters() {
        ActivityVertex activity = new ActivityVertex("A", "Start", 2, "hours", 100.0, "USD");

        assertEquals("A", activity.getId(), "ID should match the value passed in the constructor.");
        assertEquals("Start", activity.getDescription(), "Description should match the value passed in the constructor.");
        assertEquals(2, activity.getDuration(), "Duration should match the value passed in the constructor.");
        assertEquals("hours", activity.getDurationUnit(), "Duration unit should match the value passed in the constructor.");
        assertEquals(100.0, activity.getCost(), "Cost should match the value passed in the constructor.");
        assertEquals("USD", activity.getCostUnit(), "Cost unit should match the value passed in the constructor.");
    }

    @Test
    void testSetters() {
        ActivityVertex activity = new ActivityVertex("A", "Start", 2, "hours", 100.0, "USD");

        activity.setId("B");
        activity.setDescription("Intermediate");
        activity.setDuration(3);
        activity.setDurationUnit("days");
        activity.setCost(200.0);
        activity.setCostUnit("EUR");

        assertEquals("B", activity.getId(), "ID should reflect the updated value.");
        assertEquals("Intermediate", activity.getDescription(), "Description should reflect the updated value.");
        assertEquals(3, activity.getDuration(), "Duration should reflect the updated value.");
        assertEquals("days", activity.getDurationUnit(), "Duration unit should reflect the updated value.");
        assertEquals(200.0, activity.getCost(), "Cost should reflect the updated value.");
        assertEquals("EUR", activity.getCostUnit(), "Cost unit should reflect the updated value.");
    }

    @Test
    void testToString() {
        ActivityVertex activity = new ActivityVertex("A", "Start", 2, "hours", 100.0, "USD");
        String expected = "ActivityVertex [ID: A, Description: Start, Duration: 2 hours, Cost: 100.0 USD]";
        assertEquals(expected, activity.toString(), "toString output should match the expected format.");
    }

    @Test
    void testEqualsAndHashCode() {
        ActivityVertex activity1 = new ActivityVertex("A", "Start", 2, "hours", 100.0, "USD");
        ActivityVertex activity2 = new ActivityVertex("A", "Start", 2, "hours", 100.0, "USD");
        ActivityVertex activity3 = new ActivityVertex("B", "Intermediate", 3, "days", 150.0, "EUR");

        assertEquals(activity1, activity2, "Activities with identical attributes should be equal.");
        assertNotEquals(activity1, activity3, "Activities with different attributes should not be equal.");
        assertEquals(activity1.hashCode(), activity2.hashCode(), "Hash codes of identical activities should match.");
        assertNotEquals(activity1.hashCode(), activity3.hashCode(), "Hash codes of different activities should not match.");
    }
}
