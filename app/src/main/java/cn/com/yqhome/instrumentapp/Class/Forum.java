package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.yqhome.instrumentapp.BaseUtils;

/**
 * Created by depengli on 2017/9/12.
 */

public class Forum implements Serializable{

    private static final String TAG = "Forum";

    public String id;
    public String title;
    public String brand;
    public String product;
    public String type;
    public String subType;
    public String[] tags;
    public String content;
    public Boolean topup;
    public String author;
    public String avator;
    public String avatorPath;
    public long   issueTime;
    public String duration;
    public String[] images;
    public String[] videos;
    public String comment;
    public int read;
    public int support;
    public int collect;


    public static final String ForumSqlName = "forum";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class ForumEntry implements BaseColumns {
        public static final String Forum_id = "itemid";
        public static final String Forum_TITLE = "title";
        public static final String Forum_PRODUCT = "product";
        public static final String Forum_TYPE = "type";
        public static final String Forum_SUBTYPE = "subType";
        public static final String Forum_TAG = "tags";
        public static final String Forum_CONTENT = "content";
        public static final String Forum_TOPUP = "topup";
        public static final String Forum_AUTHOR = "author";
        public static final String Forum_AVATOR = "avator";
        public static final String Forum_AVATORPATH = "avatorPath";
        public static final String Forum_ISSUE = "issueTime";
        public static final String Forum_DURATION = "duration";
        public static final String Forum_IMAGES = "images";
        public static final String Forum_VIDEOS = "videos";
        public static final String Forum_READ = "read";
        public static final String Forum_SUPPORT = "support";
        public static final String Forum_COLLECT = "collect";
    }

    public static String createForumSql(){
        return "CREATE TABLE IF NOT EXISTS " + ForumSqlName + " (" +
                ForumEntry.Forum_id + " TEXT PRIMARY KEY," +
                ForumEntry.Forum_TITLE + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_PRODUCT + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_TYPE + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_SUBTYPE + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_TAG + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_CONTENT + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_TOPUP + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_AUTHOR + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_AVATOR + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_AVATORPATH + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_ISSUE + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_DURATION + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_IMAGES + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_VIDEOS + TEXT_TYPE + COMMA_SEP +
                ForumEntry.Forum_READ + INTEGER_TYPE + COMMA_SEP +
                ForumEntry.Forum_SUPPORT + INTEGER_TYPE + COMMA_SEP +
                ForumEntry.Forum_COLLECT + INTEGER_TYPE +
                " )";

    }

    public static ContentValues createContentValue(Forum forum){
        ContentValues values = new ContentValues();
        values.put(ForumEntry.Forum_id, forum.id);
        values.put(ForumEntry.Forum_TITLE, forum.title);
        values.put(ForumEntry.Forum_PRODUCT, forum.product);
        values.put(ForumEntry.Forum_TYPE, forum.type);
        values.put(ForumEntry.Forum_SUBTYPE, forum.subType);
        values.put(ForumEntry.Forum_AUTHOR, forum.author);
        values.put(ForumEntry.Forum_AVATOR, forum.avator);
        values.put(ForumEntry.Forum_AVATORPATH, forum.avatorPath);
        String tagstring = "";
        for (int i = 0; i < forum.tags.length; i++) {
            if (tagstring==""){
                tagstring = forum.tags[i];
            }else {
                tagstring = tagstring +","+ forum.tags[i];
            }

        }
        values.put(ForumEntry.Forum_TAG, tagstring);
        values.put(ForumEntry.Forum_TOPUP, forum.topup.toString());
        String imagestring = "";
        for (int i = 0; i < forum.images.length ; i++) {
            if (imagestring==""){
                imagestring = forum.images[i];
            }else {
                imagestring = imagestring +","+ forum.images[i];
            }
        }
        values.put(ForumEntry.Forum_IMAGES, imagestring);

        String videostring = "";
        for (int i = 0; i < forum.videos.length ; i++) {
            if (videostring==""){
                videostring = forum.videos[i];
            }else {
                videostring = videostring +","+ forum.videos[i];
            }
        }
        values.put(ForumEntry.Forum_VIDEOS, videostring);

        if (forum.issueTime > 0){
            values.put(ForumEntry.Forum_ISSUE, forum.issueTime+"");
        }
        values.put(ForumEntry.Forum_COLLECT, forum.collect);
        values.put(ForumEntry.Forum_READ, forum.read);
        values.put(ForumEntry.Forum_SUPPORT, forum.support);

        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, Forum forum){
        String[] projection = {ForumEntry.Forum_id};
        String selection = ForumEntry.Forum_id + " = ?";
        String[] selectionArgs = { forum.id };
        Cursor c = db.query(
                ForumSqlName,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        int count = c.getCount();
        c.close();
        if (count > 0){
            return 0;
        }
        else{
            ContentValues values = createContentValue(forum);
            long insertvalue = db.insert(ForumSqlName,null,values);
            Log.i(TAG,insertvalue+"");
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, Forum forum){
        String[] projection = {ForumEntry.Forum_id};
        String selection = ForumEntry.Forum_id + " = ?";
        String[] selectionArgs = { forum.id };
        Cursor c = db.query(
                ForumSqlName,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        int count = c.getCount();
        c.close();
//        delete forum
        if (count > 0){
            db.delete(ForumSqlName,selection,selectionArgs);
        }

        return db.insert(ForumSqlName,null,createContentValue(forum));

    }

    public static List<Forum> getAllForumsFromDB(SQLiteDatabase db){
        String[] projection = {
                ForumEntry.Forum_id,
                ForumEntry.Forum_TITLE,
                ForumEntry.Forum_TYPE,
                ForumEntry.Forum_SUBTYPE,
                ForumEntry.Forum_AUTHOR,
                ForumEntry.Forum_AVATOR,
                ForumEntry.Forum_AVATORPATH,
                ForumEntry.Forum_CONTENT,
                ForumEntry.Forum_TOPUP,
                ForumEntry.Forum_DURATION,
                ForumEntry.Forum_IMAGES,
                ForumEntry.Forum_VIDEOS,
                ForumEntry.Forum_ISSUE,
                ForumEntry.Forum_COLLECT,
                ForumEntry.Forum_READ,
                ForumEntry.Forum_TAG,
                ForumEntry.Forum_SUPPORT
        };

// Filter results WHERE "title" = 'My Title'


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ForumEntry.Forum_ISSUE + " DESC";

        Cursor c = db.query(
                ForumSqlName,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Forum> list = new ArrayList<Forum>();

        if (c != null && c.moveToFirst()){
            do{
                Forum forum = new Forum();
                //id
                int idindex = c.getColumnIndex(ForumEntry.Forum_id);
                forum.id = c.getString(idindex);
                //title
                int titleindex = c.getColumnIndex(ForumEntry.Forum_TITLE);
                forum.title = c.getString(titleindex);
                //type
                int typeindex = c.getColumnIndex(ForumEntry.Forum_TYPE);
                forum.type = c.getString(typeindex);
//            ForumEntry.Forum_SUBTYPE
                int subtypeindex = c.getColumnIndex(ForumEntry.Forum_SUBTYPE);
                forum.subType = c.getString(subtypeindex);
//            ForumEntry.Forum_CONTENT
                int contentindex = c.getColumnIndex(ForumEntry.Forum_CONTENT);
                forum.content = c.getString(contentindex);
//            ForumEntry.Forum_TAG
                int tagindex = c.getColumnIndex(ForumEntry.Forum_TAG);
                forum.tags = c.getString(tagindex).split(",");
//            ForumEntry.Forum_CONTENT
                int topupindex = c.getColumnIndex(ForumEntry.Forum_TOPUP);
                forum.topup = c.getString(topupindex).contains("1");
//            Forum_AUTHOR
                int authorindex = c.getColumnIndex(ForumEntry.Forum_AUTHOR);
                forum.author = c.getString(authorindex);
//            Forum_AVATOR
                int avatorindex = c.getColumnIndex(ForumEntry.Forum_AVATOR);
                forum.avator = c.getString(avatorindex);
//            Forum_AVATORPATH
                int avatorpathindex = c.getColumnIndex(ForumEntry.Forum_AVATORPATH);
                forum.avatorPath = c.getString(avatorpathindex);
//             Forum_COLLECT
                int collectindex = c.getColumnIndex(ForumEntry.Forum_COLLECT);
                if (c.getString(collectindex)==null){
                    forum.collect = 0;
                }else{
                    forum.collect = c.getInt(collectindex) ;
                }

//           Forum_DURATION
                int durationindex = c.getColumnIndex(ForumEntry.Forum_DURATION);
                forum.duration = c.getString(durationindex) ;
//            Forum_IMAGES
                int imageindex = c.getColumnIndex(ForumEntry.Forum_IMAGES);
                forum.images = c.getString(imageindex).split(",") ;

                int videoindex = c.getColumnIndex(ForumEntry.Forum_VIDEOS);
                forum.videos = c.getString(videoindex).split(",") ;
//            Forum_ISSUE
                int issueindex = c.getColumnIndex(ForumEntry.Forum_ISSUE);
                forum.issueTime = c.getLong(issueindex);
//            Forum_Read
                int readindex = c.getColumnIndex(ForumEntry.Forum_READ);
                forum.read = c.getInt(readindex) ;
//            Forum_support
                int supportindex = c.getColumnIndex(ForumEntry.Forum_SUPPORT);

                forum.support = c.getInt(supportindex) ;

                list.add(forum);
            }while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public static List<Forum> getAllForumsByType(SQLiteDatabase db,String type){
        String[] projection = {
                ForumEntry.Forum_id,
                ForumEntry.Forum_TITLE,
                ForumEntry.Forum_TYPE,
                ForumEntry.Forum_SUBTYPE,
                ForumEntry.Forum_AUTHOR,
                ForumEntry.Forum_AVATOR,
                ForumEntry.Forum_AVATORPATH,
                ForumEntry.Forum_CONTENT,
                ForumEntry.Forum_TOPUP,
                ForumEntry.Forum_DURATION,
                ForumEntry.Forum_IMAGES,
                ForumEntry.Forum_VIDEOS,
                ForumEntry.Forum_ISSUE,
                ForumEntry.Forum_COLLECT,
                ForumEntry.Forum_READ,
                ForumEntry.Forum_TAG,
                ForumEntry.Forum_SUPPORT
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder =
                ForumEntry.Forum_ISSUE + " DESC";

        String selection = ForumEntry.Forum_SUBTYPE + " = ?";
        String[] selectionArgs = { type };
        if (type=="其他") {
            selection = ForumEntry.Forum_SUBTYPE + " is Null or (";
            for (int i = 0; i < BaseUtils.TYPE_INSTRUMENT.length; i++) {
                if (i == 0){
                    selection = selection + ForumEntry.Forum_SUBTYPE + " != ? " ;
                }else{
                    selection = selection + " and "+ ForumEntry.Forum_SUBTYPE + " != ? ";
                }
            }
            selection = selection +" )";

            selectionArgs = BaseUtils.TYPE_INSTRUMENT;
        }
        Cursor c = db.query(
                ForumSqlName,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Forum> list = new ArrayList<Forum>();

        if (c != null && c.moveToFirst()){
            do{
                Forum forum = new Forum();
                //id
                int idindex = c.getColumnIndex(ForumEntry.Forum_id);
                forum.id = c.getString(idindex);
                //title
                int titleindex = c.getColumnIndex(ForumEntry.Forum_TITLE);
                forum.title = c.getString(titleindex);
                //type
                int typeindex = c.getColumnIndex(ForumEntry.Forum_TYPE);
                forum.type = c.getString(typeindex);
//            ForumEntry.Forum_SUBTYPE
                int subtypeindex = c.getColumnIndex(ForumEntry.Forum_SUBTYPE);
                forum.subType = c.getString(subtypeindex);
//            ForumEntry.Forum_CONTENT
                int contentindex = c.getColumnIndex(ForumEntry.Forum_CONTENT);
                forum.content = c.getString(contentindex);
//            ForumEntry.Forum_TAG
                int tagindex = c.getColumnIndex(ForumEntry.Forum_TAG);
                forum.tags = c.getString(tagindex).split(",");
//            ForumEntry.Forum_CONTENT
                int topupindex = c.getColumnIndex(ForumEntry.Forum_TOPUP);
                if (topupindex>=0){
                    forum.topup = c.getString(topupindex).contains("1");
                }

//            Forum_AUTHOR
                int authorindex = c.getColumnIndex(ForumEntry.Forum_AUTHOR);
                forum.author = c.getString(authorindex);
//            Forum_AVATOR
                int avatorindex = c.getColumnIndex(ForumEntry.Forum_AVATOR);
                forum.avator = c.getString(avatorindex);
//            Forum_AVATORPATH
                int avatorpathindex = c.getColumnIndex(ForumEntry.Forum_AVATORPATH);
                forum.avatorPath = c.getString(avatorpathindex);
//             Forum_COLLECT
                int collectindex = c.getColumnIndex(ForumEntry.Forum_COLLECT);
                if (c.getString(collectindex)==null){
                    forum.collect = 0;
                }else{
                    forum.collect = c.getInt(collectindex) ;
                }

//           Forum_DURATION
                int durationindex = c.getColumnIndex(ForumEntry.Forum_DURATION);
                forum.duration = c.getString(durationindex) ;
//            Forum_IMAGES
                int imageindex = c.getColumnIndex(ForumEntry.Forum_IMAGES);
                forum.images = c.getString(imageindex).split(",") ;

                int videoindex = c.getColumnIndex(ForumEntry.Forum_VIDEOS);
                forum.videos = c.getString(videoindex).split(",") ;
//            Forum_ISSUE
                int issueindex = c.getColumnIndex(ForumEntry.Forum_ISSUE);
                forum.issueTime = c.getLong(issueindex);
//            Forum_Read
                int readindex = c.getColumnIndex(ForumEntry.Forum_READ);
                forum.read = c.getInt(readindex) ;
//            Forum_support
                int supportindex = c.getColumnIndex(ForumEntry.Forum_SUPPORT);

                forum.support = c.getInt(supportindex) ;

                list.add(forum);
            }while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public Forum(JSONObject objectForum) {
        try{
            this.id = objectForum.getString("_id");
            this.title = objectForum.getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.brand = objectForum.getString("brand");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.product = objectForum.getString("product");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.type = objectForum.getString("type");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.subType = objectForum.getString("subType");
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String[] tmplist = new String[objectForum.getJSONArray("tags").length()];
            for (int i = 0; i < objectForum.getJSONArray("tags").length(); i++) {
                tmplist[i] = objectForum.getJSONArray("tags").getString(i);
            }
            this.tags = tmplist;

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.content = objectForum.getString("content");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.topup = objectForum.getBoolean("topup");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.author = objectForum.getString("author");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avator = objectForum.getString("avator");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avatorPath = objectForum.getString("avatorPath");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String issuestring = objectForum.getString("issueTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);
            this.issueTime = dt.getTime();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.duration =  objectForum.getString("duration");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String[] tmplist = new String[objectForum.getJSONArray("images").length()];
            for (int i = 0; i < objectForum.getJSONArray("images").length(); i++) {
                tmplist[i] = objectForum.getJSONArray("images").getString(i);
            }
            this.images =  tmplist;
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String[] tmpvideolist = new String[objectForum.getJSONArray("videos").length()];
            for (int i = 0; i < objectForum.getJSONArray("videos").length(); i++) {
                tmpvideolist[i] = objectForum.getJSONArray("videos").getString(i);
            }
            this.videos =  tmpvideolist;
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.read =  objectForum.getInt("read");
            this.support =  objectForum.getInt("support");
            this.collect =  objectForum.getInt("collect");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Forum(){

    }
}
