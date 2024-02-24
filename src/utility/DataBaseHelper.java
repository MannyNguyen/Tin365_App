package utility;

import java.util.ArrayList;

import object.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper 
{ 
	// All Static variables
	// Database Version
	private  final static int DATABASE_VERSION = 1;
	// Database Name
	// Contacts table name
	public  final static String TABLE_Bookmark = "Bookmark";
	// Contacts Table Columns names
	private  final String KEY_ID = "id";
	private  final String userId = "userId";
	private  final String IdArtical = "IdArtical";
	private  final String thumbImg = "thumbImg";
	private  final String title = "title";
	private  final String subtitle = "subtitle";
	private  final String sources = "sources";
	private  final String createDate = "createDate";
	private  final String bookmark_status = "bookmark_status";
	private  final String favorite = "favorite";
	
	int number_page = 20;
	
	//destination path (location) of our database on device 
//	private static String DB_PATH = "";  
	private static String DB_NAME ="Tin365_bookmark";// Database name 
	private final Context mContext; 
	
	
	public DataBaseHelper(Context context)  
	{ 
		super(context, DB_NAME, null, DATABASE_VERSION);// 1? its Database Version 
//		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/"; 
		this.mContext = context; 
	}    



	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BOOKMARK_TABLE = "CREATE TABLE " + TABLE_Bookmark + "("
                + KEY_ID + " INTEGER PRIMARY KEY," +
				userId + " TEXT,"
				+ IdArtical + " TEXT," + thumbImg + " TEXT,"
                + title + " TEXT,"+ subtitle +" TEXT," + sources + " TEXT," +
                createDate + " TEXT," +
                favorite + " TEXT,"   +
                bookmark_status + " TEXT" + ")";
		
        db.execSQL(CREATE_BOOKMARK_TABLE);
        
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS " + TABLE_Bookmark);
	        // Create tables again
	        onCreate(db);
	}
	public void addBookmark(Bookmark bookmark,String table) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(IdArtical, bookmark.getIDNews()); // Contact Name
        values.put(userId, bookmark.getUserID());
        values.put(thumbImg, bookmark.getThumbImg()); 
        values.put(title, bookmark.getTitle()); // 
        values.put(subtitle, bookmark.getSubTitle());
        values.put(sources, bookmark.getSources()); 
        values.put(createDate, bookmark.getCreateDate()); 
        values.put(favorite, bookmark.getFavorite()); 
        values.put(bookmark_status, bookmark.getBookmark_status()); 
        // Inserting Row
        db.insert(table, null, values);
        db.close(); // Closing database connection
    }
	public ArrayList<Bookmark> getAllBookMark(String userid,int size,String table) {
		ArrayList<Bookmark> array_bookmark = new ArrayList<Bookmark>();
	    // Select All Query
		String selectQuery = "SELECT  * FROM " + table  + " order by id desc limit 0 , "+ size*number_page ;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
            do {
	        	Bookmark bookmark = new Bookmark();
	        	bookmark.setIDNews(cursor.getString(cursor.getColumnIndex(IdArtical)));
	        	bookmark.setUserID(cursor.getString(cursor.getColumnIndex(userId)));
	        	bookmark.setThumbImg(cursor.getString(cursor.getColumnIndex(thumbImg)));
	        	bookmark.setTitle(cursor.getString(cursor.getColumnIndex(title)));
	        	bookmark.setSubTitle(cursor.getString(cursor.getColumnIndex(subtitle)));
	        	bookmark.setSources(cursor.getString(cursor.getColumnIndex(sources)));
	        	bookmark.setCreateDate(cursor.getString(cursor.getColumnIndex(createDate)));
	        	bookmark.setFavorite(cursor.getString(cursor.getColumnIndex(favorite)));
	        	bookmark.setBookmark_status(cursor.getString(cursor.getColumnIndex(bookmark_status)));
	            // Adding contact to list
	        	array_bookmark.add(bookmark);
            } while (cursor.moveToNext());
        }
	 
	    // return contact list
	    return array_bookmark;
	}
	public int GetTotalBookMark(String table)
	{
		String selectQuery = "SELECT  * FROM " + table ;
		 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    int size = cursor.getCount();
		return size;
	}
	public boolean deleteOneBookamrk(String IdArtical ,String table)
	{
	    // Select All Query
		String selectQuery = "SELECT  id FROM " + table +" where "+this.IdArtical+" = '" + IdArtical + "'";
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if(cursor.moveToFirst())
	    {
	    	db.delete(table, " id = "+ cursor.getString(cursor.getColumnIndex(KEY_ID)), null);
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	} 
	public boolean checkArticalBookmarkExist(String IdArtical ,String table)
	{
	    // Select All Query
		String selectQuery = "SELECT  id FROM " + table +" where "+ this.IdArtical+" = '" + IdArtical + "'";
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if(cursor.moveToFirst())
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	} 
	public void DeteleAllBookmark(String table)
	{
		SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(table, null, null);
	}
	/*	public boolean checkFavoriteExist(String title_film ,String table)
	{
	    // Select All Query
		String selectQuery = "SELECT  id FROM " + table +" where title = '" + title_film + "'";
		SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    // looping through all rows and adding to list
	    if(cursor.moveToFirst())
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	} 

	
	public void createDataBase() throws IOException 
	{ 
		//If database not exists copy it from the assets 

		boolean mDataBaseExist = checkDataBase(); 
		if(!mDataBaseExist) 
		{ 
			this.getReadableDatabase(); 
			this.close(); 
			try  
			{ 
				//Copy the database from assests 
				copyDataBase(); 
				Log.e(TAG, "createDatabase database created"); 
			}  
			catch (IOException mIOException)  
			{ 
//				throw new Error("ErrorCopyingDataBase"); 
			} 
		} 
	} 
	//Check that the database exists here: /data/data/your package/databases/Da Name 
	private boolean checkDataBase() 
	{ 
		File dbFile = new File(DB_PATH + DB_NAME); 
		//Log.v("dbFile", dbFile + "   "+ dbFile.exists()); 
		return dbFile.exists(); 
	} 

	//Copy the database from assets 
	private void copyDataBase() throws IOException 
	{ 
		InputStream mInput = mContext.getAssets().open(DB_NAME); 
		String outFileName = DB_PATH + DB_NAME; 
		OutputStream mOutput = new FileOutputStream(outFileName); 
		byte[] mBuffer = new byte[1024]; 
		int mLength; 
		while ((mLength = mInput.read(mBuffer))>0) 
		{ 
			mOutput.write(mBuffer, 0, mLength); 
		} 
		mOutput.flush(); 
		mOutput.close(); 
		mInput.close(); 
	} 

	//Open the database, so we can query it 
	public boolean openDataBase() throws SQLException 
	{ 
		String mPath = DB_PATH + DB_NAME; 
		//Log.v("mPath", mPath); 
		mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY); 
		//mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS); 
		return mDataBase != null; 
	} 
*/
	/*@Override 
	public synchronized void close()  
	{ 
		if(mDataBase != null) 
			mDataBase.close(); 
		super.close(); 
	}*/






} 