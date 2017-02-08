package buaa.buaahelper;


import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by alan_yang on 2017/1/26.
 */

public class ListScrollListener extends RecyclerView.OnScrollListener {
    private BUAAContentProvider buaaContentProvider;
    boolean isFirst = true;
    public ListScrollListener() {
    }

    public BUAAContentProvider getBuaaContentProvider() {
        return buaaContentProvider;
    }

    public void setBuaaContentProvider(BUAAContentProvider buaaContentProvider) {
        this.buaaContentProvider = buaaContentProvider;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BUAA_RecyclerViewAdapter)
            if (((BUAA_RecyclerViewAdapter) adapter).getProvider() instanceof BUAAContentProvider)
            buaaContentProvider = (BUAAContentProvider) ((BUAA_RecyclerViewAdapter) adapter).getProvider();

        if (!recyclerView.canScrollVertically(-1)) //上拉到顶
        {

           /*  buaaContentProvider.LoadPreData();
            if (isFirst) isFirst=false;
            else
             recyclerView.getAdapter().notifyDataSetChanged();*/
            LoadPreDatatask loadPreDatatask = new LoadPreDatatask();
            loadPreDatatask.execute();
        }
        if(!recyclerView.canScrollVertically(1)) //下拉到底
        {
           // buaaContentProvider.LoadPostData();
           // recyclerView.getAdapter().notifyDataSetChanged();
            LoadPostDatatask loadPostDatatask = new LoadPostDatatask();
            loadPostDatatask.execute();
        }
    }
    /*  下拉到底的样例，也可以在NoticeListFragment里面找到它XD,如果在启动fragment之前未调用setOnScrollListener,则采用DefaultOnScrollListener
     public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);//necessary
                int times = 0;
                RecyclerView.Adapter<BUAA_RecyclerViewAdapter.ListItemViewHolder> mAdapter= recyclerView.getAdapter();//necessary
                if (!recyclerView.canScrollVertically(1))//necessary
                {
                    for (int i = 1; i <= 10;i++)
                    {
                        ContentProvider.ITEMS.add(new CommonItemForList("new "+ContentProvider.ITEMS.size()+" times: "+times,"http://",new Date()));
                    }
                    times++;
                    mAdapter.notifyDataSetChanged();//necessary
                }
            }
     */
    class LoadPreDatatask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            buaaContentProvider.LoadPreData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isFirst) isFirst=false;
            else
                buaaContentProvider.getBuaa_recyclerViewAdapter().notifyDataSetChanged();
        }
    }

    class LoadPostDatatask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            buaaContentProvider.LoadPostData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // if (isFirst) isFirst=false;
            //else
                buaaContentProvider.getBuaa_recyclerViewAdapter().notifyDataSetChanged();
        }
    }
}
