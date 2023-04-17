package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseSubCategoriesModel;

public class SubDBManager {

    private ConfigOpenCollection dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public SubDBManager(Context c) {
        context = c;
    }

    public SubDBManager open() throws SQLException {
        dbHelper = new ConfigOpenCollection(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(DatabaseCategoriesModel databaseCategoriesModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ConfigOpenCollection._ID, databaseCategoriesModel.getId());
        contentValue.put(ConfigOpenCollection.CATEGORY_ID, databaseCategoriesModel.getCategory_id());
        contentValue.put(ConfigOpenCollection.NAME, databaseCategoriesModel.getName());
        contentValue.put(ConfigOpenCollection.IMAGEURL, databaseCategoriesModel.getImageurl());
        contentValue.put(ConfigOpenCollection.CATEGORY_BANNER, databaseCategoriesModel.getCategory_banner());
        contentValue.put(ConfigOpenCollection.PARENTID, databaseCategoriesModel.getParent_id());
        database.insert(ConfigOpenCollection.TABLE_NAME, null, contentValue);
    }

    public void insertOption(DatabaseSubCategoriesModel databaseSubCategoriesModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ConfigOpenCollection._SUBID, databaseSubCategoriesModel.getId());
        contentValue.put(ConfigOpenCollection.SUBCATEGORYID, databaseSubCategoriesModel.getSub_category_id());
        contentValue.put(ConfigOpenCollection.SUB_IMAGEURL, databaseSubCategoriesModel.getSub_imageurl());
        contentValue.put(ConfigOpenCollection.SUB_PARENTID, databaseSubCategoriesModel.getSub_parent_id());
        contentValue.put(ConfigOpenCollection.SUB_CATEGORYNAME, databaseSubCategoriesModel.getSub_name());
        database.insert(ConfigOpenCollection.TABLE_NAME_SUB, null, contentValue);
    }


    public ArrayList<DatabaseCategoriesModel> getDataCategories() {
        ArrayList<DatabaseCategoriesModel> databaseCategoriesModels = new ArrayList<DatabaseCategoriesModel>();
        // Select All Query


        String selectQuery = "SELECT * FROM CATEGORIES" ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DatabaseCategoriesModel databaseCategoriesModel = new DatabaseCategoriesModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                        );
                databaseCategoriesModels.add(databaseCategoriesModel);
            } while (cursor.moveToNext());
        }
        // return student list
        return databaseCategoriesModels;
    }

    public ArrayList<DatabaseSubCategoriesModel> getSubCategories(int id) {
        ArrayList<DatabaseSubCategoriesModel> databaseSubCategoriesModels = new ArrayList<DatabaseSubCategoriesModel>();
        // Select All Query
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //  Cursor c = db.rawQuery("Select ConfigDatabaseHelper.ATTRIBUTEID from users INNER JOIN post ON users.ID=post.ID WHERE NAME='"+Name+"'" , null);

        Cursor cursor = db.rawQuery( "SELECT * FROM " + ConfigOpenCollection.TABLE_NAME_SUB + " WHERE " +
                ConfigOpenCollection.SUB_PARENTID + "=?", new String[] { Integer.toString(id) } , null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseSubCategoriesModel databaseSubCategoriesModel = new DatabaseSubCategoriesModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                        );
                databaseSubCategoriesModels.add(databaseSubCategoriesModel);
            } while (cursor.moveToNext());
        }
        // return student list

        return databaseSubCategoriesModels;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + ConfigDatabaseHelper.TABLE_NAMEOPEN;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void delete(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from "+ ConfigOpenCollection.TABLE_NAME);
    }
    public void delete1(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from "+ ConfigOpenCollection.TABLE_NAME_SUB);
    }
}
