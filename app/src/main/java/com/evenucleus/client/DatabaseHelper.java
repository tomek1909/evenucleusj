package com.evenucleus.client;

/**
 * Created by tomeks on 2014-12-28.
 */

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper
{
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;
    private String _fullPath;
    public SQLiteDatabase _database;
    private ConnectionSource _connectionSource;

    // the DAO object we use to access the SimpleData table
    private Dao<Version, Integer> versionDao = null;
    public DatabaseHelper(String fullPath) {
        _fullPath = fullPath;
    }

    public void Initialize() throws java.sql.SQLException {
        Log.i(DatabaseHelper.class.getName(), String.format("Initialize %s", new String[]{_fullPath}));

        _database = SQLiteDatabase.openDatabase(_fullPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        _connectionSource = new AndroidConnectionSource(_database);
        boolean initialized = DoesTableExists(_database, "Version");
        if (!initialized)
            CreateInitial();
    }


    private boolean DoesTableExists(SQLiteDatabase database, String tablename)
    {
        long cnt = DatabaseUtils.queryNumEntries(database, "sqlite_master", "type='table' AND name=?", new String[] {tablename});
        return cnt > 0;
    }

    private void CreateInitial() throws java.sql.SQLException {
        TableUtils.createTable(_connectionSource, Version.class);
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Version, Integer> getDao() throws java.sql.SQLException {
        if (versionDao == null) {
            versionDao = DaoManager.createDao(_connectionSource, Version.class);
        }
        return versionDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    public void close() {
        _database.close();
        versionDao = null;
    }
}

