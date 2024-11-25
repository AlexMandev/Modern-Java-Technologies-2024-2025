package bg.sofia.uni.fmi.mjt.socialnetwork;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfileFriendsCountComparator;

public class SocialNetworkImpl implements SocialNetwork {
    private final Set<UserProfile> registeredUsers;
    private final Set<Post> posts;

    public SocialNetworkImpl() {
        this.registeredUsers = new HashSet<>();
        this.posts = new HashSet<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null.");
        }
        if (!this.registeredUsers.add(userProfile)) {
            throw new UserRegistrationException("UserProfile " + userProfile.getUsername() + " is already registered.");
        }
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Collections.unmodifiableSet(this.registeredUsers);
    }

    @Override
    public Collection<Post> getPosts() {
        return Collections.unmodifiableSet(this.posts);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null.");
        }
        if (!registeredUsers.contains(userProfile)) {
            throw new UserRegistrationException("UserProfile is not registered in this network.");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty.");
        }

        Post newPost = new SocialFeedPost(userProfile, content);
        posts.add(newPost);
        return newPost;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> usersSortedByFriendsCount = new TreeSet<>(new UserProfileFriendsCountComparator());
        usersSortedByFriendsCount.addAll(this.registeredUsers);

        return usersSortedByFriendsCount;
    }

    private boolean haveCommonInterests(UserProfile userProfile1, UserProfile userProfile2) {
        Collection<Interest> interests1 = userProfile1.getInterests();
        Collection<Interest> interests2 = userProfile2.getInterests();

        for (Interest interest : interests1) {
            if (interests2.contains(interest)) {
                return true;
            }
        }
        return false;
    }

    private void findReachedUsers(UserProfile current, UserProfile author, Set<UserProfile> visited,
                                  Set<UserProfile> reachedUsers) {
        if (!visited.add(current)) {
            return;
        }

        if (current != author && haveCommonInterests(current, author)) {
            reachedUsers.add(current);
        }

        for (UserProfile friend : current.getFriends()) {
            if (!visited.contains(friend)) {
                findReachedUsers(friend, author, visited, reachedUsers);
            }
        }
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }

        UserProfile author = post.getAuthor();
        Set<UserProfile> reachedUsers = new HashSet<>();
        Set<UserProfile> visited = new HashSet<>();

        findReachedUsers(author, author, visited, reachedUsers);

        return reachedUsers;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
            throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("UserProfile cannot be null.");
        }
        if (!registeredUsers.contains(userProfile1) || !registeredUsers.contains(userProfile2)) {
            throw new UserRegistrationException("UserProfile is not registered in the Social Network.");
        }

        Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());

        return mutualFriends;
    }
}
