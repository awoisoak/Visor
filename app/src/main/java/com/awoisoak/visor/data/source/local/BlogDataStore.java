package com.awoisoak.visor.data.source.local;

import android.content.Context;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.Post;
import com.awoisoak.visor.data.source.WPAPI;
import com.awoisoak.visor.presentation.VisorApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

//TODO Create an BLogDataStoreException to be the only one throw up by methods in this class
public class BlogDataStore {
    private static final String MARKER = BlogDataStore.class.getCanonicalName();
    private static final Context sContext = VisorApplication.getVisorApplication();
    private final DatabaseHelper mDatabaseHelper;
    Dao<Post, Integer> mPostDao = null;

    /**
     * Private constructor.
     */
    public BlogDataStore() {
        mDatabaseHelper = new DatabaseHelper(sContext);
    }

    /**
     * Add Post to the DB
     *
     * @param p
     * @throws Exception
     */
    public void addPost(Post p) throws Exception {
        try {
            Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
            postDao.createIfNotExists(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a list of posts to the DB
     *
     * @param posts
     * @throws Exception
     */
    public void addPosts(final List<Post> posts) throws Exception {
        try {
            final Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
            postDao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Post p : posts) {
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
            Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
            QueryBuilder<Post, String> queryBuilder = postDao.queryBuilder();
            return queryBuilder.orderBy(Post.CREATION_DATE, false).query();
        } catch (SQLException e) {
            throw new Exception("Error retrieving all the Posts in the DB", e);
        }
    }


    /**
     * Retrieve a list of posts given an offset
     *
     * @return Posts List, empty list if no Post was found
     * @throws UnknownError
     */
    public List<Post> getPosts(int offset)
            throws Exception {
        try {
            Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
            QueryBuilder<Post, String> queryBuilder = postDao.queryBuilder();
            queryBuilder.orderBy(Post.CREATION_DATE,false).offset((long) offset).limit(
                    (long) WPAPI.MAX_NUMBER_POSTS_RETURNED);
            return postDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            throw new Exception("Error retrieving posts in the DB from offset = " + offset, e);
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


    public void addImage(Image image) throws Exception {
        try {
            Dao<Image, String> ImageDao = mDatabaseHelper.getImageDao();
            ImageDao.createIfNotExists(image);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addImages(final List<Image> Images) throws Exception {
        try {
            final Dao<Image, String> ImageDao = mDatabaseHelper.getImageDao();
            ImageDao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Image p : Images) {
                        ImageDao.createIfNotExists(p);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieve all Images
     *
     * @return Images List, empty list if no Image was found
     * @throws Exception
     */
    public List<Image> getAllImages()
            throws Exception {
        try {
            Dao<Image, String> ImageDao = mDatabaseHelper.getImageDao();
            return ImageDao.queryForAll();
        } catch (SQLException e) {
            throw new Exception("Error retrieving all the Images in the DB", e);
        }
    }

    /**
     * Gets Images from a specific post
     *
     * @param postId
     * @return
     * @throws Exception
     */
    public List<Image> getAllImagesFromPost(String postId)
            throws Exception {
        try {
            Dao<Image, String> ImageDao = mDatabaseHelper.getImageDao();
            return ImageDao.queryForEq(Image.POST_ID, postId);
        } catch (SQLException e) {
            throw new Exception("Error retrieving all the Images in the DB", e);
        }
    }


    /**
     * Gets Images from a specific post given an offset
     *
     * @param postId
     * @param offset
     * @return
     * @throws Exception
     */

    public List<Image> getImagesFromPost(String postId, int offset) throws Exception {
        try {
            List<Image> allImages = getAllImagesFromPost(postId);
            if (allImages.size() >= offset + WPAPI.MAX_NUMBER_IMAGES_RETURNED) {
                return allImages.subList(offset, (offset + WPAPI.MAX_NUMBER_IMAGES_RETURNED));
            } else {
                return allImages.subList(offset, allImages.size());
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving Images for postId" + postId + "in the DB with offset=" + offset, e);
        }
    }


    /**
     * Remove all Images
     *
     * @throws SQLException
     */
    public void removeAllImages()
            throws SQLException {
        mDatabaseHelper.deleteAllImages();
    }


    /**
     * Remove all Tables
     *
     * @throws SQLException
     */
    public void removeAllDB()
            throws SQLException {
        mDatabaseHelper.deleteAllTables();
    }


    /**
     * Retrieve the last post entry saved in the DB
     *
     * @return Posts List, empty list if no Post was found
     * @throws UnknownError
     */
    public Post getLastPost() throws Exception {
        Dao<Post, String> postDao = mDatabaseHelper.getPostDao();
        QueryBuilder<Post, String> queryBuilder = postDao.queryBuilder();
        return queryBuilder.orderBy(Post.CREATION_DATE, false).query().get(0);
    }


}
