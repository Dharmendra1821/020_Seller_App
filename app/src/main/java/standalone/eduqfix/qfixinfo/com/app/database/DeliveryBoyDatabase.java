package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DeliveryBoyDatabase extends SQLiteOpenHelper {

    public static final String TABLENAME = "deliveryboy";

    public static final String ID = "id";
    public static final String DELIVERYBOYENTITY = "entity";
    public static final String DELIVERYBOYFIRST= "firstname";
    public static final String DELIVERYBOYLAST= "lastname";
    public static final String DELIVERYBOYEMAIL = "email";
    public static final String DELIVERYBOYCONTACT = "contact";
    public static final String DELIVERYBOYSTATUS = "status";


    public static final String DATABASE = "DELIVERYBOY.DB";
    public static final int VERSION = 1;


    public static final String CREATETABLE = "CREATE TABLE "+ TABLENAME +"("+ID
            + " INTEGER ,"+ DELIVERYBOYENTITY + " TEXT ,"+ DELIVERYBOYEMAIL + " TEXT ,"+
            DELIVERYBOYFIRST + " TEXT ,"+
            DELIVERYBOYLAST + " TEXT ,"+
            DELIVERYBOYCONTACT + " TEXT ,"+
            DELIVERYBOYSTATUS +" TEXT );";



    public DeliveryBoyDatabase(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATETABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
