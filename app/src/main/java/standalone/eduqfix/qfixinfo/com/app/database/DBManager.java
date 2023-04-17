package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ConfigProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeModel;

public class DBManager {

    private ConfigDatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new ConfigDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(ConfigProductModel configProductModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ConfigDatabaseHelper._ID, configProductModel.getId());
        contentValue.put(ConfigDatabaseHelper.PRODUCTID, configProductModel.getProductid());
        contentValue.put(ConfigDatabaseHelper.PRODUCTIMAGE, configProductModel.getProductimage());
        contentValue.put(ConfigDatabaseHelper.PRODUCTNAME, configProductModel.getProductname());
        contentValue.put(ConfigDatabaseHelper.PRODUCTPRICE, configProductModel.getProductprice());
        contentValue.put(ConfigDatabaseHelper.PRODUCTSMALLIMAGE, configProductModel.getProductsmallimage());
        contentValue.put(ConfigDatabaseHelper.PRODUCTSTYPE, configProductModel.getProducttype());
        contentValue.put(ConfigDatabaseHelper.PRODUCTSKU, configProductModel.getProductsku());
        contentValue.put(ConfigDatabaseHelper.PRODUCTSTHUMBNAIL, configProductModel.getProductsmallimage());
        database.insert(ConfigDatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public void insertOption(ProductSizeModel productSizeModel) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(ConfigDatabaseHelper._OPENID, productSizeModel.getId());
        contentValue.put(ConfigDatabaseHelper.ATTRIBUTEID, productSizeModel.getAttributeid());
        contentValue.put(ConfigDatabaseHelper.OPENPRODUCTID, productSizeModel.getProductid());
        contentValue.put(ConfigDatabaseHelper.DEFAULT_TITLE, productSizeModel.getTitle());
        contentValue.put(ConfigDatabaseHelper.DISPLAYLABEL, productSizeModel.getLabel());
        contentValue.put(ConfigDatabaseHelper.PRICE, productSizeModel.getPrice());
        contentValue.put(ConfigDatabaseHelper.SKU, productSizeModel.getSku());
        contentValue.put(ConfigDatabaseHelper.VALUEINDEX, productSizeModel.getIndex());
        database.insert(ConfigDatabaseHelper.TABLE_NAMEOPEN, null, contentValue);
    }


    public ArrayList<ConfigProductModel> getConfigProduct(int id) {
        ArrayList<ConfigProductModel> productSizeModels = new ArrayList<ConfigProductModel>();
        // Select All Query
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery( "SELECT * FROM " + ConfigDatabaseHelper.TABLE_NAME + " WHERE " +
                ConfigDatabaseHelper.PRODUCTID + "=?", new String[] { Integer.toString(id) } , null);

        if (cursor.moveToFirst()) {
            do {
                ConfigProductModel configProductModel = new ConfigProductModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8));
                productSizeModels.add(configProductModel);
            } while (cursor.moveToNext());
        }
        // return student list
        return productSizeModels;
    }

    public ArrayList<ProductSizeModel> getProductOption(int id) {
        ArrayList<ProductSizeModel> productSizeModels = new ArrayList<ProductSizeModel>();
        // Select All Query
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //  Cursor c = db.rawQuery("Select ConfigDatabaseHelper.ATTRIBUTEID from users INNER JOIN post ON users.ID=post.ID WHERE NAME='"+Name+"'" , null);

        Cursor cursor = db.rawQuery( "SELECT * FROM " + ConfigDatabaseHelper.TABLE_NAMEOPEN + " WHERE " +
                ConfigDatabaseHelper.OPENPRODUCTID + "=?", new String[] { Integer.toString(id) } , null);

        if (cursor.moveToFirst()) {
            do {
                ProductSizeModel productSizeModel = new ProductSizeModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7));
                productSizeModels.add(productSizeModel);
            } while (cursor.moveToNext());
        }
        // return student list

        return productSizeModels;
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
        db.execSQL("delete from "+ ConfigDatabaseHelper.TABLE_NAME);
    }
    public void delete1(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.execSQL("delete from "+ ConfigDatabaseHelper.TABLE_NAMEOPEN);
    }
}
