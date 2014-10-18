package assistente.br.ucb.tcc.assistentereflexivo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper{

    private final static int DB_VERSION = 9;
    private final static String CREATE_QUERY = "create table logins (userId Integer primary key autoincrement, "+
            " username text, password text, name text, birthday date, funcao text)";

    public DBTools(Context context) {

        super(context, "myApp.db", null,DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = CREATE_QUERY;
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try{
            System.out.println("UPGRADE DB oldVersion="+oldVersion+" - newVersion="+newVersion);
            //recreateDb(sqLiteDatabase);
           // if (oldVersion<10){
                String query = CREATE_QUERY;
                sqLiteDatabase.execSQL(query);
           // }
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("DOWNGRADE DB oldVersion="+oldVersion+" - newVersion="+newVersion);
    }

    public User insertUser (User queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", queryValues.getUsername());
        values.put("password", queryValues.getPassword());
        values.put("name", queryValues.getName());
        values.put("birthday",queryValues.getBirthday());
        values.put("funcao",queryValues.getFuncao());
        queryValues.setUserId(database.insert("logins", null, values));
        database.close();
        return queryValues;
    }

    //@TODO Implement "Forget the password"
//    public int updateUserPassword (User queryValues){
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("username", queryValues.username);
//        values.put("password", queryValues.password);
//        queryValues.userId=database.insert("logins", null, values);
//        database.close();
//        return database.update("logins", values, "userId = ?", new String[] {String.valueOf(queryValues.userId)});
//    }

    public User getUser (String username){
        String query = "Select userId, password from logins where username ='"+username+"'";
        User myUser = new User();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                myUser.setUserId(cursor.getLong(0));
                myUser.setPassword(cursor.getString(1));
                myUser.setName(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return myUser;
    }
}