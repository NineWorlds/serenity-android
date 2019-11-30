package us.nineworlds.serenity.recyclerutils;

import android.graphics.Rect;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
  private static final boolean DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM = false;
  private static final boolean DEFAULT_ADD_SPACE_BELOW_LAST_ITEM = false;

  private final int space;
  private final boolean addSpaceAboveFirstItem;
  private final boolean addSpaceBelowLastItem;

  public SpaceItemDecoration(int space) {
    this(space, DEFAULT_ADD_SPACE_ABOVE_FIRST_ITEM, DEFAULT_ADD_SPACE_BELOW_LAST_ITEM);
  }

  public SpaceItemDecoration(int space, boolean addSpaceAboveFirstItem,
      boolean addSpaceBelowLastItem) {
    this.space = space;
    this.addSpaceAboveFirstItem = addSpaceAboveFirstItem;
    this.addSpaceBelowLastItem = addSpaceBelowLastItem;
  }

  @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    if (space <= 0) {
      return;
    }

    if (addSpaceAboveFirstItem && parent.getChildLayoutPosition(view) < 1
        || parent.getChildLayoutPosition(view) >= 1) {
      if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
        outRect.top = space;
      } else {
        outRect.left = space;
      }
    }

    if (addSpaceBelowLastItem
        && parent.getChildAdapterPosition(view) == getTotalItemCount(parent) - 1) {
      if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
        outRect.bottom = space;
      } else {
        outRect.right = space;
      }
    }
  }

  private int getTotalItemCount(RecyclerView parent) {
    return parent.getAdapter().getItemCount();
  }

  private int getOrientation(RecyclerView parent) {
    if (parent.getLayoutManager() instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
    } else {
      throw new IllegalStateException(
          "SpaceItemDecoration can only be used with a LinearLayoutManager.");
    }
  }
}