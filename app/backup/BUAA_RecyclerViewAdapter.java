package buaa.buaahelper;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
/**
 * Created by alan_yang on 2017/1/21.
 */

public class BUAA_RecyclerViewAdapter extends RecyclerView.Adapter<BUAA_RecyclerViewAdapter.ListItemViewHolder> {


    private  List<CommonItemForList> items;
    private  NoticeListFragment.OnListFragmentInteractionListener Listener;
    private OnClickListener onClickListener;
    private ContentProvider provider;
    private GarbageCollectioner garbageCollectioner;
    BUAA_RecyclerViewAdapter(ContentProvider provider) {

        if (provider == null ) {
            throw new IllegalArgumentException(
                    "Data must not be null");
        }
        this.provider = provider;
        this.items = provider.getDataList();
        this.Listener = null;
    }

    public GarbageCollectioner getGarbageCollectioner() {
        return garbageCollectioner;
    }

    public void setGarbageCollectioner(GarbageCollectioner garbageCollectioner) {
        this.garbageCollectioner = garbageCollectioner;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public NoticeListFragment.OnListFragmentInteractionListener getListener() {
        return Listener;
    }

    public void setListener(NoticeListFragment.OnListFragmentInteractionListener listener) {
        Listener = listener;
    }

    public ContentProvider getProvider() {
        return provider;
    }

    public void setProvider(ContentProvider provider) {
        this.provider = provider;
    }

    public void setOnListFragmentInteractionListener(NoticeListFragment.OnListFragmentInteractionListener mlistener)
    {
            this.Listener = mlistener;
    }
    @Override
    public ListItemViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_demo,
                        viewGroup,
                        false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            final ListItemViewHolder viewHolder, int position) {
        viewHolder.itemForList = items.get(position);
        viewHolder.label.setText(viewHolder.itemForList.label);

        String dateStr = DateUtils.formatDateTime(
                viewHolder.label.getContext(),
                viewHolder.itemForList.ReceiveTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);
        viewHolder.dateTime.setText(dateStr);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

     class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView label;
         TextView dateTime;
         ImageView icon;
         View view;
         CommonItemForList itemForList;
         RelativeLayout mItemLayout;
         LinearLayout ContainerLayout,mSwipeMenuLayout;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.container_inner_item);
//            view.setOnClickListener(this);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
            dateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
            icon = (ImageView) itemView.findViewById(R.id.image_noice_icon);
            mItemLayout = (RelativeLayout) itemView.findViewById(R.id.ListItemLayout);
            //mItemLayout.setOnClickListener(this);
            mSwipeMenuLayout = (LinearLayout) itemView.findViewById(R.id.SwipeMenuLayout);
            ContainerLayout = (LinearLayout) itemView.findViewById(R.id.container_inner_item);
            ((Button)itemView.findViewById(R.id.SwipeButtonDelete)).setOnClickListener(this);
            ContainerLayout.setOnClickListener(this);  //TAKE HEED OF THAT EVEN THE BACKGROUND COLOR OF THE CHILD VIEW WILL COVER THE FATHER VIEW, THE CLICK EVENT WILL BE CAPTURED FIRST BY FATHER
        }
         @Override
         public void onClick(View view) {
            // if (onClickListener != null)
             if (view.getId() == R.id.container_inner_item) {
                 int pos = getAdapterPosition();
                 label.setTextColor(Color.GRAY);

                 //onClickListener.OnItemClick(getAdapterPosition());
          /*       if (pos>=0)
                     onClickListener.OnItemClick(items.get(pos),pos);
                 else
                 {
                     notifyDataSetChanged();
                     pos = getAdapterPosition();
                     onClickListener.OnItemClick(items.get(pos),pos);
                 }*/
                 onClickListener.OnItemClick(items.get(pos),pos);
             }
             if (view.getId() == R.id.SwipeButtonDelete)
             {
                 onClickListener.OnDeleteBtnClick(getAdapterPosition());
             }
             if (view.getId() == R.id.SwipeButtonFav)
             {
                 onClickListener.OnFavoriteBtnClick(getAdapterPosition());
             }
         }
         public TextView getLabel() {
             return label;
         }

         public TextView getDateTime() {
             return dateTime;
         }

         public ImageView getIcon() {
             return icon;
         }

         public CommonItemForList getItemForList() {
             return itemForList;
         }

         public View getView() {
             return view;
         }

         public RelativeLayout getmItemLayout() {
             return mItemLayout;
         }

         public LinearLayout getmSwipeMenuLayout() {
             return mSwipeMenuLayout;
         }

         public LinearLayout getContainerLayout() {
             return ContainerLayout;
         }
     }
     public void RemoveData(int position)
     {
         if (garbageCollectioner!=null)
         {
             garbageCollectioner.OnDataRemoved(items.get(position),position);
         }
     }
    public void Update(int p)
    {
        items.clear();
        items = provider.getDataList();
    }
     public interface OnClickListener
     {
         public void OnItemClick(CommonItemForList itemForList,int pos);
         public void OnItemClick(int pos);
         public void OnDeleteBtnClick(int pos);
         public void OnFavoriteBtnClick(int pos);
     }
     public interface GarbageCollectioner
     {
         public void OnDataRemoved(CommonItemForList itemForList,int position);
     }

}
