package us.nineworlds.serenity;

import com.google.android.gms.analytics.ExceptionParser;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class AnalyticsExceptionParser implements ExceptionParser {
  /*
   * (non-Javadoc)
   *
   * @see
   * com.google.analytics.tracking.android.ExceptionParser#getDescription(
   * java.lang.String, java.lang.Throwable)
   */
  @Override public String getDescription(String p_thread, Throwable p_throwable) {
    return "Thread: " + p_thread + ", Exception: " + ExceptionUtils.getStackTrace(p_throwable);
  }
}