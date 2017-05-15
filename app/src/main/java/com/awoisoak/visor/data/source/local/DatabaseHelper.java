package com.awoisoak.visor.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.awoisoak.visor.data.source.Post;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME    = "posts.db";
    private static final int    DATABASE_VERSION = 1;
    private Dao<Post, String> mPostDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Post.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Post.class,true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the DAO
     * @return
     * @throws SQLException
     */
    public Dao<Post, String> getPostDao() throws SQLException {
        if (mPostDao == null) {
            mPostDao = getDao(Post.class);
        }
        return mPostDao;
    }

    /**
     * Clear cached DAOs and close the database
     */
    @Override
    public void close() {
        mPostDao = null;
        super.close();
    }

    /**
     * Delete all posts in the data base.
     */
    public void deleteAllPosts() throws SQLException {
        ConnectionSource connectionSource = getConnectionSource();
        TableUtils.dropTable(connectionSource, Post.class, true);
        TableUtils.createTableIfNotExists(connectionSource, Post.class);
    }

}

