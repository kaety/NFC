package kandidat.nfc.nfcapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DAO {
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	private String[] allColumns = {DBHelper.COLUMN_1,DBHelper.COLUMN_2};
	
	public DAO(Context context){
		dbHelper = new DBHelper(context);
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	

	public void insert(String lockID, String unlockID) {
		String isUpdate = get(lockID);
		if(isUpdate == null){
			database.execSQL("INSERT INTO " + DBHelper.DATABASE_TABLE_NAME + " VALUES ('" + lockID + "', '" + unlockID + "')");
		}else{
			//TODO update 
		}
	 }

	 public void delete(String lockID) {
		//database.beginTransaction();
	    database.execSQL("DELETE FROM " + DBHelper.DATABASE_TABLE_NAME + " WHERE " + DBHelper.COLUMN_1 + " = " + lockID);
		//database.endTransaction();
	  }
	 
	 public String get(String lockID) {
		 String select = "SELECT " + DBHelper.COLUMN_2 + " FROM " + DBHelper.DATABASE_TABLE_NAME + " WHERE " + DBHelper.COLUMN_1 + " = '" + lockID + "'";
		 Cursor c = database.rawQuery(select, null);
		 if(c.getCount() == 0){
			 return null;
		 }
		 try{
			 c.moveToFirst();
			 String result = c.getString(0);
			 c.close();
			 return result;
		 }catch(Exception e){
			 return null;
		 }
	 }
	 
	 public void modify(String lockID, String unlockID){
		 
	 }
}