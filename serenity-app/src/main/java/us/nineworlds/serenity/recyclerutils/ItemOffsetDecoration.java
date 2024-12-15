package us.nineworlds.serenity.recyclerutils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

  private int mItemOffset;

  public ItemOffsetDecoration(int itemOffset) {
    mItemOffset = itemOffset;
  }

  public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
    this(context.getResources().getDimensionPixelSize(itemOffsetId));
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
  }
}