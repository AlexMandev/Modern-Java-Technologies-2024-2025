package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {
    private final String username;
    private final Set<Interest> interests;
    private final Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        this.interests = EnumSet.noneOf(Interest.class);
        this.friends = new LinkedHashSet<>();
    }

    /**
     * Returns the username of the user.
     * Each user is guaranteed to have a unique username.
     *
     * @return the username of the user
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Returns an unmodifiable view of the user's interests.
     *
     * @return an unmodifiable view of the user's interests
     */
    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(this.interests);
    }

    /**
     * Adds an interest to the user's profile.
     *
     * @param interest the interest to be added
     * @return true if the interest is newly added, false if the interest is already present
     * @throws IllegalArgumentException if the interest is null
     */
    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Null argument passed when adding interest");
        }
        return this.interests.add(interest);
    }

    /**
     * Removes an interest from the user's profile.
     *
     * @param interest the interest to be removed
     * @return true if the interest is removed, false if the interest is not present
     * @throws IllegalArgumentException if the interest is null
     */
    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Null argument passed when removing interest");
        }
        return this.interests.remove(interest);
    }

    /**
     * Return unmodifiable view of the user's friends.
     *
     * @return an unmodifiable view of the user's friends
     */
    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(this.friends);
    }

    /**
     * Adds a user to the user's friends.
     *
     * @param userProfile the user to be added as a friend
     * @return true if the user is added, false if the user is already a friend
     * @throws IllegalArgumentException if the user is trying to add themselves as a friend,
     *                                  or if the user is null
     */
    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Null argument given when adding a friend.");
        }
        if (userProfile == this) {
            throw new IllegalArgumentException("UserProfile cannot add themselves as a friend.");
        }

        if (this.friends.contains(userProfile)) {
            return false;
        }

        this.friends.add(userProfile);

        if (!userProfile.isFriend(this)) {
            userProfile.addFriend(this);
        }
        return true;
    }

    /**
     * Removes a user from the user's friends.
     *
     * @param userProfile the user to be removed
     * @return true if the user is removed, false if the user is not a friend
     * @throws IllegalArgumentException if the user is null
     */
    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Null argument given when removing a friend.");
        }

        if (!this.friends.contains(userProfile)) {
            return false;
        }

        this.friends.remove(userProfile);

        if (userProfile.isFriend(this)) {
            userProfile.unfriend(this);
        }

        return true;
    }

    /**
     * Checks if a user is already a friend.
     *
     * @param userProfile the user to be checked
     * @return true if the user is a friend, false if the user is not a friend
     * @throws IllegalArgumentException if the user is null
     */
    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Null argument given when checking for friends.");
        }
        return this.friends.contains(userProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultUserProfile other = (DefaultUserProfile) o;
        return this.username.equals(other.getUsername());
    }
}
