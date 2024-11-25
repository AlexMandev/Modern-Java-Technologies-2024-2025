package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.comparator.IdentifierCompetitorComparator;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MJTOlympicsTest {
    @Test
    void testUpdateMedalStatsWithCompetitionOfFourAthletes() {
        CompetitionResultFetcher competitionResultFetcher = mock();

        Competitor competitor1 = new Athlete("1", "Ivan", "Bulgaria");
        Competitor competitor2 = new Athlete("2", "George", "USA");
        Competitor competitor3 = new Athlete("3", "Hans", "Germany");
        Competitor competitor4 = new Athlete("4", "Josh", "UK");

        TreeSet<Competitor> ranking = new TreeSet<>(new IdentifierCompetitorComparator());
        ranking.addAll(Set.of(competitor1, competitor2, competitor3, competitor4));
        when(competitionResultFetcher.getResult(any())).thenReturn(ranking);

        Set<Competitor> registeredCompetitors = new HashSet<>(Set.of(competitor1, competitor2, competitor3, competitor4));

        MJTOlympics mjtOlympics = new MJTOlympics(registeredCompetitors, competitionResultFetcher);

        Competition comp = new Competition("Running", "100km",
                Set.of(competitor1, competitor2, competitor3, competitor4));

        mjtOlympics.updateMedalStatistics(comp);

        assertEquals(1, competitor1.getMedals().size(), "First competitor should receive 1 medal.");
        assertEquals(1, competitor2.getMedals().size(), "Second competitor should receive 1 medal.");
        assertEquals(1, competitor3.getMedals().size(), "Third competitor should receive 1 medal.");
        assertEquals(0, competitor4.getMedals().size(), "Fourth competitor should not receive a medal.");
        assertTrue(competitor1.getMedals().contains(Medal.GOLD), "First competitor should receive GOLD.");
        assertTrue(competitor2.getMedals().contains(Medal.SILVER), "Second competitor should receive SILVER.");
        assertTrue(competitor3.getMedals().contains(Medal.BRONZE), "Third competitor should receive BRONZE.");
    }

    @Test
    void testUpdateMedalStatisticsThrowsForUnregisteredCompetitors() {
        Competitor competitor1 = new Athlete("1", "Ivan", "BG");
        Competitor competitor2 = new Athlete("2", "Georgi", "BG");
        Competition competition = new Competition("Running", "100m", Set.of(competitor1, competitor2));

        CompetitionResultFetcher competitionResultFetcher = mock();

        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(), competitionResultFetcher);

        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(competition),
                "UpdateMedalStatistics should throw if competitors are not registered for the olympics.");
    }

    @Test
    void testGetTotalMedalsForNotAddedNationality() {
        CompetitionResultFetcher competitionResultFetcher = mock();
        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(), competitionResultFetcher);

        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals("Not added."),
                "GetTotalMedals should throw if nationality is not in the olympics.");
    }

    @Test
    void testGetTotalMedalsForNullNationality() {
        CompetitionResultFetcher competitionResultFetcher = mock();
        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(), competitionResultFetcher);

        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.getTotalMedals(null),
                "GetTotalMedals should throw if nationality is not in the olympics.");
    }

    @Test
    void getTotalMedalsForValidNationalityWithDifferentMedals() {
        CompetitionResultFetcher competitionResultFetcher = mock();

        Competitor competitor1 = new Athlete("1", "Ivan", "BG");
        Competitor competitor2 = new Athlete("2", "Georgi", "BG");
        Competitor competitor3 = new Athlete("3", "Dimitar", "BG");

        Set<Competitor> competitors = Set.of(competitor1, competitor2, competitor3);

        Competition comp = new Competition("Weightlifting", "100kg", competitors);

        TreeSet<Competitor> ranking = new TreeSet<>(new IdentifierCompetitorComparator());
        ranking.addAll(competitors);
        when(competitionResultFetcher.getResult(comp)).thenReturn(ranking);

        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(competitors), competitionResultFetcher);

        mjtOlympics.updateMedalStatistics(comp);

        assertEquals(3, mjtOlympics.getTotalMedals("BG"),
                "GetTotalMedals should return all medals of the given nationality.");
    }

    @Test
    void getNationsRankListWithTwoMedalsForTheFirstNation() {
        Competitor competitor1 = new Athlete("1", "Carlos Nasar", "Bulgaria");
        Competitor competitor2 = new Athlete("2", "Georgi", "Bulgaria");
        Competitor competitor3 = new Athlete("3", "Hans", "Germany");
        Competitor competitor4 = new Athlete("4", "Josh", "UK");

        Set<Competitor> competitors = Set.of(competitor1, competitor2, competitor3, competitor4);

        Competition comp = new Competition("Weightlifting", "500kg",
                competitors);

        TreeSet<Competitor> ranking = new TreeSet<>(new IdentifierCompetitorComparator());
        ranking.addAll(competitors);

        CompetitionResultFetcher resultFetcher = mock();
        when(resultFetcher.getResult(comp)).thenReturn(ranking);

        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(competitors), resultFetcher);
        mjtOlympics.updateMedalStatistics(comp);

        TreeSet<String> rankList = mjtOlympics.getNationsRankList();
        assertEquals("Bulgaria", rankList.pollFirst(),
                "Nation with most medals should be first in the rank list.");
        assertEquals("Germany", rankList.pollFirst(),
                "Nation with second most medals should be second in the rank list.");
    }

    @Test
    void testUpdateMedalStatsWithNullCompetition() {
        CompetitionResultFetcher competitionResultFetcher = mock();
        MJTOlympics mjtOlympics = new MJTOlympics(new HashSet<>(), competitionResultFetcher);

        assertThrows(IllegalArgumentException.class, () -> mjtOlympics.updateMedalStatistics(null),
                "UpdateMedalStatistics should throw when competition is null.");
    }


}
