package cn.com.yqhome.instrumentapp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.com.yqhome.instrumentapp.Class.Ads;
import cn.com.yqhome.instrumentapp.Class.Forum;
import cn.com.yqhome.instrumentapp.Class.Learn;
import cn.com.yqhome.instrumentapp.Class.Live;
import cn.com.yqhome.instrumentapp.Class.News;

/**
 * Created by depengli on 2017/9/15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Instrument.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Forum.createForumSql());
        db.execSQL(Live.createLiveSql());
        db.execSQL(Ads.createAdsSql());
        db.execSQL(News.createNewsSql());
        db.execSQL(Learn.createLearnSql());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(Forum.createForumSql());
        db.execSQL("DROP TABLE IF EXISTS " + Forum.ForumSqlName);
        db.execSQL("DROP TABLE IF EXISTS " + Live.LiveSqlName);
        db.execSQL("DROP TABLE IF EXISTS " + Ads.AdsSqlName);
        db.execSQL("DROP TABLE IF EXISTS " + News.NewsSqlName);
        db.execSQL("DROP TABLE IF EXISTS " + Learn.LearnSqlName);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
