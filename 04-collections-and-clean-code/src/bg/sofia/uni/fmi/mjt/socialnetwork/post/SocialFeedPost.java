package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import java.util.Collections;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

public class SocialFeedPost implements Post {
    private static final String POST_ID_PREFIX = "post-";
    private static int postIdCounter;

    private final String uniqueId;
    private final UserProfile author;
    private final LocalDateTime publishedOn;
    private final String content;
    private final Map<ReactionType, Set<UserProfile>> reactions;

    static {
        postIdCounter = 0;
    }

    public SocialFeedPost(UserProfile author, String content) {
        this.author = author;
        this.publishedOn = LocalDateTime.now();
        this.content = content;
        this.uniqueId = POST_ID_PREFIX + ++postIdCounter;
        this.reactions = new HashMap<>();
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public UserProfile getAuthor() {
        return this.author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null");
        }

        Set<UserProfile> usersReactedWithReactionType = this.reactions.get(reactionType);

        if (usersReactedWithReactionType != null && usersReactedWithReactionType.contains(userProfile)) {
            return false;
        }
        boolean addedNewReaction = true;
        for (Map.Entry<ReactionType, Set<UserProfile>> entry : this.reactions.entrySet()) {
            if (entry.getKey() != reactionType && entry.getValue().contains(userProfile)) {
                entry.getValue().remove(userProfile);
                addedNewReaction = false;
                if (entry.getValue().isEmpty()) {
                    this.reactions.remove(entry.getKey());
                }
                break;
            }
        }
        reactions.putIfAbsent(reactionType, new HashSet<>());
        reactions.get(reactionType).add(userProfile);
        return addedNewReaction;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("UserProfile cannot be null.");
        }

        boolean removedReaction = false;

        for (Map.Entry<ReactionType, Set<UserProfile>> entries : reactions.entrySet()) {
            if (entries.getValue().contains(userProfile)) {
                entries.getValue().remove(userProfile);
                removedReaction = true;
                if (entries.getValue().isEmpty()) {
                    this.reactions.remove(entries.getKey());
                }
            }
        }

        return removedReaction;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        return Collections.unmodifiableMap(this.reactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("ReactionType cannot be null.");
        }

        return this.reactions.getOrDefault(reactionType, Collections.emptySet()).size();
    }

    @Override
    public int totalReactionsCount() {
        int totalReactions = 0;

        for (Set<UserProfile> usersReacted : reactions.values()) {
            totalReactions += usersReacted.size();
        }

        return totalReactions;
    }
}
