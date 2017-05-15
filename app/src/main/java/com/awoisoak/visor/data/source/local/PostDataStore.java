package com.awoisoak.visor.data.source.local;

import android.content.Context;

import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.presentation.VisorApplication;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class PostDataStore {
    private static final String MARKER = PostDataStore.class.getCanonicalName();
    private static final Context sContext = VisorApplication.getVisorApplication();
    private final DatabaseHelper mDatabaseHelper;
    Dao<Post, Integer> mPostDao = null;


    /**
     * Private constructor.
     */
    public PostDataStore() {
        mDatabaseHelper = new DatabaseHelper(sContext);
    }

    public void addPost(Post p) throws Exception {
        try {
            Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
             postDao.createIfNotExists(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPosts(final List<Post> posts) throws Exception {
        try {
            final Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
            postDao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Post p: posts){
                        postDao.createIfNotExists(p);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Retrieve all Posts
     *
     * @return Posts List, empty list if no Post was found
     * @throws UnknownError
     */
    public List<Post> getAllPosts()
            throws Exception {
        try {
            Dao<Post, String> PostDao = mDatabaseHelper.getPostDao();
            return PostDao.queryForAll();
        } catch (SQLException e) {
            throw new Exception("Error retrieving all the Posts in the DB", e);
        }
    }

    /**
     * Remove all posts
     *
     * @throws SQLException
     */
    public void removeAllPosts()
            throws SQLException {
        mDatabaseHelper.deleteAllPosts();
    }
}
