package org.ast.astralsta;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static org.ast.astralsta.MainActivity.update;

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "RecyclerViewScrollListener";
    private LinearLayoutManager layoutManager;
    private OnBottomReachedListener bottomReachedListener;

    public interface OnBottomReachedListener {
        void onBottomReached();
    }

    public void setOnBottomReachedListener(OnBottomReachedListener listener) {
        this.bottomReachedListener = listener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (layoutManager == null) {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }

        if (layoutManager != null) {
            int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            int itemCount = layoutManager.getItemCount();

            // 检测是否滑动到了最后一个item
            if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == (itemCount - 1)) {
                Log.i(TAG, "滑动到底部");
                if (bottomReachedListener != null) {
                    bottomReachedListener.onBottomReached();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:

                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:

                break;
            case RecyclerView.SCROLL_STATE_SETTLING:

                break;
        }
    }
}
