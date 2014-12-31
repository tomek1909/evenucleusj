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
    private Dao<KeyInfo, Integer> keyInfoDao = null;
    private Dao<Pilot, Integer> pilotDao = null;
    private Dao<Skill, Integer> skillDao = null;
    private Dao<PendingNotification, Integer> pendingNotificationDao = null;
    private Dao<Corporation, Integer> corporationDao = null;
    private Dao<Job, Integer> jobDao = null;
    private Dao<Category, Integer> categoryDao = null;
    private Dao<CacheEntry, String> cacheEntryDao = null;


    public DatabaseHelper(String fullPath) {
        _fullPath = fullPath;
    }

    public void Initialize() throws java.sql.SQLException {
        Log.i(DatabaseHelper.class.getName(), String.format("Initialize %s", _fullPath));

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
        TableUtils.createTable(_connectionSource, KeyInfo.class);
        TableUtils.createTable(_connectionSource, Pilot.class);
        TableUtils.createTable(_connectionSource, Corporation.class);
        TableUtils.createTable(_connectionSource, Skill.class);
        TableUtils.createTable(_connectionSource, PendingNotification.class);
        TableUtils.createTable(_connectionSource, Job.class);
        TableUtils.createTable(_connectionSource, Category.class);
        TableUtils.createTable(_connectionSource, CacheEntry.class);
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Version, Integer> getVersionDao() throws java.sql.SQLException {
        if (versionDao == null) {
            versionDao = DaoManager.createDao(_connectionSource, Version.class);
        }
        return versionDao;
    }

    public Dao<KeyInfo, Integer> getKeyInfoDao() throws java.sql.SQLException {
        if (keyInfoDao == null) {
            keyInfoDao = DaoManager.createDao(_connectionSource, KeyInfo.class);
        }
        return keyInfoDao;
    }

    public Dao<Pilot, Integer> getPilotDao() throws java.sql.SQLException {
        if (pilotDao == null) {
            pilotDao = DaoManager.createDao(_connectionSource, Pilot.class);
        }
        return pilotDao;
    }

    public Dao<Skill, Integer> getSkillDao() throws java.sql.SQLException {
        if (skillDao == null) {
            skillDao = DaoManager.createDao(_connectionSource, Skill.class);
        }
        return skillDao;
    }

    public Dao<PendingNotification, Integer> getPendingNotificationDao() throws java.sql.SQLException {
        if (pendingNotificationDao == null) {
            pendingNotificationDao = DaoManager.createDao(_connectionSource, PendingNotification.class);
        }
        return pendingNotificationDao;
    }

    public Dao<Corporation, Integer> getCorporationDao() throws java.sql.SQLException {
        if (corporationDao == null) {
            corporationDao = DaoManager.createDao(_connectionSource, Corporation.class);
        }
        return corporationDao;
    }

    public Dao<Job, Integer> getJobDao() throws java.sql.SQLException {
        if (jobDao == null) {
            jobDao = DaoManager.createDao(_connectionSource, Job.class);
        }
        return jobDao;
    }

    public Dao<Category, Integer> getCategoryDao() throws java.sql.SQLException {
        if (categoryDao == null) {
            categoryDao = DaoManager.createDao(_connectionSource, Category.class);
        }
        return categoryDao;
    }

    public Dao<CacheEntry, String> getCacheEntryDao() throws java.sql.SQLException {
        if (cacheEntryDao == null) {
            cacheEntryDao = DaoManager.createDao(_connectionSource, CacheEntry.class);
        }
        return cacheEntryDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    public void close() {
        _database.close();
        versionDao = null;
    }
}

