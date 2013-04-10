package kandidat.nfc.nfcapp;

import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
/**
 * Data Access object. Like a layer on top of the Database
 * 
 * @author Fredrik
 *
 */
public class DAO {
	
	private SQLiteDatabase database;
	private DBHelper dbHelper;
	
	/**
	 * Empty Constructor
	 * @param context for example this
	 */
	public DAO(Context context){
		dbHelper = new DBHelper(context);
	}
	
	/**
	 * Needs to be called before any other operation on the database is performed.
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Call this when you are done modifying the database.
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Checks if lockId is found in database. If it is update. If not insert.
	 * @param lockID
	 * @param unlockID
	 */
	public void insertOrUpdate(String lockID, String unlockID) {
		String isUpdate = get(lockID);
		if(isUpdate == null){
			database.execSQL("INSERT INTO " + DBHelper.DATABASE_TABLE_NAME + " VALUES ('" + lockID + "', '" + unlockID + "')");
		}else{
			database.execSQL("UPDATE " + DBHelper.DATABASE_TABLE_NAME + " SET " + DBHelper.COLUMN_2 + " = '" + unlockID + "' WHERE " +
					DBHelper.COLUMN_1 + " = '" + lockID + "'");
		}
	 }

	 /**Not needed => Not tested yet
	  * 
	  * @param lockID
	  */
	 public void delete(String lockID) {
	    database.execSQL("DELETE FROM " + DBHelper.DATABASE_TABLE_NAME + " WHERE " + DBHelper.COLUMN_1 + " = '" + lockID + "'");
	  }
	 
	 /**
	  * Queries the database for the given lockID
	  * @param lockId to serch for
	  * @return the found unlockId 
	  */
	 public String get(String lockId) {
		 String select = "SELECT " + DBHelper.COLUMN_2 + " FROM " + DBHelper.DATABASE_TABLE_NAME + " WHERE " + DBHelper.COLUMN_1 + " = '" + lockId + "'";
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
	 
	 public Map<String,String> getAll(){
		 Map<String,String> map = new TreeMap<String,String>();
		 String selectAll = "SELECT " + DBHelper.COLUMN_2 + " FROM " + DBHelper.DATABASE_TABLE_NAME;
		 Cursor c = database.rawQuery(selectAll,null);
		 if(c.getCount() != 0){
			 c.moveToFirst();
			 map.put(c.getString(0), c.getString(1));
			 while(c.getCount() > 0){
				c.moveToNext();
				map.put(c.getString(0), c.getString(1));
			 }
		 }
		 return map;
	 }
}
