package buaa.buaahelper;

import android.support.v7.widget.RecyclerView;

import java.util.Date;

/**
 * Created by alan_yang on 2017/1/26.
 */

public class DefaultOnScrollListener extends RecyclerView.OnScrollListener {
    private int times = 0;
    private ContentProvider provider;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.Adapter<BUAA_RecyclerViewAdapter.ListItemViewHolder> mAdapter= recyclerView.getAdapter();
        if (mAdapter instanceof  BUAA_RecyclerViewAdapter)
         provider = ((BUAA_RecyclerViewAdapter)mAdapter).getProvider();
        if (!recyclerView.canScrollVertically(1))   //下拉示例
        {
            for (int i = 1; i <= 10;i++)
            {
                provider.addItem(new CommonItemForList(0,"new "+ provider.getDataSize()+" times: "+times,"http://",new Date()));
            }
            times++;
            mAdapter.notifyDataSetChanged();
        }
    }
}
