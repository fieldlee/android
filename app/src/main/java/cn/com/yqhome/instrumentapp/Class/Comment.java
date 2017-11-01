package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by depengli on 2017/9/19.
 */

public class Comment {
    public boolean isSub;
    public String id;
    public String parentId;
    public String content;
    public String author;
    public long issueTime;
    public String fromTime;
    public String avator;
    public String avatorPath;
    public int support;


    public static final String CommentSqlName = "comment";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class CommentEntry implements BaseColumns {
        public static final String Comment_ID = "itemid";
        public static final String Comment_ParentID = "parentId";
        public static final String Comment_Content = "content";
        public static final String Comment_Author = "author";
        public static final String Comment_Issue = "issueTime";
        public static final String Comment_FromTime = "fromTime";
        public static final String Comment_Avator = "avator";
        public static final String Comment_AvatorPath = "avatorpath";
        public static final String Comment_Support = "support";
        public static final String Comment_IsSub = "isSub";
    }

    public static String createCommentSql(){
        return "CREATE TABLE IF NOT EXISTS " + CommentSqlName + " (" +
                CommentEntry.Comment_ID + " TEXT PRIMARY KEY," +
                CommentEntry.Comment_ParentID + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_Content + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_Author + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_Issue + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_FromTime + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_Avator + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_AvatorPath + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_Support + TEXT_TYPE + COMMA_SEP +
                CommentEntry.Comment_IsSub + TEXT_TYPE +
                " )";
    }

    public static ContentValues createContentValue(Comment comment){
        ContentValues values = new ContentValues();
        values.put(CommentEntry.Comment_ID, comment.id);
        values.put(CommentEntry.Comment_ParentID, comment.parentId);
        values.put(CommentEntry.Comment_Author, comment.author);
        values.put(CommentEntry.Comment_Avator, comment.avator);
        values.put(CommentEntry.Comment_AvatorPath, comment.avatorPath);
        values.put(CommentEntry.Comment_Content, comment.content);
        values.put(CommentEntry.Comment_FromTime, comment.fromTime);
        values.put(CommentEntry.Comment_Issue, comment.issueTime);
        values.put(CommentEntry.Comment_Support, comment.support);
        values.put(CommentEntry.Comment_IsSub, comment.isSub);
        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, Comment comment){
        String[] projection = {CommentEntry.Comment_ID};
        String selection = CommentEntry.Comment_ID + " = ?";
        String[] selectionArgs = { comment.id };
        Cursor c = db.query(
                CommentSqlName,                     // The table to query
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
            ContentValues values = createContentValue(comment);
            long insertvalue = db.insert(CommentSqlName,null,values);
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, Comment comment){
        String[] projection = {CommentEntry.Comment_ID};
        String selection = CommentEntry.Comment_ID + " = ?";
        String[] selectionArgs = { comment.id };
        Cursor c = db.query(
                CommentSqlName,                     // The table to query
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
            db.delete(CommentSqlName,selection,selectionArgs);
        }
        return db.insert(CommentSqlName,null,createContentValue(comment));
    }

    public Comment(JSONObject objectcomment) {
        try{
            this.id = objectcomment.getString("_id");
            this.parentId = objectcomment.getString("parentId");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.content = objectcomment.getString("content");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.author = objectcomment.getString("author");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String issuestring = objectcomment.getString("issueTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);
            this.issueTime = dt.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.fromTime = objectcomment.getString("fromTime");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avator = objectcomment.getString("avator");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.avatorPath = objectcomment.getString("avatorPath");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.support = objectcomment.getInt("support");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Comment(){

    }

}
