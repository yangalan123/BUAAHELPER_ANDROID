package buaa.buaahelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by alan_yang on 2017/1/29.
 */

public class BUAAItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private BUAA_RecyclerViewAdapter madapter;

    public BUAAItemTouchHelperCallback(BUAA_RecyclerViewAdapter madapter) {
        this.madapter = madapter;

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT ) {
            madapter.RemoveData(viewHolder.getAdapterPosition());
            madapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags =  ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

}

