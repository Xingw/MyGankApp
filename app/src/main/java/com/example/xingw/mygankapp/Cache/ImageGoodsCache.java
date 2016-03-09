package com.example.xingw.mygankapp.Cache;

import com.example.xingw.mygankapp.Database.Image;
import com.example.xingw.mygankapp.Model.Goods;

import java.util.ArrayList;
import java.util.Random;

import io.realm.RealmResults;

/**
 * Created by Xingw on 2016/3/7.
 */
public class ImageGoodsCache {
    private static ImageGoodsCache instance;

    private ArrayList<Image> mGankImageList;

    public ImageGoodsCache(){
        mGankImageList = new ArrayList<>();
    }

    public static ImageGoodsCache getIns() {
        if (null == instance) {
            synchronized (ImageGoodsCache.class) {
                if (null == instance) {
                    instance = new ImageGoodsCache();
                }
            }
        }
        return instance;
    }

    private void addGoodsToImage(Goods goods){
        Image image = new Image();
        Image.updateDbGoods(image,goods);
        mGankImageList.add(image);
    }

    public void addAllImageGoods(ArrayList<Goods> list){
        if(null != list && list.size() > 0){
            mGankImageList.clear();
            for (Goods goods:list){
                addGoodsToImage(goods);
            }
        }
    }

    public void addAllImageGoods(RealmResults<Image> images){
        mGankImageList.clear();
        mGankImageList.addAll(images);
    }

    public ArrayList<Image> getGankImageList() {
        return mGankImageList;
    }

    public Image getImgGoodsRandom(int randomIndex){
        int size = getGankImageList().size();
        if(size == 0)return null;
        Random random = new Random();
        int randomInt = random.nextInt(size);
        if(randomInt + randomIndex >= size){
            return getGankImageList().get(randomInt);
        }
        return getGankImageList().get(randomInt + randomIndex);
    }

}
