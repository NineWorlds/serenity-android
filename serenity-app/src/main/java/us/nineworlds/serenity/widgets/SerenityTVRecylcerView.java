package us.nineworlds.serenity.widgets;

import android.content.Context;
import android.util.AttributeSet;
import app.com.tvrecyclerview.TvRecyclerView;
import com.google.android.flexbox.FlexboxLayoutManager;

public class SerenityTVRecylcerView extends TvRecyclerView {

  public SerenityTVRecylcerView(Context context) {
    super(context);
  }

  public SerenityTVRecylcerView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SerenityTVRecylcerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override public int getFirstVisiblePosition() {
    LayoutManager layoutManager = this.getLayoutManager();
    if (layoutManager instanceof FlexboxLayoutManager) {
      return ((FlexboxLayoutManager) layoutManager).findFirstVisibleItemPosition();
    }

    return super.getFirstVisiblePosition();
  }

  @Override public int getLastVisiblePosition() {
    LayoutManager layoutManager = this.getLayoutManager();
    if (layoutManager instanceof FlexboxLayoutManager) {
      return ((FlexboxLayoutManager) layoutManager).findLastVisibleItemPosition();
    }

    return super.getLastVisiblePosition();
  }
}
