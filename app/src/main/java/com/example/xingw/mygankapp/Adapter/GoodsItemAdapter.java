package com.example.xingw.mygankapp.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xingw.mygankapp.Cache.ImageGoodsCache;
import com.example.xingw.mygankapp.Database.Image;
import com.example.xingw.mygankapp.Manager.CollectManager;
import com.example.xingw.mygankapp.Model.Goods;
import com.example.xingw.mygankapp.R;
import com.example.xingw.mygankapp.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xingw on 2016/3/7.
 */
public class GoodsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 3;

    private Context context;
    private int lastAnimatedPosition = -1;
    private boolean animateItems = false;

    private ArrayList<Goods> goodsItemData;

    public GoodsItemAdapter(Context context) {
        this.context = context;
        goodsItemData = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if(viewId == R.id.img_like_goods){
        }
    }

    private View.OnClickListener mItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer position = (Integer)view.getTag();
            Goods goods = goodsItemData.get(position.intValue());
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(goods.getUrl());
            intent.setData(content_url);
            view.getContext().startActivity(intent);
        }
    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.goods_item_layout, parent, false);
        final CellGoodsViewHolder cellGoodsViewHolder = new CellGoodsViewHolder(view);
        cellGoodsViewHolder.imgLikeGoods.setOnClickListener(this);
        cellGoodsViewHolder.rootView.setOnClickListener(mItemOnClickListener);
        return cellGoodsViewHolder;
    }

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        final CellGoodsViewHolder holder = (CellGoodsViewHolder) viewHolder;
        bindGoodsItem(position, holder);
    }

    private void bindGoodsItem(int position, CellGoodsViewHolder holder) {
        Goods goods = goodsItemData.get(position);
        Image image = ImageGoodsCache.getIns().getImgGoodsRandom(position);
        boolean hasImg = null != image;
        holder.txtGoodsTitle.setText("#"+goods.getDesc());
        holder.txtImgAuthor.setText(hasImg?"图："+image.getWho():"");
        holder.txtGoodsAuthor.setText(getGoodsAuthorInfo(goods));
        loadGoodsImage(holder, image);
        updateHeartButton(holder, goods, false);
        holder.imgLikeGoods.setTag(holder);
        holder.rootView.setTag(position);
    }
    private void loadGoodsImage(final CellGoodsViewHolder holder,Image imgGoods){
        if(null == imgGoods || TextUtils.isEmpty(imgGoods.getUrl())){
            Glide.with(context)
                    .load(R.drawable.item_default_img)
                    .centerCrop()
                    .into(holder.imgGoodsImageBg);
        }else {
            Glide.with(context)
                    .load(imgGoods.getUrl())
                    .centerCrop()
                    .into(holder.imgGoodsImageBg);
        }
    }
    private String getGoodsAuthorInfo(Goods goods){
        StringBuilder builder = new StringBuilder();
        Date date = Utils.formatDateFromStr(goods.getPublishedAt());
        String dateStr = Utils.getFormatDateStr(date);
        builder.append(goods.getWho()).append(TextUtils.isEmpty(dateStr) ? "" : "@" + dateStr);
        return builder.toString();
    }

    private void updateHeartButton(final CellGoodsViewHolder holder,Goods goods, boolean animated) {
        if (animated) {
            if (!CollectManager.getIns().isCollect(goods)) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.imgLikeGoods, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.imgLikeGoods, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.imgLikeGoods, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.imgLikeGoods.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //resetLikeAnimationState(holder);
                    }
                });
                animatorSet.start();
            }else {

            }
        } else {
            if (CollectManager.getIns().isCollect(goods)) {
                holder.imgLikeGoods.setImageResource(R.drawable.ic_heart_red);
            } else {
                holder.imgLikeGoods.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        }
    }

    @Override
    public int getItemCount() {
        return goodsItemData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class CellGoodsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_goods_img)
        ImageView imgGoodsImageBg;
        @Bind(R.id.txt_img_author)
        TextView txtImgAuthor;
        @Bind(R.id.txt_goods_title)
        TextView txtGoodsTitle;
        @Bind(R.id.img_like_goods)
        ImageView imgLikeGoods;
        @Bind(R.id.txt_goods_author)
        TextView txtGoodsAuthor;

        public final View rootView;

        public CellGoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rootView = view;
        }

    }
}
