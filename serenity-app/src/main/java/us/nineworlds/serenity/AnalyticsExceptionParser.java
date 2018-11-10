package us.nineworlds.serenity;

import com.google.android.gms.analytics.ExceptionParser;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AnalyticsExceptionParser implements ExceptionParser {

  @Override public String getDescription(String p_thread, Throwable p_throwable) {
    return "Thread: " + p_thread + ", Exception: " + getStackTrace(p_throwable);
  }

  public String getStackTrace(final Throwable throwable) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw, true);
    throwable.printStackTrace(pw);
    return sw.getBuffer().toString();
  }
}