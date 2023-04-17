package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConfigDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "CONFIGPRODUCT";

    // Table columns
    public static final String _ID = "_id";
    public static final String PRODUCTID =        "productid";
    public static final String PRODUCTIMAGE =     "productimage";
    public static final String PRODUCTNAME =      "productname";
    public static final String PRODUCTPRICE =     "productprice";
    public static final String PRODUCTSMALLIMAGE= "productsmallimage";
    public static final String PRODUCTSTYPE =     "producttype";
    public static final String PRODUCTSKU =       "productsku";
    public static final String PRODUCTSTHUMBNAIL= "productthumbnail";


    public static final String TABLE_NAMEOPEN = "CONFIGOPENCOLLECTION";

    // Table columns
    public static final String _OPENID = "_id";
    public static final String ATTRIBUTEID = "attribute_id";
    public static final String DEFAULT_TITLE = "default_title";
    public static final String DISPLAYLABEL = "display_label";
    public static final String PRICE = "price";
    public static final String OPENPRODUCTID = "productid";
    public static final String SKU = "optionsku";
    public static final String VALUEINDEX = "valueindex";


    // Database Information
    static final String DB_NAME = "CONFIGPRODUCT.DB";

    // database versionINTEGER PRIMARY KEY AUTOINCREMENT,
    static final int DB_VERSION = 1;
//create table CONFIGPRODUCT(_id INTEGER , productid INTEGER PRIMARY KEY AUTOINCREMENT , productimage TEXT , productname TEXT , productprice TEXT , productsmallimage TEXT , producttype TEXT , productsku TEXT , productthumbnail TEXT);
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER , " + PRODUCTID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            PRODUCTIMAGE + " TEXT , " +
            PRODUCTNAME + " TEXT , " +
            PRODUCTPRICE + " TEXT , " +
            PRODUCTSMALLIMAGE + " TEXT , " +
            PRODUCTSTYPE + " TEXT , " +
            PRODUCTSKU + " TEXT , " +
            PRODUCTSTHUMBNAIL + " TEXT);";

    private static final String CREATE_OPENTABLE = "create table " + TABLE_NAMEOPEN + "(" + _OPENID
            + " INTEGER , " + OPENPRODUCTID + " INTEGER  , " +
            ATTRIBUTEID + " TEXT , " +
            DEFAULT_TITLE + " TEXT , " +
            DISPLAYLABEL + " TEXT , " +
            PRICE + " TEXT , " +
            SKU + " TEXT , " +
            VALUEINDEX + " TEXT);";

    public ConfigDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_OPENTABLE);
        Log.d("Execute....",CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEOPEN);
        onCreate(db);
    }



}
