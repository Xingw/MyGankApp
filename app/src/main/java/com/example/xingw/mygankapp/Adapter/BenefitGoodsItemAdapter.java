package com.example.xingw.mygankapp.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.xingw.mygankapp.Database.Image;
import com.example.xingw.mygankapp.R;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Xingw on 2015/12/10.
 */
public abstract class BenefitGoodsItemAdapter extends ArrayRecyclerAdapter<Image,
        BenefitGoodsItemAdapter
        .ViewHolder>{

    private final LayoutInflater inflater;
    private Context context;

    protected abstract void onItemClick(View v , int position);

    public class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.image)
        public RadioImageView imageView;

        public ViewHolder(@LayoutRes int resource, ViewGroup parent) {
            super(inflater.inflate(resource, parent, false));
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, getAdapterPosition());
                }
            });
        }

        private void loadGoodsImage(ViewHolder holder,Image imgGoods) {
            if(null == imgGoods || TextUtils.isEmpty(imgGoods.getUrl())){
                Glide.with(context)
                        .load(R.drawable.item_default_img)
                        .into(holder.imageView);
            }else {
                Glide.with(context)
                        .load(imgGoods.getUrl())
                        .into(holder.imageView);
            }
        }
    }
}
