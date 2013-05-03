package datx02.group15.database;

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
public class DataAccessObject {
	
	private SQLiteDatabase database;
	private CustomSQLiteOpenHelper dbHelper;
	
	/**
	 * Empty Constructor
	 * @param context for example this
	 */
	public DataAccessObject(Context context){
		dbHelper = new CustomSQLiteOpenHelper(context);
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
			database.execSQL("INSERT INTO " + CustomSQLiteOpenHelper.DATABASE_TABLE_NAME + " VALUES ('" + lockID + "', '" + unlockID + "')");
		}else{
			database.execSQL("UPDATE " + CustomSQLiteOpenHelper.DATABASE_TABLE_NAME + " SET " + CustomSQLiteOpenHelper.COLUMN_2 + " = '" + unlockID + "' WHERE " +
					CustomSQLiteOpenHelper.COLUMN_1 + " = '" + lockID + "'");
		}
	 }

	 /**Not needed => Not tested yet
	  * 
	  * @param lockID
	  */
	 public void delete(String lockID) {
	    database.execSQL("DELETE FROM " + CustomSQLiteOpenHelper.DATABASE_TABLE_NAME + " WHERE " + CustomSQLiteOpenHelper.COLUMN_1 + " = '" + lockID + "'");
	  }
	 
	 /**
	  * Queries the database for the given lockID
	  * @param lockId to serch for
	  * @return the found unlockId 
	  */
	 public String get(String lockId) {
		 String select = "SELECT " + CustomSQLiteOpenHelper.COLUMN_2 + " FROM " + CustomSQLiteOpenHelper.DATABASE_TABLE_NAME + " WHERE " + CustomSQLiteOpenHelper.COLUMN_1 + " = '" + lockId + "'";
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
		 String selectAll = "SELECT * FROM " + CustomSQLiteOpenHelper.DATABASE_TABLE_NAME + " ORDER BY " + CustomSQLiteOpenHelper.COLUMN_1 + " COLLATE NOCASE";
		 Cursor c = database.rawQuery(selectAll,null);
		 if(c.moveToFirst()){
			 map.put(c.getString(0), c.getString(1));
			 while(c.moveToNext()){
				map.put(c.getString(0), c.getString(1));
			 }
		 }
		 return map;
	 }
}
