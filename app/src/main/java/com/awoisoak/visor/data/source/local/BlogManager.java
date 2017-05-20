package com.awoisoak.visor.data.source.local;

import com.awoisoak.visor.data.source.Image;
import com.awoisoak.visor.data.source.Post;

import java.sql.SQLException;
import java.util.List;

import dagger.internal.Preconditions;

/**
 * Manages the blog database which is made of two tables: Posts & Images
 */

public class BlogManager {

    private static BlogManager instance;
    private final BlogDataStore mBlogDataStore;

    /**
     * Gets or creates singleton instance.
     *
     * @return a valid PostManager singleton instance object
     */
    public static synchronized BlogManager getInstance() {
        if (instance == null) {
            instance = new BlogManager();
        }
        return instance;
    }

    /**
     * Private constructor.
     * It internally allocates the composite PostDataStore object.
     */
    private BlogManager() {
        mBlogDataStore = new BlogDataStore();
    }


    /**
     * Add a post object to the DB
     *
     * @param p
     */
    public synchronized void addpost(Post p) {
        String postId = "";
        try {
            mBlogDataStore.addPost(p);
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
            mBlogDataStore.addPosts(posts);
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
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            return mBlogDataStore.getAllPosts();
        } catch (Exception e) {
            throw new Exception(e);
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
            return mBlogDataStore.getPosts(offset);
        } catch (SQLException e) {
            throw new Exception("Error retrieving posts in the DB from offset = "+offset, e);
        }
    }

    /**
     * Remove all posts in the DB
     *
     * @throws SQLException
     */
    public synchronized void removeAllPosts()
            throws SQLException {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            mBlogDataStore.removeAllPosts();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Add a Image object to the DB
     *
     * @param image
     */
    public synchronized void addImage(Image image) {
        String ImageId = "";
        try {
            mBlogDataStore.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Add several Image objects to the DB at once
     *
     * @param Images
     */
    public synchronized void addImages(List<Image> Images) {
        try {
            mBlogDataStore.addImages(Images);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets all Images stored in the DB
     *
     * @throws Exception Other errors.
     */
    public synchronized List<Image> getAllImages()
            throws Exception {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            return mBlogDataStore.getAllImages();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Gets all images from a specific post
     *
     * @param postId
     * @return
     * @throws Exception
     */
    public synchronized List<Image> getAllImagesFromPost(String postId)
            throws Exception {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            return mBlogDataStore.getAllImagesFromPost(postId);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Gets images from a specific post given an offset
     *
     * @param postId
     * @return
     * @throws Exception
     */
    public synchronized List<Image> getImagesFromPost(String postId, int offset)
            throws Exception {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            return mBlogDataStore.getImagesFromPost(postId, offset);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    /**
     * Remove all Images in the DB
     *
     * @throws SQLException
     */
    public synchronized void removeAllImages()
            throws SQLException {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            mBlogDataStore.removeAllImages();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Remove all tables in the DB
     */
    public synchronized void removeAllDB() throws SQLException {
        Preconditions.checkNotNull(mBlogDataStore);
        try {
            mBlogDataStore.removeAllDB();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }


    /**
     * Retrieve the last post entry saved in the DB
     *
     * @return Posts List, empty list if no Post was found
     * @throws UnknownError
     */
    public Post getLastPost() throws Exception {
        try {
            return mBlogDataStore.getLastPost();
        } catch (SQLException e) {
            throw new Exception("Error retrieving the last post entry from the DB ", e);
        }
    }

}
