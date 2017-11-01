package cn.com.yqhome.instrumentapp.Class;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by depengli on 2017/9/12.
 */

public class Ads {
    public String id;
    public String title;
    public String subtitle;
    public String type;
    public String value;
    public String path;
    public String imagename;
    public long startTime;
    public long endTime;


    public static final String AdsSqlName = "ads";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static class AdsEntry implements BaseColumns {
        public static final String Ads_ID = "itemid";
        public static final String Ads_Title = "title";
        public static final String Ads_SubTitle = "subtitle";
        public static final String Ads_Type = "type";
        public static final String Ads_Value = "value";
        public static final String Ads_Path = "path";
        public static final String Ads_ImageName = "imagename";
        public static final String Ads_StartTime = "startTime";
        public static final String Ads_EndTime = "endTime";
    }

    public static String createAdsSql(){
        return "CREATE TABLE IF NOT EXISTS " + AdsSqlName + " (" +
                AdsEntry.Ads_ID + " TEXT PRIMARY KEY," +
                AdsEntry.Ads_Title + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_Type + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_Path + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_Value + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_SubTitle + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_ImageName + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_StartTime + TEXT_TYPE + COMMA_SEP +
                AdsEntry.Ads_EndTime + TEXT_TYPE +
                " )";
    }

    public static ContentValues createContentValue(Ads ads){
        ContentValues values = new ContentValues();
        values.put(AdsEntry.Ads_ID, ads.id);
        values.put(AdsEntry.Ads_Title, ads.title);
        values.put(AdsEntry.Ads_Type, ads.type);
        values.put(AdsEntry.Ads_SubTitle, ads.subtitle);
        values.put(AdsEntry.Ads_Value, ads.value);
        values.put(AdsEntry.Ads_Path, ads.path);
        values.put(AdsEntry.Ads_ImageName, ads.imagename);
        values.put(AdsEntry.Ads_StartTime, ads.startTime);
        values.put(AdsEntry.Ads_EndTime, ads.endTime);
        return values;
    }

    public static long insertContentValue(SQLiteDatabase db, Ads ads){
        String[] projection = {AdsEntry.Ads_ID};
        String selection = AdsEntry.Ads_ID + " = ?";
        String[] selectionArgs = { ads.id };
        Cursor c = db.query(
                AdsSqlName,                     // The table to query
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
            ContentValues values = createContentValue(ads);
            long insertvalue = db.insert(AdsSqlName,null,values);
            return insertvalue;
        }
    }

    public static long updateContentValue(SQLiteDatabase db, Ads ads){
        String[] projection = {AdsEntry.Ads_ID};
        String selection = AdsEntry.Ads_ID + " = ?";
        String[] selectionArgs = { ads.id };
        Cursor c = db.query(
                AdsSqlName,                     // The table to query
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
            db.delete(AdsSqlName,selection,selectionArgs);
        }
        return db.insert(AdsSqlName,null,createContentValue(ads));
    }

    public Ads(JSONObject objectnews) {
        try{
            this.id = objectnews.getString("_id");
            this.title = objectnews.getString("title");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            if (!objectnews.isNull("subtitle")){
                this.subtitle = objectnews.getString("subtitle");
            }else{
                this.subtitle = "";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.type = objectnews.getString("type");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.value = objectnews.getString("value");
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            this.path = objectnews.getString("path");
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (!objectnews.isNull("imagename")){
                this.imagename = objectnews.getString("imagename");
            }else{
                this.imagename = "";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String issuestring = objectnews.getString("startTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);

            this.startTime = dt.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String issuestring = objectnews.getString("endTime");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date dt = parser.parse(issuestring);
            this.endTime = dt.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public Ads(){

    }

    public static List<Ads> getAllAdsFromDB(SQLiteDatabase db){

        String[] projection = {
                AdsEntry.Ads_ID,
                AdsEntry.Ads_Title,
                AdsEntry.Ads_Type,
                AdsEntry.Ads_SubTitle,
                AdsEntry.Ads_Value,
                AdsEntry.Ads_Path,
                AdsEntry.Ads_ImageName,
                AdsEntry.Ads_StartTime,
                AdsEntry.Ads_EndTime
        };
        String sortOrder =
                AdsEntry.Ads_StartTime + " DESC";

        Cursor c = db.query(
                AdsSqlName,                     // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Ads> list = new ArrayList<Ads>();

        if (c != null && c.moveToFirst()){
            do{
                Ads ads = new Ads();
                //id
                int idindex = c.getColumnIndex(AdsEntry.Ads_ID);
                ads.id = c.getString(idindex);
                //title
                int titleindex = c.getColumnIndex(AdsEntry.Ads_Title);
                ads.title = c.getString(titleindex);
                //type
                int typeindex = c.getColumnIndex(AdsEntry.Ads_Type);
                ads.type = c.getString(typeindex);
//            subtitle
                int subtypeindex = c.getColumnIndex(AdsEntry.Ads_SubTitle);
                ads.subtitle = c.getString(subtypeindex);
//            imagename
                int contentindex = c.getColumnIndex(AdsEntry.Ads_ImageName);
                ads.imagename = c.getString(contentindex);
//            path
                int tagindex = c.getColumnIndex(AdsEntry.Ads_Path);
                ads.path = c.getString(tagindex);
//            value
                int topupindex = c.getColumnIndex(AdsEntry.Ads_Value);
                ads.path = c.getString(topupindex);
//            Ads_StartTime
                int authorindex = c.getColumnIndex(AdsEntry.Ads_StartTime);
                ads.startTime = c.getLong(authorindex);
//            Ads_EndTime
                int avatorindex = c.getColumnIndex(AdsEntry.Ads_EndTime);
                ads.endTime = c.getLong(avatorindex);

                list.add(ads);
            }while (c.moveToNext());
            c.close();
        }
        return list;
    }
}
