package de.patri.ck.clipperdbx.common.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerSpace extends RecyclerView.ItemDecoration {

  int space;
  int spanCount;

  public RecyclerSpace(int space, int spanCount) {
    this.space = space;
    this.spanCount = spanCount;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.top    = space;
    outRect.right  = space;
    outRect.left   = space;
    outRect.bottom = space;
  }

}