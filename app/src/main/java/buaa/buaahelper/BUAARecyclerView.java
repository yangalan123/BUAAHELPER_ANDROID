package buaa.buaahelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.animation.AnimatorSet;
import android.widget.Scroller;

/**
 * Created by alan_yang on 2017/1/30.
 */
//unfinished,but it will be finished later
public class BUAARecyclerView extends RecyclerView {
    private Rect mTouchFrame;
    private int pos, mFirstPosition, mStartX;
    private RelativeLayout itemLayout;
    private int MaxLength, mTouchSlop;
    private boolean isFirst;
    private Scroller mScroller = new Scroller(getContext(), new LinearInterpolator(getContext(), null));
    private LinearLayout SwipeMenu;

    public BUAARecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BUAARecyclerView(Context context) {
        super(context);
    }

    public BUAARecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //通过点击的坐标计算当前的position
                mStartX = x;
                mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                Rect frame = mTouchFrame;
                if (frame == null) {
                    mTouchFrame = new Rect();
                    frame = mTouchFrame;
                }
                final int count = getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() == View.VISIBLE) {
                        child.getHitRect(frame);
                        if (frame.contains(x, y)) {
                            pos = mFirstPosition + i;
                        }
                    }
                }
                View view = getChildAt(pos - mFirstPosition);
                BUAA_RecyclerViewAdapter.ListItemViewHolder viewHolder = (BUAA_RecyclerViewAdapter.ListItemViewHolder) getChildViewHolder(view);
                SwipeMenu = viewHolder.getmSwipeMenuLayout();
                itemLayout = viewHolder.mItemLayout;
                MaxLength = viewHolder.getmSwipeMenuLayout().getWidth();
                mTouchSlop = MaxLength / 4;
            }
            break;
            case MotionEvent.ACTION_MOVE: {


                int scrollX = itemLayout.getScrollX();
                int newScrollX = mStartX - x;
                if (newScrollX < 0) newScrollX = 0;
                if (newScrollX < -MaxLength) newScrollX = -MaxLength;
                if (scrollX < 0 && scrollX >= -MaxLength) {
                    if (isFirst) {
                        ObjectAnimator animatorX = ObjectAnimator.ofFloat(SwipeMenu, "scaleX", 1f, 1.2f, 1f);
                        ObjectAnimator animatorY = ObjectAnimator.ofFloat(SwipeMenu, "scaleY", 1f, 1.2f, 1f);
                        AnimatorSet animSet = new AnimatorSet();
                        animSet.play(animatorX).with(animatorY);
                        animSet.setDuration(800);
                        animSet.start();
                        isFirst = false;
                    }
                }
                itemLayout.scrollBy(newScrollX, 0);
            }
            break;
            case MotionEvent.ACTION_UP: {


                int xUp = x;
                int yUp = y;
                int dx = xUp - mStartX;
                if (Math.abs(dx) < mTouchSlop) {
                    //geton.getPosition(pos);

                } else {
                    int scrollX = itemLayout.getScrollX();
                    if (scrollX > MaxLength / 2) {
                        //       ((RecyclerAdapter) getAdapter()).removeRecycle(pos);
                    } else {
                        mScroller.startScroll(scrollX, 0, -scrollX, 0);
                        invalidate();
                    }
                    isFirst = true;
                }
            }


            break;
        }
        return true;
    }
}
