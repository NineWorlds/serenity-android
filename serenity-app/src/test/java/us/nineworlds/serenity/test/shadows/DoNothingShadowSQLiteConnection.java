package us.nineworlds.serenity.test.shadows;

import android.database.sqlite.SQLiteConnection;
import android.database.sqlite.SQLiteCustomFunction;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.Resetter;

/**
 * A shadow of SQLiteConnection that does absolutely nothing.
 */
@Implements(value = android.database.sqlite.SQLiteConnection.class, isInAndroidSdk = false)
public class DoNothingShadowSQLiteConnection {

  public DoNothingShadowSQLiteConnection() {
  }

  @Implementation public static SQLiteConnection open() {
    return new SQLiteConnection();
  }

  @Implementation public static long nativeOpen(String path, int openFlags, String label, boolean enableTrace, boolean enableProfile) {
    return 0;
  }

  @Implementation public static long nativePrepareStatement(long connectionPtr, String sql) {
    return 0;
  }

  @Resetter public static void reset() {

  }

  @Implementation public static void nativeClose(long connectionPtr) {

  }

  @Implementation public static void nativeFinalizeStatement(long connectionPtr, long statementPtr) {

  }

  @Implementation public static int nativeGetParameterCount(final long connectionPtr, final long statementPtr) {
    return 0;
  }

  @Implementation public static boolean nativeIsReadOnly(final long connectionPtr, final long statementPtr) {
    return true;
  }

  @Implementation public static long nativeExecuteForLong(final long connectionPtr, final long statementPtr) {
    return -1;
  }

  @Implementation public static void nativeExecute(final long connectionPtr, final long statementPtr) {

  }

  @Implementation public static String nativeExecuteForString(final long connectionPtr, final long statementPtr) {
    return null;
  }

  @Implementation public static int nativeGetColumnCount(final long connectionPtr, final long statementPtr) {
    return 0;
  }

  @Implementation public static String nativeGetColumnName(final long connectionPtr, final long statementPtr, final int index) {
    return null;
  }

  @Implementation public static void nativeBindNull(final long connectionPtr, final long statementPtr, final int index) {
  }

  @Implementation public static void nativeBindLong(final long connectionPtr, final long statementPtr, final int index, final long value) {
  }

  @Implementation public static void nativeBindDouble(final long connectionPtr, final long statementPtr, final int index, final double value) {
  }

  @Implementation public static void nativeBindString(final long connectionPtr, final long statementPtr, final int index, final String value) {
  }

  @Implementation public static void nativeBindBlob(final long connectionPtr, final long statementPtr, final int index, final byte[] value) {
  }

  @Implementation public static void nativeRegisterLocalizedCollators(long connectionPtr, String locale) {
  }

  @Implementation public static int nativeExecuteForChangedRowCount(final long connectionPtr, final long statementPtr) {
    return -0;
  }

  @Implementation public static long nativeExecuteForLastInsertedRowId(final long connectionPtr, final long statementPtr) {
    return -0;
  }

  @Implementation public static long nativeExecuteForCursorWindow(
      final long connectionPtr, final long statementPtr, final long windowPtr, int startPos, int requiredPos, boolean countAllRows) {
    return -0;
  }

  @Implementation public static void nativeResetStatementAndClearBindings(final long connectionPtr, final long statementPtr) {

  }

  @Implementation public static void nativeCancel(long connectionPtr) {
  }

  @Implementation public static void nativeResetCancel(long connectionPtr, boolean cancelable) {
  }

  @Implementation public static void nativeRegisterCustomFunction(long connectionPtr, SQLiteCustomFunction function) {
  }

  @Implementation public static int nativeExecuteForBlobFileDescriptor(long connectionPtr, long statementPtr) {
    return -1;
  }

  @Implementation public static int nativeGetDbLookaside(long connectionPtr) {
    return -1;
  }
}
