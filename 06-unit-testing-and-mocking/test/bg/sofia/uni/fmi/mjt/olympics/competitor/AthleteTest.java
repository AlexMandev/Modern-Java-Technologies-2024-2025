package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {
    @Test
    void testHashCodeWithSameNamesButDifferentId() {
        Competitor first = new Athlete("Athlete_1", "John", "USA");
        Competitor second = new Athlete("Athlete_2", "John", "USA");

        assertNotEquals(first.hashCode(), second.hashCode(),
                "Athletes with same names, but different identifiers should have different hash codes.");
    }

    @Test
    void testAddGoldMedal() {
        Competitor c = new Athlete("Athlete_1", "John", "USA");
        c.addMedal(Medal.GOLD);
        Collection<Medal> medals = c.getMedals();
        assertTrue(medals.contains(Medal.GOLD));
    }

    @Test
    void testGetMedalsReturnsUnmodifiableSet() {
        Athlete athlete = new Athlete("1", "John Doe", "USA");

        athlete.addMedal(Medal.GOLD);

        Collection<Medal> medals = athlete.getMedals();
        assertThrows(UnsupportedOperationException.class, () -> medals.add(Medal.SILVER),
                "The medals collection should be unmodifiable");
    }

    @Test
    void testAddMedalThrowsExceptionWhenMedalIsNull() {
        Athlete athlete = new Athlete("1", "John Doe", "USA");

        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null),
                "Adding a null medal should throw an IllegalArgumentException");
    }

    @Test
    void testEqualsReturnsTrueForIdenticalAthletes() {
        Competitor first = new Athlete("Athlete_1", "John", "USA");
        Competitor second = new Athlete("Athlete_1", "John", "USA");

        assertEquals(first, second, "Equals should return true for identical athletes.");
    }

    @Test
    void testEqualsReturnsFalseForDifferentAthletes() {
        Athlete first = new Athlete("Athlete_1", "John", "USA");
        Athlete second = new Athlete("Athlete_2", "Ivan", "Bulgaria");

        assertFalse(first.equals(second), "Two athletes with different data should not be equal");
    }

    @Test
    void testAddMultipleMedals() {
        Athlete athlete = new Athlete("1", "John", "USA");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        Collection<Medal> medals = athlete.getMedals();
        assertEquals(3, medals.size(), "Adding 3 medals should make the size of the medal collection 3");
    }
}
