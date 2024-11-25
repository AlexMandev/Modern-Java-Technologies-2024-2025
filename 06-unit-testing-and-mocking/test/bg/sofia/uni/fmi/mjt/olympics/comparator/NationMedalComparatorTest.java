package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NationMedalComparatorTest {
    @Test
    void testCompareWithSameMedalCounts() {
        MJTOlympics mjtOlympics = mock();
        when(mjtOlympics.getTotalMedals("BG")).thenReturn(3);
        when(mjtOlympics.getTotalMedals("UK")).thenReturn(3);

        NationMedalComparator comparator = new NationMedalComparator(mjtOlympics);

        assertTrue(comparator.compare("BG", "UK") < 0,
                "Compare should a negative number when the first country is lexicographically before the second.");
    }

    @Test
    void testCompareWithSameNationality() {
        MJTOlympics mjtOlympics = mock();
        when(mjtOlympics.getTotalMedals("BG")).thenReturn(3);

        NationMedalComparator comparator = new NationMedalComparator(mjtOlympics);

        assertEquals(0, comparator.compare("BG", "BG"),
                "Compare should return 0 for identical nations.");
    }
}
