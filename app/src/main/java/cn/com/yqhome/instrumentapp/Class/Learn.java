package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.yqhome.instrumentapp.BaseUtils;

/**
 * Created by depengli on 2017/9/29.
 */
public class Learn implements Serializable {
    public String id;
    public String title;
    public String type;
    public String region;
    public Map mp3;
    public List<String> files;
    public String difficult;
    public String bpt;
    public String author;

    public String avator;
    public String authorPath;
    public String style;
    public int read;
    public int support;
    public long issue;


    public static final String LearnSqlName = "learn";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class LearnEntry implements BaseColumns {
        public static final String Learn_id = "itemid";
        public static final String Learn_Title = "title";
        public static final String Learn_Type = "type";
        public static final String Learn_Region = "region";
        public static final String Learn_Mp3 = "mp3";
        public static final String Learn_Files = "files";
        public static final String Learn_Diff = "difficult";
        public static final String Learn_Bpt = "bpt";
        public static final String Learn_Author = "author";
        public static final String Learn_Avator = "avator";
        public static final String Learn_AuthorPath = "authorPath";
        public static final String Learn_Style = "style";
        public static final String Learn_Read = "read";
        public static final String Learn_Support = "support";
        public static final String Learn_Issue = "issue";
    }


    public static String createLearnSql(){
        return "CREATE TABLE IF NOT EXISTS " + LearnSqlName + " (" +
                LearnEntry.Learn_id + " TEXT PRIMARY KEY," +
                LearnEntry.Learn_Title + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Type + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Region + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Mp3 + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Files + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Diff + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Bpt + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Author + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Avator + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_AuthorPath + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Style + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Read + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Issue + TEXT_TYPE + COMMA_SEP +
                LearnEntry.Learn_Support + TEXT_TYPE +
                " )";

    }

    public static ContentValues createContentValue(Learn learn){
        ContentValues values = new ContentValues();
        values.put(LearnEntry.Learn_id, learn.id);
        values.put(LearnEntry.Learn_Title, learn.title);
        values.put(LearnEntry.Learn_Type, learn.type);
        values.put(LearnEntry.Learn_Region, learn.region);
//        mp3
        if (learn.mp3 != null){
            values.put(LearnEntry.Learn_Mp3, (String) learn.mp3.get("path"));
        }
//      learn files
        if (learn.files != null && learn.files.size() >0){
            String fileString = "";
            for (int i = 0; i < learn.files.size(); i++) {
                if (fileString.isEmpty()){
                    fileString = learn.files.get(i);
                }else{
                    fileString = fileString+","+learn.files.get(i);
                }
            }
            values.put(LearnEntry.Learn_Files, fileString);
        }
//
        values.put(LearnEntry.Learn_Diff, learn.difficult);
//        Learn_Bpt
        values.put(LearnEntry.Learn_Bpt, learn.bpt);
//        Learn_Author
        values.put(LearnEntry.Learn_Author, learn.author);
//        Learn_Avator
        values.put(LearnEntry.Learn_Avator, learn.avator);
//        Learn_AuthorPath
        values.put(LearnEntry.Learn_AuthorPath, learn.authorPath);
//        Learn_Style
        values.put(LearnEntry.Learn_Style, learn.style);
//        Learn_Read
        values.put(LearnEntry.Learn_Read, learn.read);
//        Learn_Support
        values.put(LearnEntry.Learn_Support, learn.support);
//        Learn_Issue
        values.put(LearnEntry.Learn_Issue, learn.issue);
        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, Learn learn){
        String[] projection = {LearnEntry.Learn_id};
        String selection = LearnEntry.Learn_id + " = ?";
        String[] selectionArgs = { learn.id };
        Cursor c = db.query(
                LearnSqlName,                     // The table to query
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
            ContentValues values = createContentValue(learn);
            long insertvalue = db.insert(LearnSqlName,null,values);
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, Learn learn){
        String[] projection = {LearnEntry.Learn_id};
        String selection = LearnEntry.Learn_id + " = ?";
        String[] selectionArgs = { learn.id };
        Cursor c = db.query(
                LearnSqlName,                     // The table to query
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
            db.delete(LearnSqlName,selection,selectionArgs);
        }

        return db.insert(LearnSqlName,null,createContentValue(learn));

    }

    public static List<Learn> getAllLearnsFromDB(SQLiteDatabase db){
        String[] projection = {
                LearnEntry.Learn_id,
                LearnEntry.Learn_Support,
                LearnEntry.Learn_Read,
                LearnEntry.Learn_Style,
                LearnEntry.Learn_Author,
                LearnEntry.Learn_Mp3,
                LearnEntry.Learn_Region,
                LearnEntry.Learn_AuthorPath,
                LearnEntry.Learn_Files,
                LearnEntry.Learn_Avator,
                LearnEntry.Learn_Bpt,
                LearnEntry.Learn_Diff,
                LearnEntry.Learn_Title,
                LearnEntry.Learn_Type,
                LearnEntry.Learn_Issue
        };

// Filter results WHERE "title" = 'My Title'


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                LearnEntry.Learn_Issue + " DESC";

        Cursor c = db.query(
                LearnSqlName,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Learn> list = new ArrayList<Learn>();
        c.moveToFirst();
        do{
            Learn learn = new Learn();
            //id
            int idindex = c.getColumnIndex(LearnEntry.Learn_id);
            learn.id = c.getString(idindex);
            //title
            int titleindex = c.getColumnIndex(LearnEntry.Learn_Title);
            learn.title = c.getString(titleindex);
            //type
            int typeindex = c.getColumnIndex(LearnEntry.Learn_Type);
            learn.type = c.getString(typeindex);
//            region
            int regionindex = c.getColumnIndex(LearnEntry.Learn_Region);
            learn.region = c.getString(regionindex);
//            mp3
            int mp3index = c.getColumnIndex(LearnEntry.Learn_Mp3);
            Map<String,String> mp3map = new HashMap<>();
            mp3map.put("path",c.getString(mp3index));
            learn.mp3 = mp3map;
//                    files
            int filesindex = c.getColumnIndex(LearnEntry.Learn_Files);
            List<String> filesList = new ArrayList<String>();
            if (c.getString(filesindex) != null){
                String[] filesString = c.getString(filesindex).split(",");
                for (int i = 0; i < filesString.length; i++) {
                    filesList.add(filesString[i]);
                }
            }
            learn.files = filesList;

//            difficult
            int diffindex = c.getColumnIndex(LearnEntry.Learn_Diff);
            learn.difficult = c.getString(diffindex);
//                    bpt
            int bptindex = c.getColumnIndex(LearnEntry.Learn_Bpt);
            learn.bpt = c.getString(bptindex);
//            author
            int authorindex = c.getColumnIndex(LearnEntry.Learn_Author);
            learn.author = c.getString(authorindex);
//                    avator
            int avatorindex = c.getColumnIndex(LearnEntry.Learn_Avator);
            learn.avator = c.getString(avatorindex);
//            authorPath
            int authPathIndex = c.getColumnIndex(LearnEntry.Learn_AuthorPath);
            learn.authorPath = c.getString(authPathIndex);
//                    style
            int styleIndex = c.getColumnIndex(LearnEntry.Learn_Style);
            learn.style = c.getString(styleIndex);
//            read
            int readIndex = c.getColumnIndex(LearnEntry.Learn_Read);
            learn.read = c.getInt(readIndex);
//                    support
            int supportIndex = c.getColumnIndex(LearnEntry.Learn_Support);
            learn.support = c.getInt(supportIndex);
//            issue
            int issueIndex = c.getColumnIndex(LearnEntry.Learn_Issue);
            learn.issue = c.getLong(issueIndex);

            list.add(learn);
        }while (c.moveToNext());

        return list;
    }

    public static List<Learn> getAllLearnsByType(SQLiteDatabase db,String type){
        String[] projection = {
                LearnEntry.Learn_id,
                LearnEntry.Learn_Support,
                LearnEntry.Learn_Read,
                LearnEntry.Learn_Style,
                LearnEntry.Learn_Author,
                LearnEntry.Learn_Mp3,
                LearnEntry.Learn_Region,
                LearnEntry.Learn_AuthorPath,
                LearnEntry.Learn_Files,
                LearnEntry.Learn_Avator,
                LearnEntry.Learn_Bpt,
                LearnEntry.Learn_Diff,
                LearnEntry.Learn_Title,
                LearnEntry.Learn_Type,
                LearnEntry.Learn_Issue
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder = LearnEntry.Learn_Issue + " DESC ";
        String selection = LearnEntry.Learn_Type + " = ? ";
        String[] selectionArgs = { type };

        if (type=="其他") {
            selection = LearnEntry.Learn_Type + " is Null or (";
            for (int i = 0; i < BaseUtils.TYPE_LEARNINSTRUMENT.length; i++) {
                if (i == 0){
                    selection = selection + LearnEntry.Learn_Type + " != ? " ;
                }else{
                    selection = selection + " and "+ LearnEntry.Learn_Type + " != ? ";
                }
            }
            selection = selection +" )";
            selectionArgs = BaseUtils.TYPE_LEARNINSTRUMENT;
        }


        Cursor c = db.query(
                LearnSqlName,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Learn> list = new ArrayList<Learn>();
        c.moveToFirst();
        if (c.getCount()>0){
            do{
                Learn learn = new Learn();
                //id
                int idindex = c.getColumnIndex(LearnEntry.Learn_id);
                learn.id = c.getString(idindex);
                //title
                int titleindex = c.getColumnIndex(LearnEntry.Learn_Title);
                learn.title = c.getString(titleindex);
                //type
                int typeindex = c.getColumnIndex(LearnEntry.Learn_Type);
                learn.type = c.getString(typeindex);
//            region
                int regionindex = c.getColumnIndex(LearnEntry.Learn_Region);
                learn.region = c.getString(regionindex);
//            mp3
                int mp3index = c.getColumnIndex(LearnEntry.Learn_Mp3);
                Map<String,String> mp3map = new HashMap<>();
                mp3map.put("path",c.getString(mp3index));
                learn.mp3 = mp3map;
//                    files
                int filesindex = c.getColumnIndex(LearnEntry.Learn_Files);
                List<String> filesList = new ArrayList<String>();
                if (c.getString(filesindex) == null){
                    learn.files = filesList;
                }else{
                    String[] filesString = c.getString(filesindex).split(",");
                    for (int i = 0; i < filesString.length; i++) {
                        filesList.add(filesString[i]);
                    }
                    learn.files = filesList;
                }
//            difficult
                int diffindex = c.getColumnIndex(LearnEntry.Learn_Diff);
                learn.difficult = c.getString(diffindex);
//                    bpt
                int bptindex = c.getColumnIndex(LearnEntry.Learn_Bpt);
                learn.bpt = c.getString(bptindex);
//            author
                int authorindex = c.getColumnIndex(LearnEntry.Learn_Author);
                learn.author = c.getString(authorindex);
//                    avator
                int avatorindex = c.getColumnIndex(LearnEntry.Learn_Avator);
                learn.avator = c.getString(avatorindex);
//            authorPath
                int authPathIndex = c.getColumnIndex(LearnEntry.Learn_AuthorPath);
                learn.authorPath = c.getString(authPathIndex);
//                    style
                int styleIndex = c.getColumnIndex(LearnEntry.Learn_Style);
                learn.style = c.getString(styleIndex);
//            read
                int readIndex = c.getColumnIndex(LearnEntry.Learn_Read);
                learn.read = c.getInt(readIndex);
//                    support
                int supportIndex = c.getColumnIndex(LearnEntry.Learn_Support);
                learn.support = c.getInt(supportIndex);
//            issue
                int issueIndex = c.getColumnIndex(LearnEntry.Learn_Issue);
                learn.issue = c.getLong(issueIndex);

                list.add(learn);
            }while (c.moveToNext());
        }
        return list;
    }

    public Learn(JSONObject objectForum) {
        try{
            this.id = objectForum.getString("_id");
            this.title = objectForum.getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }

//        type
        try{
            this.type = objectForum.getString("type");
        }catch (Exception e){
            e.printStackTrace();
        }
//                region
        try{
            this.region = objectForum.getString("region");
        }catch (Exception e){
            e.printStackTrace();
        }
//        mp3
        try{
           JSONObject mp3Obj = objectForum.getJSONObject("mp3");
            if (mp3Obj != null){
                Map<String,String> mp3map = new HashMap<>();
                mp3map.put("path",mp3Obj.getString("path"));
                this.mp3 = mp3map;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//                files
        try{
            List<String> filesArr = new ArrayList<>();
            JSONArray files = objectForum.getJSONArray("files");
            for (int i = 0; i < files.length(); i++) {
                JSONObject tmpobj = files.getJSONObject(i);
                filesArr.add(tmpobj.getString("path"));
            }
            this.files = filesArr;
        }catch (Exception e){
            e.printStackTrace();
        }
//        difficult
        try{
            this.difficult = objectForum.getString("difficult");
        }catch (Exception e){
            e.printStackTrace();
        }
//                bpt
        try{
            this.bpt = objectForum.getString("bpt");
        }catch (Exception e){
            e.printStackTrace();
        }
//        author
        try{
            this.author = objectForum.getString("author");
        }catch (Exception e){
            e.printStackTrace();
        }
//                avator
        try{
            this.avator = objectForum.getString("avator");
        }catch (Exception e){
            e.printStackTrace();
        }
//        authorPath
        try{
            this.authorPath = objectForum.getString("authorPath");
        }catch (Exception e){
            e.printStackTrace();
        }
//                style
        try{
            this.style = objectForum.getString("style");
        }catch (Exception e){
            e.printStackTrace();
        }
//        read
        try{
            this.read = objectForum.getInt("read");
        }catch (Exception e){
            e.printStackTrace();
        }
//                support
        try{
            this.support = objectForum.getInt("support");
        }catch (Exception e){
            e.printStackTrace();
        }
//        issue
        try{
            this.issue = objectForum.getLong("issue");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
///mobile/:type/:time
    public Learn(){

    }
}
