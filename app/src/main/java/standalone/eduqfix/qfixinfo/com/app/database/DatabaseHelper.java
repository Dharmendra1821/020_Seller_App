package standalone.eduqfix.qfixinfo.com.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLENAME = "student";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String AGE = "age";

    private static final String DATABASE = "database";
    private static final int VERSION = 1;


    private static final String CREATETABLE = "CREATE TABLE "+ TABLENAME +"("+ID
            + " INTEGER ,"+NAME+ " TEXT ,"+ADDRESS+ " TEXT ,"+
            AGE +" TEXT );";



    public DatabaseHelper(Context context) {
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
