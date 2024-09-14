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
        // 获取当前项的位置
        int position = parent.getChildAdapterPosition(view);

        // 设置左边距
        outRect.left = space / 2;
        // 设置右边距
        outRect.right = space / 2;
        // 设置顶部边距
        outRect.top = space;
        // 设置底部边距
        // 如果不是最后一项，则添加底部边距
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = 0; // 最后一项不设底部边距
        } else {
            outRect.bottom = space;
        }
    }
}
