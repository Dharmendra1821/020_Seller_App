package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import standalone.eduqfix.qfixinfo.com.app.add_new_product.model.DeliveryBoyList;
import standalone.eduqfix.qfixinfo.com.app.image_add_product.activities.DatabaseCategoriesModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ConfigProductModel;
import standalone.eduqfix.qfixinfo.com.app.seller_app.container.ProductSizeModel;

public class DeliveryDBManager {

    private DeliveryBoyDatabase dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DeliveryDBManager(Context c) {
        context = c;
    }

    public DeliveryDBManager open() throws SQLException {
        dbHelper = new DeliveryBoyDatabase(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(DeliveryBoyList deliveryBoyList) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DeliveryBoyDatabase.ID, deliveryBoyList.getId());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYENTITY, deliveryBoyList.getEntityId());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYEMAIL, deliveryBoyList.getEmail());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYFIRST, deliveryBoyList.getFirstname());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYLAST, deliveryBoyList.getLastname());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYCONTACT, deliveryBoyList.getContact());
        contentValue.put(DeliveryBoyDatabase.DELIVERYBOYSTATUS, deliveryBoyList.getStatus());
        database.insert(DeliveryBoyDatabase.TABLENAME, null, contentValue);
    }


    public ArrayList<DeliveryBoyList> getDeliveryBoyList(String id) {
        ArrayList<DeliveryBoyList> deliveryBoyLists = new ArrayList<DeliveryBoyList>();
        // Select All Query
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery( "SELECT * FROM " + DeliveryBoyDatabase.TABLENAME + " WHERE " +
                DeliveryBoyDatabase.DELIVERYBOYENTITY + "=?", new String[] { id } , null);

        if (cursor.moveToFirst()) {
            do {
                DeliveryBoyList deliveryBoyList = new DeliveryBoyList(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                        );
                deliveryBoyLists.add(deliveryBoyList);
            } while (cursor.moveToNext());
        }
        // return student list
        return deliveryBoyLists;
    }

    public ArrayList<DeliveryBoyList> getDeliveryBoyEntity(String name) {
        ArrayList<DeliveryBoyList> deliveryBoyLists = new ArrayList<DeliveryBoyList>();
        // Select All Query
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery( "SELECT * FROM " + DeliveryBoyDatabase.TABLENAME + " WHERE " +
                DeliveryBoyDatabase.DELIVERYBOYFIRST  + "=?", new String[] { name } , null);

        if (cursor.moveToFirst()) {
            do {
                DeliveryBoyList deliveryBoyList = new DeliveryBoyList(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                deliveryBoyLists.add(deliveryBoyList);
            } while (cursor.moveToNext());
        }
        // return student list
        return deliveryBoyLists;
    }


    public ArrayList<DeliveryBoyList> getAllDeliveryBoy() {
        ArrayList<DeliveryBoyList> deliveryBoyLists = new ArrayList<DeliveryBoyList>();
        // Select All Query


        String selectQuery = "SELECT * FROM " + DeliveryBoyDatabase.TABLENAME ;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DeliveryBoyList deliveryBoyList = new DeliveryBoyList(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                deliveryBoyLists.add(deliveryBoyList);
            } while (cursor.moveToNext());
        }
        // return student list
        return deliveryBoyLists;
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
        db.execSQL("delete from "+ DeliveryBoyDatabase.TABLENAME);
    }

}
