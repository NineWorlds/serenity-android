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

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.core.model.DBMetaData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author dcarver
 * 
 */
public class DBMetaDataSource {

	private SQLiteDatabase database;
	private SerenityDBHelper dbHelper;
	private String[] allColumsn = { SerenityDBHelper.KEY_ID,
			SerenityDBHelper.KEY_PLEX_ID, SerenityDBHelper.KEY_YOUTUBE_ID };

	/**
	 * 
	 */
	public DBMetaDataSource(Context context) {
		dbHelper = new SerenityDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void createMetaData(String youTubeId, String plexId) {
		ContentValues values = new ContentValues();
		values.put(SerenityDBHelper.KEY_YOUTUBE_ID, youTubeId);
		values.put(SerenityDBHelper.KEY_PLEX_ID, plexId);

		long insertId = database.insert(
				SerenityDBHelper.TABLE_EXTERNAL_METADATA, null, values);
		Cursor cursor = database.query(
				SerenityDBHelper.TABLE_EXTERNAL_METADATA, allColumsn,
				SerenityDBHelper.KEY_ID + " = " + insertId, null, null, null,
				null);
	}

	public void deleteMetaData(DBMetaData metaData) {
		long id = metaData.getId();
		database.delete(SerenityDBHelper.TABLE_EXTERNAL_METADATA,
				SerenityDBHelper.KEY_ID + " = " + id, null);
	}

	public List<DBMetaData> getAllMetaData() {
		List<DBMetaData> metaDatas = new ArrayList<DBMetaData>();

		Cursor cursor = database.query(
				SerenityDBHelper.TABLE_EXTERNAL_METADATA, allColumsn, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DBMetaData metaData = cursorToMetaData(cursor);
			metaDatas.add(metaData);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return metaDatas;
	}

	private DBMetaData cursorToMetaData(Cursor cursor) {
		DBMetaData metaData = new DBMetaData();
		metaData.setId(cursor.getLong(0));
		metaData.setPlexId(cursor.getString(1));
		metaData.setYouTubeID(cursor.getString(2));

		return metaData;
	}
	
	public DBMetaData findMetaDataByPlexId(String plexId) {
		Cursor cursor = database.query(SerenityDBHelper.TABLE_EXTERNAL_METADATA, allColumsn, SerenityDBHelper.KEY_PLEX_ID + " = " + plexId, null, null, null, null, null);
		cursor.moveToFirst();
		DBMetaData metaData = null;
		if (!cursor.isAfterLast()) {
			metaData = cursorToMetaData(cursor);
		}
		cursor.close();
		return metaData;
	}
}
