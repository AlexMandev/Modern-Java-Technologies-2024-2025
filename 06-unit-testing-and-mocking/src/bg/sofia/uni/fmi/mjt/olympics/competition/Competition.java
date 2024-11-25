package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @throws IllegalArgumentException when creating a competition with null or blank name
 * @throws IllegalArgumentException when creating a competition with null or blank discipline
 * @throws IllegalArgumentException when creating a competition with null or empty competitors
 */
public record Competition(String name, String discipline, Set<Competitor> competitors) {
	public Competition {
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("Competition name cannot be null.");
		}
		if (discipline == null || discipline.isEmpty() || discipline.isBlank()) {
			throw new IllegalArgumentException("Competition discipline cannot be null.");
		}
		if (competitors == null || competitors.isEmpty()) {
			throw new IllegalArgumentException("Competition cannot have 0 competitors.");
		}
	}

	public Set<Competitor> competitors() {
		return Collections.unmodifiableSet(competitors);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Competition that = (Competition) o;
		return Objects.equals(name, that.name) && Objects.equals(discipline, that.discipline);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, discipline);
	}
}
