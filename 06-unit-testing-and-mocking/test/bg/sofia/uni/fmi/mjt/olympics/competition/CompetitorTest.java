package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class CompetitorTest {
    @Test
    void testBlankCompetitionNameThrows() {
        Competitor competitor1 = new Athlete("1", "Ivan", "Bulgaria");
        assertThrows(IllegalArgumentException.class, () -> new Competition("", ".",
                        Set.of(competitor1)),
                "Competition should throw when the given name is blank.");
    }

    @Test
    void testNullCompetitionNameThrows() {
        Competitor competitor1 = new Athlete("1", "Ivan", "Bulgaria");
        assertThrows(IllegalArgumentException.class, () -> new Competition(null, ".",
                        Set.of(competitor1)),
                "Competition should throw when the given name is null.");
    }

    @Test
    void testBlankDiscipline() {
        Competitor competitor1 = new Athlete("1", "Ivan", "Bulgaria");
        assertThrows(IllegalArgumentException.class, () -> new Competition("Running", "",
                        Set.of(competitor1)),
                "Competition should throw when the given discipline is blank.");
    }

    @Test
    void testCompetitionWithNoCompetitorsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new Competition("Running", "2000m", Collections.emptySet()),
                "Should not create a competition with 0 competitors.");
    }

    @Test
    void testEqualsForIdenticalCompetitions() {
        Competitor competitor1 = new Athlete("1", "Ivan", "BG");

        Competition comp1 = new Competition("Weightlifting", "100kg", Set.of(competitor1));
        Competition comp2 = new Competition("Weightlifting", "100kg", Set.of(competitor1));

        assertEquals(comp1, comp2, "Equals should return true for identical competitions.");
    }

    @Test
    void hashCodeForIdenticalCompetitions() {
        Competitor competitor1 = new Athlete("1", "Ivan", "BG");

        Competition comp1 = new Competition("Weightlifting", "100kg", Set.of(competitor1));
        Competition comp2 = new Competition("Weightlifting", "100kg", Set.of(competitor1));

        assertEquals(comp1.hashCode(), comp2.hashCode(),
                "HashCode for identical competitions should be the same.");
    }

    @Test
    void testCompetitorsShouldReturnUnmodifiableSet() {
        Competitor competitor1 = new Athlete("1", "Ivan", "BG");

        Competition comp = new Competition("Weightlifting", "100kg", Set.of(competitor1));

        Set<Competitor> competitors = comp.competitors();
        assertThrows(UnsupportedOperationException.class, () -> competitors.add(competitor1),
                "Competitors should return an unmodifiable set.");
    }

}
