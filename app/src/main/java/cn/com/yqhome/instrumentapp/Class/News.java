package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by depengli on 2017/9/12.
 */

public class News implements Serializable {
    public String id;
    public String title;

    public String brand;
    public String product;
    public String type;
    public String subType;
    public boolean topup;

    public String[] tags;
    public String content;
    public String author;
    public String avator;
    public String avatorPath;

    public long issueTime;
    public String fromTime;
    public String[] images;

    public String[] videos;
    public int comment;
    public int read;
    public int collect;
    public int support;

    public static final String NewsSqlName = "news";
    private static final String TEXT_TYPE = " TEXT NULL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class NewsEntry implements BaseColumns {
        public static final String News_ID = "itemid";
        public static final String News_TITLE = "title";
        public static final String News_BRAND = "brand";
//        brand product type subType tags content author avator avatorPath
        public static final String News_PRODUCT = "product";
        public static final String News_TYPE = "type";
        public static final String News_SUBTYPE = "subType";
        public static final String News_TAG = "tags";
        public static final String News_CONTENT = "content";

        public static final String News_TOPUP = "topup";
        public static final String News_AUTHOR = "author";
        public static final String News_AVATOR = "avator";
        public static final String News_AVATORPATH = "avatorPath";
//        issueTime fromTime images videos comment read collect support
        public static final String News_ISSUE = "issueTime";
        public static final String News_FROMTIME= "fromTime";
        public static final String News_IMAGES = "images";
        public static final String News_VIDEOS = "videos";
        public static final String News_COMMENT = "comment";
        public static final String News_READ = "read";
        public static final String News_SUPPORT = "support";
        public static final String News_COLLECT = "collect";
    }

    public static String createNewsSql(){
        return "CREATE TABLE IF NOT EXISTS " + NewsSqlName + " (" +
                NewsEntry.News_ID + " TEXT PRIMARY KEY," +
                NewsEntry.News_TITLE + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_BRAND + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_TYPE + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_PRODUCT + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_SUBTYPE + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_TAG + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_CONTENT + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_TOPUP + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_AUTHOR + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_AVATOR + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_AVATORPATH + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_ISSUE + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_FROMTIME + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_IMAGES + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_VIDEOS + TEXT_TYPE + COMMA_SEP +
                NewsEntry.News_READ + INTEGER_TYPE + COMMA_SEP +
                NewsEntry.News_COMMENT + INTEGER_TYPE + COMMA_SEP +
                NewsEntry.News_SUPPORT + INTEGER_TYPE + COMMA_SEP +
                NewsEntry.News_COLLECT + INTEGER_TYPE +
                " )";

    }

    public static ContentValues createContentValue(News news){
        ContentValues values = new ContentValues();
        values.put(NewsEntry.News_ID, news.id);
        values.put(NewsEntry.News_TITLE, news.title);
        values.put(NewsEntry.News_BRAND, news.brand);
        if (news.type != null){
            values.put(NewsEntry.News_TYPE, news.type);
        }else{
            values.put(NewsEntry.News_TYPE, "");
        }
        if (news.product!= null){
            values.put(NewsEntry.News_PRODUCT, news.product);
        }
        else{
            values.put(NewsEntry.News_PRODUCT, "");
        }
        if (news.subType != null){
            values.put(NewsEntry.News_SUBTYPE, news.subType);
        }else{
            values.put(NewsEntry.News_SUBTYPE, "");
        }
        String tagstring = "";
        for (int i = 0; i < news.tags.length; i++) {
            if (tagstring==""){
                tagstring = news.tags[i];
            }else {
                tagstring = tagstring +","+ news.tags[i];
            }

        }
        values.put(NewsEntry.News_TAG, tagstring);
        values.put(NewsEntry.News_CONTENT,news.content);
        values.put(NewsEntry.News_TOPUP, news.topup);

        values.put(NewsEntry.News_AUTHOR, news.author);
        values.put(NewsEntry.News_AVATOR, news.avator);
        values.put(NewsEntry.News_AVATORPATH, news.avatorPath);
        if (news.issueTime > 0){
            values.put(NewsEntry.News_ISSUE, news.issueTime);
        }
        values.put(NewsEntry.News_FROMTIME, news.fromTime);

        String imagestring = "";
        for (int i = 0; i < news.images.length ; i++) {
            if (imagestring==""){
                imagestring = news.images[i];
            }else {
                imagestring = imagestring +","+ news.images[i];
            }
        }
        if (imagestring != ""){
            values.put(NewsEntry.News_IMAGES, imagestring);
        }

        String videostring = "";
        for (int i = 0; i < news.videos.length ; i++) {
            if (videostring==""){
                videostring = news.videos[i];
            }else {
                videostring = videostring +","+ news.videos[i];
            }
        }
        if (videostring != ""){
            values.put(NewsEntry.News_VIDEOS, videostring);
        }

        values.put(NewsEntry.News_COMMENT, news.comment);
        values.put(NewsEntry.News_COLLECT, news.collect);
        values.put(NewsEntry.News_READ, news.read);
        values.put(NewsEntry.News_SUPPORT, news.support);

        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, News news){
        String[] projection = {NewsEntry.News_ID};
        String selection = NewsEntry.News_ID + " = ? ";
        String[] selectionArgs = { news.id };
        Cursor c = db.query(
                NewsSqlName,                     // The table to query
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
            ContentValues values = createContentValue(news);
            long insertvalue = db.insert(NewsSqlName,null,values);
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, News news){
        String[] projection = {NewsEntry.News_ID};
        String selection = NewsEntry.News_ID + " = ?";
        String[] selectionArgs = { news.id };
        Cursor c = db.query(
                NewsSqlName,                     // The table to query
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
            db.delete(NewsSqlName,selection,selectionArgs);
        }

        return db.insert(NewsSqlName,null,createContentValue(news));

    }

    public static List<News> getAllNewsFromDB(SQLiteDatabase db){
        String[] projection = {
                NewsEntry.News_ID,
                NewsEntry.News_TITLE,
                NewsEntry.News_BRAND,
                NewsEntry.News_PRODUCT,
                NewsEntry.News_TOPUP,

                NewsEntry.News_IMAGES,
                NewsEntry.News_VIDEOS,
                NewsEntry.News_ISSUE,
                NewsEntry.News_AUTHOR,
                NewsEntry.News_AVATOR,
                NewsEntry.News_AVATORPATH,
                NewsEntry.News_CONTENT,
                NewsEntry.News_FROMTIME,
                NewsEntry.News_ISSUE,
                NewsEntry.News_READ,
                NewsEntry.News_COMMENT,
                NewsEntry.News_COLLECT,
                NewsEntry.News_SUPPORT
        };
// How you want the results sorted in the resulting Cursor
        String sortOrder =
                NewsEntry.News_ISSUE + " DESC";

        Cursor c = db.query(
                NewsSqlName,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<News> list = new ArrayList<News>();

        if (c.getCount()>0){
            c.moveToFirst();
            do{
                News news = new News();
                //id
                int idindex = c.getColumnIndex(NewsEntry.News_ID);
                news.id = c.getString(idindex);
                //title
                int titleindex = c.getColumnIndex(NewsEntry.News_TITLE);
                news.title = c.getString(titleindex);
                //type
                int typeindex = c.getColumnIndex(NewsEntry.News_TYPE);
                news.type = c.getString(typeindex);
//            SUBTYPE
                int subtypeindex = c.getColumnIndex(NewsEntry.News_SUBTYPE);
                news.subType = c.getString(subtypeindex);
//            CONTENT
                int contentindex = c.getColumnIndex(NewsEntry.News_CONTENT);
                news.content = c.getString(contentindex);
//            TAG
                int tagindex = c.getColumnIndex(NewsEntry.News_TAG);
                if (c.getString(tagindex) != null && c.getString(tagindex) != ""){
                    news.tags = c.getString(tagindex).split(",");
                }
                else{
                    news.tags = null;
                }
//            topup
                int topupindex = c.getColumnIndex(NewsEntry.News_TOPUP);
                news.topup = c.getString(topupindex).contains("1");
//            AUTHOR
                int authorindex = c.getColumnIndex(NewsEntry.News_AUTHOR);
                news.author = c.getString(authorindex);
//            AVATOR
                int avatorindex = c.getColumnIndex(NewsEntry.News_AVATOR);
                news.avator = c.getString(avatorindex);
//            AVATORPATH
                int avatorpathindex = c.getColumnIndex(NewsEntry.News_AVATORPATH);
                news.avatorPath = c.getString(avatorpathindex);
//             COLLECT
                int collectindex = c.getColumnIndex(NewsEntry.News_COLLECT);
                if (c.getString(collectindex)==null){
                    news.collect = 0;
                }else{
                    news.collect = c.getInt(collectindex) ;
                }

//            IMAGES
                int imageindex = c.getColumnIndex(NewsEntry.News_IMAGES);
                if (c.getString(imageindex) != null && c.getString(imageindex) != "" ){
                    news.images = c.getString(imageindex).split(",") ;
                }
                else{
                    news.images = null;
                }

                int videoindex = c.getColumnIndex(NewsEntry.News_VIDEOS);
                if (c.getString(videoindex) != null && c.getString(videoindex) != ""){
                    news.videos = c.getString(videoindex).split(",") ;
                }
                else{
                    news.videos = null ;
                }
//            ISSUE
                int issueindex = c.getColumnIndex(NewsEntry.News_ISSUE);
                news.issueTime = c.getLong(issueindex);
//            Read
                int readindex = c.getColumnIndex(NewsEntry.News_READ);
                news.read = c.getInt(readindex) ;
//            support
                int supportindex = c.getColumnIndex(NewsEntry.News_SUPPORT);
                news.support = c.getInt(supportindex) ;
//            comment
                int commentindex = c.getColumnIndex(NewsEntry.News_COMMENT);
                news.comment = c.getInt(commentindex) ;

                list.add(news);
            }while (c.moveToNext());
        }
        return list;
    }


    public News(JSONObject objectNews) {
        try{
            this.id = objectNews.getString("_id");
            this.title = objectNews.getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.brand = objectNews.getString("brand");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.product = objectNews.getString("product");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.type = objectNews.getString("type");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.subType = objectNews.getString("subType");
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            String[] tmplist = new String[objectNews.getJSONArray("tags").length()];
            for (int i = 0; i < objectNews.getJSONArray("tags").length(); i++) {
                tmplist[i] = objectNews.getJSONArray("tags").getString(i);
            }
            this.tags = tmplist;

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.content = objectNews.getString("content");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.topup = objectNews.getBoolean("topup");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.author = objectNews.getString("author");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avator = objectNews.getString("avator");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avatorPath = objectNews.getString("avatorPath");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String issuestring = objectNews.getString("issueTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);

            this.issueTime = dt.getTime();

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String[] tmplist = new String[objectNews.getJSONArray("images").length()];
            for (int i = 0; i < objectNews.getJSONArray("images").length(); i++) {
                tmplist[i] = objectNews.getJSONArray("images").getString(i);
            }
            this.images =  tmplist;
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String[] tmpvideolist = new String[objectNews.getJSONArray("videos").length()];
            for (int i = 0; i < objectNews.getJSONArray("videos").length(); i++) {
                tmpvideolist[i] = objectNews.getJSONArray("videos").getString(i);
            }
            this.videos =  tmpvideolist;
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.comment = objectNews.getInt("comment");
            this.read =  objectNews.getInt("read");
            this.support =  objectNews.getInt("support");
            this.collect =  objectNews.getInt("collect");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public News(){

    }
}
