package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Comparator;

public class UserProfileFriendsCountComparator implements Comparator<UserProfile> {
    @Override
    public int compare(UserProfile o1, UserProfile o2) {
        return o2.getFriends().size() - o1.getFriends().size();
    }
}
