package com.example.xingw.mygankapp.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xingw.mygankapp.Adapter.BenefitGoodsItemAdapter;
import com.example.xingw.mygankapp.Model.Goods;
import com.example.xingw.mygankapp.Model.GoodsResult;
import com.example.xingw.mygankapp.Config.Constants;
import com.example.xingw.mygankapp.Database.Image;
import com.example.xingw.mygankapp.Network.GankCloudApi;
import com.example.xingw.mygankapp.R;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Observer;

/**
 * Created by Xingw on 2015/11/30.
 */
public class BenefitListFragment extends BaseLoadingFragment implements SwipeRefreshLayout.OnRefreshListener,
        RealmChangeListener {

    @Bind(R.id.benefit_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.benefit_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<Image> mAllbenefitImage;
    private BenefitGoodsItemAdapter mBenefitItemAdapter;
    private UpdateResultReceiver updateResultReceiver = new UpdateResultReceiver();
    private Realm mRealm;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    //是否正在更新图片信息
    private boolean bImproveDoing = false;
    private boolean isAllLoad = false;
    private int hasLoadPage = Constants.HAS_LOAD_PAGE_NOT;
    private boolean isLoadMore = false;

    private Observer<GoodsResult> getBenefitGoodsObserver=new Observer<GoodsResult>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(GoodsResult goodsResult) {
            if(mAllbenefitImage.isEmpty() && goodsResult.getResults().isEmpty()){
                showNoDataView();
                return;
            }
            showContent();
            if(goodsResult.getResults().size()== GankCloudApi.LOAD_LIMIT){
                hasLoadPage++;
            }else {
                isAllLoad = true;
            }
            if (analysisNewImage(goodsResult))
                doImproveJob();
            else refreshBenefitGoods();
        }
    };

    private void refreshBenefitGoods() {
        mAllbenefitImage.clear();
        RealmResults<Image> results = mRealm.where(Image.class).notEqualTo("width",0).findAll();
        mAllbenefitImage.addAll(results);
        mBenefitItemAdapter.replaceWith(mAllbenefitImage);
    }

    /**
     * 分析新数据
     * @param goodsResult
     * @return 是否有新数据插入
     */
    private boolean analysisNewImage(GoodsResult goodsResult) {
        mRealm.beginTransaction();
        if(null != goodsResult && null != goodsResult.getResults()){
            for(Goods goods:goodsResult.getResults())
            {
                Image image = Image.queryImageById(mRealm,goods.getObjectId()); //如果是已有的图片就更新吧
                if(null == image)image = mRealm.createObject(Image.class);
                Image.updateDbGoods(image,goods);
            }
            mRealm.commitTransaction();
            return true;
        }
        mRealm.cancelTransaction();
        return false;
    }

    private void doImproveJob(){
        bImproveDoing = true;
        Intent intent = new Intent(getActivity(),ImageImproveService.class);
        intent.setAction(ImageImproveService.ACTION_IMPROVE_IMAGE);
        getActivity().startService(intent);
    }

    private void showNoDataView(){
        Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_shopping_cart)
                .colorRes(android.R.color.white);
        List<Integer> skipIds = new ArrayList<>();
        showEmpty(emptyDrawable, "数据列表为空", "没有拿到数据哎，请等一下再来玩妹子吧", skipIds);
    }

    private View.OnClickListener mErrorRetryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reloadData();
        }
    };

    private void onScrollStateChanged(){
        int[] positions = new int[mStaggeredGridLayoutManager.getSpanCount()];
        mStaggeredGridLayoutManager.findLastVisibleItemPositions(positions);
        for(int position : positions)
        {
            if(position == mStaggeredGridLayoutManager.getItemCount() - 1){
                loadMore();
                break;
            }
        }
    }

    private void reloadData() {
        mSwipeRefreshLayout.setRefreshing(true);
        mAllbenefitImage.clear();
        isAllLoad = false;
        hasLoadPage = 0;
        loadData(Constants.LOAD_DATA_PAGE_ONE);
    }

    private void loadMore() {
        if (isAllLoad) {
            Toast.makeText(getActivity(), "全部加载完毕", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isLoadMore) return;
        isLoadMore = true;
        loadData(hasLoadPage + 1);
    }

    private void loadData(int startPage) {

    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    @Override
    public void onChange() {

    }
}
