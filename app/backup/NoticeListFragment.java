package buaa.buaahelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NoticeListFragment extends Fragment implements BUAA_RecyclerViewAdapter.OnClickListener,BUAA_RecyclerViewAdapter.GarbageCollectioner {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private BUAA_RecyclerViewAdapter buaa_recyclerViewAdapter;
    private RecyclerView.OnScrollListener onScrollListener;
    private RecyclerView recyclerView;
    private ContentProvider provider,trash;
    private ItemTouchHelper itemTouchHelper;
    private BUAAItemTouchHelperCallback buaaItemTouchHelperCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticeListFragment newInstance(int columnCount) {
        NoticeListFragment fragment = new NoticeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.NoticeList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        //provider = new ContentProvider();


        if (buaa_recyclerViewAdapter == null)
            buaa_recyclerViewAdapter = new BUAA_RecyclerViewAdapter(provider);
        buaa_recyclerViewAdapter.setOnClickListener(this);
        ((BUAAContentProvider) provider).setBuaa_recyclerViewAdapter(buaa_recyclerViewAdapter);
        buaaItemTouchHelperCallback = new BUAAItemTouchHelperCallback(buaa_recyclerViewAdapter);
        // buaaItemTouchHelperCallback.setMadapter(buaa_recyclerViewAdapter);
        itemTouchHelper = new ItemTouchHelper(buaaItemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(buaa_recyclerViewAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (onScrollListener == null)
            recyclerView.addOnScrollListener(new DefaultOnScrollListener());
        else
            recyclerView.addOnScrollListener(onScrollListener);
        /*DividerDecoration divider = new DividerDecoration.Builder(getContext(),mLrecyclerviewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
        mRecyclerView.addItemDecoration(divider);*/
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
            if (buaa_recyclerViewAdapter != null)
                buaa_recyclerViewAdapter.setOnListFragmentInteractionListener(mListener);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BUAAContentProvider)provider).updateNotifications();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction(CommonItemForList item);
    }

    @Override
    public void OnItemClick(CommonItemForList itemForList,int pos) {
       // BUAA_RecyclerViewAdapter.ListItemViewHolder viewHolder = (BUAA_RecyclerViewAdapter.ListItemViewHolder)(recyclerView.getChildViewHolder(view));
       // CommonItemForList itemForList = viewHolder.getItemForList();
        //Bundle bundle = new Bundle();
       // bundle.putInt("ID",itemForList.getId() );
        //bundle.putInt();

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("ID",itemForList.getId() );
        intent.putExtra("Position",pos);
        // if (itemForList.Detail != null)
        //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
        //Toast.makeText(getActivity(),"Position:"+pos,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnDeleteBtnClick(int pos) {
        //int pos = recyclerView.getChildAdapterPosition(view);
        buaa_recyclerViewAdapter.RemoveData(pos);
        buaa_recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFavoriteBtnClick(int pos) {

    }

    @Override
    public void OnItemClick(int pos) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        //intent.putExtra("ID",itemForList.getId() );
        intent.putExtra("Position",pos);
       // if (itemForList.Detail != null)
       //     intent.putExtra("Detail",itemForList.Detail.toString());
        // intent.putExtra("Adapter",  buaa_recyclerViewAdapter);
        startActivity(intent);
    }

    @Override
    public void OnDataRemoved(CommonItemForList itemForList,int position) {
        //TODO:删除item的接口已经暴露,position表示待删除元素在ITEMS数组中的index，itemForList是待删除元素的详细内容
        if (trash instanceof BUAAContentProvider) {
            //得到type为TRASH的ContentProvider
            BUAAContentProvider garbageCollector = (BUAAContentProvider)trash;
            //这是目前默认的删除方式，记得保留这几行
            if (provider instanceof BUAAContentProvider)
            {
                ((BUAAContentProvider)provider).deleteDataInList(position);
            }
        }
    }

    public void setProvider(ContentProvider provider) {
        this.provider = provider;
    }

    public void setTrash(ContentProvider trash) {
        this.trash = trash;
    }
}
