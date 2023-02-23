package cat.dam.andy.sqlite_recyclerview.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cat.dam.andy.sqlite_recyclerview.Item;


public class DatabaseHelper extends SQLiteOpenHelper {

    private	static final int DB_VERSION =	7;
    private	static final String DB_NAME = "Contacts";
    private	static final String TABLE_NAME = "contacts";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_PHONE + " INTEGER" + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Item> listAll(){
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Item> items = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String nom = cursor.getString(1);
                String tel = cursor.getString(2);
                items.add(new Item(id, nom, tel));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void addContact(Item item){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PHONE, item.getTel());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    public void updateContact(Item item){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_PHONE, item.getTel());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COLUMN_ID + "	= ?", new String[] { String.valueOf(item.getId())});
    }

    public Item findContact(String name){
        String query = "Select * FROM "	+ TABLE_NAME + " WHERE " + COLUMN_NAME + " = " + name;
        SQLiteDatabase db = this.getWritableDatabase();
        Item item = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String contactName = cursor.getString(1);
            String contactPhone = cursor.getString(2);
            item = new Item(id, contactName, contactPhone);
        }
        cursor.close();
        return item;
    }

    public void removeContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "	= ?", new String[] { String.valueOf(id)});
    }
}
