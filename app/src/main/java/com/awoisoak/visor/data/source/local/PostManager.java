package com.awoisoak.visor.data.source.local;

import com.awoisoak.visor.data.source.Post;

import java.sql.SQLException;
import java.util.List;

import dagger.internal.Preconditions;

/**
 * Created by awo on 14/05/17.
 */

public class PostManager {

    private static PostManager instance;
    private final PostDataStore mPostDataStore;
    
    /**
     * Gets or creates singleton instance.
     *
     * @return a valid PostManager singleton instance object
     */
    public static synchronized PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    /**
     * Private constructor.
     * It internally allocates the composite PostDataStore object.
     */
    private PostManager() {
        mPostDataStore = new PostDataStore();
    }


    /**
     * Add a post object to the DB
     *
     * @param p
     */
    public synchronized void addpost(Post p) {
        String postId = "";
        try {
            mPostDataStore.addPost(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Add several post objects to the DB at once
     *
     * @param posts
     */
    public synchronized void addposts(List<Post> posts) {
        try {
           mPostDataStore.addPosts(posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets all posts stored in the DB
     *
     * @throws Exception Other errors.
     */
    public synchronized List<Post> getAllposts()
            throws Exception {
        Preconditions.checkNotNull(mPostDataStore);
        try {
            return mPostDataStore.getAllPosts();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Remove all posts in the DB
     *
     * @throws SQLException
     */
    public synchronized void removeAllPosts()
            throws SQLException {
        Preconditions.checkNotNull(mPostDataStore);
        try {
            mPostDataStore.removeAllPosts();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

}
