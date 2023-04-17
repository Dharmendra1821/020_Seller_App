package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConfigOpenCollection extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "CATEGORIES";

    // Table columns
    public static final String _ID =             "_id";
    public static final String CATEGORY_ID =     "category_id";
    public static final String NAME =            "name";
    public static final String IMAGEURL =        "imageurl";
    public static final String CATEGORY_BANNER = "category_banner";
    public static final String PARENTID =        "parent_id";


    // Database Information
    static final String DB_NAME = "CATEGORIES.DB";

    // database version
    static final int DB_VERSION = 1;

    public static final String TABLE_NAME_SUB = "SUBCATEGORIES";

    // Table columns
    public static final String _SUBID = "_id";
    public static final String SUBCATEGORYID =    "sub_category_id";
    public static final String SUB_CATEGORYNAME =   "sub_category_name";
    public static final String SUB_IMAGEURL =     "sub_imageurl";
    public static final String SUB_PARENTID =     "sub_parent_id";


    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER , " + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            NAME + " TEXT , " +
            IMAGEURL + " TEXT , " +
            CATEGORY_BANNER + " TEXT , " +
            PARENTID + " TEXT);";


    private static final String CREATE_SUBTABLE = "create table " + TABLE_NAME_SUB + "(" + _SUBID
            + " INTEGER , " +
            SUBCATEGORYID + " TEXT , " +
            SUB_IMAGEURL + " TEXT , " +
            SUB_PARENTID + " TEXT , " +
            SUB_CATEGORYNAME + " TEXT);";




    public ConfigOpenCollection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_SUBTABLE);
       // db.execSQL(CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SUB);
        onCreate(db);
    }
}
