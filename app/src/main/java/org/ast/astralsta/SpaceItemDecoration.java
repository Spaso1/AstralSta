package org.ast.astralsta;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

// 自定义SpaceItemDecoration类
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 只对最后一个item应用底部间距
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = space;
        }
    }
}
