package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by depengli on 2017/9/12.
 */

public class Live implements Serializable {

    public String id;
    public String avator;
    public String avatorPath;
    public long issueTime;
    public String tel;
    public String idcard;
    public String author;
    public String image;
    public String sign;
    public Number attend;
    public Number support;
    public String types;
    public String mainid;
    public String status;

    public static final String LiveSqlName = "live";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class LiveEntry implements BaseColumns {
        public static final String Live_ID = "itemid";
        public static final String Live_AVATOR = "avator";
        public static final String Live_avatorPath = "avatorPath";
        public static final String Live_IssueTime = "issueTime";
        public static final String Live_TEL = "tel";
        public static final String Live_IDCard = "idcard";
        public static final String Live_Author = "author";
        public static final String Live_Image = "image";
        public static final String Live_Sign = "sign";
        public static final String Live_Attend = "attend";
        public static final String Live_Support = "support";
        public static final String Live_Types = "types";
        public static final String Live_MainId = "mainid";
        public static final String Live_Status = "status";

    }


    public static String createLiveSql(){
        return "CREATE TABLE IF NOT EXISTS " + LiveSqlName + " (" +
                LiveEntry.Live_ID + " TEXT PRIMARY KEY," +
                LiveEntry.Live_Attend + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Author + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_AVATOR + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_avatorPath + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_IDCard + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Image + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_IssueTime + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Sign + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Status + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Types + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_Support + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_TEL + TEXT_TYPE + COMMA_SEP +
                LiveEntry.Live_MainId + TEXT_TYPE +
                " )";
    }

    public static ContentValues createContentValue(Live live){
        ContentValues values = new ContentValues();
        values.put(LiveEntry.Live_ID, live.id);
        values.put(LiveEntry.Live_Attend, live.attend.toString());
        values.put(LiveEntry.Live_Author, live.author);
        values.put(LiveEntry.Live_IDCard, live.idcard);
        values.put(LiveEntry.Live_Image, live.image);
        values.put(LiveEntry.Live_IssueTime, live.issueTime);
        values.put(LiveEntry.Live_Sign, live.sign);
        values.put(LiveEntry.Live_Status, live.status);
        values.put(LiveEntry.Live_TEL, live.tel);
        values.put(LiveEntry.Live_Types, live.types);
        values.put(LiveEntry.Live_Support,live.support.toString());
        values.put(LiveEntry.Live_MainId,live.mainid);
        values.put(LiveEntry.Live_AVATOR,live.avator);
        values.put(LiveEntry.Live_avatorPath,live.avatorPath);
        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, Live live){
        String[] projection = {LiveEntry.Live_ID};
        String selection = LiveEntry.Live_ID + " = ?";
        String[] selectionArgs = { live.id };
        Cursor c = db.query(
                LiveSqlName,                     // The table to query
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
            ContentValues values = createContentValue(live);
            long insertvalue = db.insert(LiveSqlName,null,values);
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, Live live){
        String[] projection = {LiveEntry.Live_ID};
        String selection = LiveEntry.Live_ID + " = ?";
        String[] selectionArgs = { live.id };
        Cursor c = db.query(
                LiveSqlName,                     // The table to query
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
            db.delete(LiveSqlName,selection,selectionArgs);
        }
        return db.insert(LiveSqlName,null,createContentValue(live));
    }

    public static List<Live> getAllLivesFromDB(SQLiteDatabase db){
        String[] projection = {
                LiveEntry.Live_ID,
                LiveEntry.Live_Attend,
                LiveEntry.Live_Author,
                LiveEntry.Live_AVATOR,
                LiveEntry.Live_avatorPath,
                LiveEntry.Live_IDCard,
                LiveEntry.Live_Image,
                LiveEntry.Live_IssueTime,
                LiveEntry.Live_Sign,
                LiveEntry.Live_Status,
                LiveEntry.Live_Types,
                LiveEntry.Live_Support,
                LiveEntry.Live_TEL,
                LiveEntry.Live_MainId
        };

// Filter results WHERE "title" = 'My Title'


// How you want the results sorted in the resulting Cursor
        String sortOrder =
                LiveEntry.Live_IssueTime + " DESC";

        Cursor c = db.query(
                LiveSqlName,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Live> list = new ArrayList<Live>();

        if (c != null && c.moveToFirst()){
            do{
                Live live = new Live();
                //id
                int idindex = c.getColumnIndex(LiveEntry.Live_ID);
                live.id = c.getString(idindex);
                //avator
                int avatorindex = c.getColumnIndex(LiveEntry.Live_AVATOR);
                live.avator = c.getString(avatorindex);
                //avatorPath
                int avatorPathindex = c.getColumnIndex(LiveEntry.Live_avatorPath);
                live.avatorPath = c.getString(avatorPathindex);
                //issueTime  long
                int issueindex = c.getColumnIndex(LiveEntry.Live_IssueTime);
                live.issueTime = c.getLong(issueindex);
                //tel
                int telindex = c.getColumnIndex(LiveEntry.Live_TEL);
                live.tel = c.getString(telindex);
                //idcard
                int idcardindex = c.getColumnIndex(LiveEntry.Live_IDCard);
                live.idcard = c.getString(idcardindex);
                //author
                int authorindex = c.getColumnIndex(LiveEntry.Live_Author);
                live.author = c.getString(authorindex);
                //image
                int imageindex = c.getColumnIndex(LiveEntry.Live_Image);
                live.image = c.getString(imageindex);
                //sign
                int signindex = c.getColumnIndex(LiveEntry.Live_Sign);
                live.sign = c.getString(signindex);
                //attend  int
                int attentindex = c.getColumnIndex(LiveEntry.Live_Attend);
                live.attend = c.getInt(attentindex);
                //support  int
                int supportindex = c.getColumnIndex(LiveEntry.Live_Support);
                live.support = c.getInt(supportindex);
                //types string
                int typesindex = c.getColumnIndex(LiveEntry.Live_Types);
                live.types = c.getString(typesindex);
                //mainid
                int mainidindex = c.getColumnIndex(LiveEntry.Live_MainId);
                live.mainid = c.getString(mainidindex);
                //status
                int statusindex = c.getColumnIndex(LiveEntry.Live_Status);
                live.status = c.getString(statusindex);
                list.add(live);
            }while (c.moveToNext());
            c.close();
        }
        return list;
    }


    public Live(JSONObject objectLive) {
        try{
            this.id = objectLive.getString("_id");
            this.avator = objectLive.getString("avator");
            this.avatorPath = objectLive.getString("avatorPath");
            this.tel = objectLive.getString("tel");
            this.idcard = objectLive.getString("idcard");
            this.author = objectLive.getString("author");
            this.image = objectLive.getString("image");
            this.sign = objectLive.getString("sign");
            this.mainid = objectLive.getString("mainid");

            //this.types = objectLive.getString("types");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            JSONArray types = objectLive.getJSONArray("types");
            String typestring = "";
            for (int i = 0; i < types.length() ; i++) {
                if (typestring.isEmpty()){
                    typestring = types.get(i).toString();
                }else{
                    typestring = typestring +","+ types.get(i).toString();
                }
            }
            this.types = typestring;
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String issuestring = objectLive.getString("issueTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);
            this.issueTime = dt.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.attend = objectLive.getInt("attend");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            this.support = objectLive.getInt("support");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Live(){

    }
}
