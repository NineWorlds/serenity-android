/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core.util;

import com.google.analytics.tracking.android.Log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author dcarver
 *
 */
public class SerenityDBHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_EXTERNAL_METADATA = "METADATA";
	public static final String DATABASE_NAME = "serenityDatabase.db";
	public static int VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_PLEX_ID = "PLEX_ID";
	public static final String KEY_YOUTUBE_ID = "YOUTUBE_ID";
	private static final String DATABASE_CREATE = "create table " + TABLE_EXTERNAL_METADATA + 
			"(" + KEY_ID + " integer primary key autoincrement, " +
			KEY_PLEX_ID + " text not null, " +
			KEY_YOUTUBE_ID + " text not null);"; 
			

	/**
	 * {@inheritDoc} 
	 */
	public SerenityDBHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("Upgrading database from version " + oldVersion + " to version " + newVersion);
		db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_EXTERNAL_METADATA);
	}

}
