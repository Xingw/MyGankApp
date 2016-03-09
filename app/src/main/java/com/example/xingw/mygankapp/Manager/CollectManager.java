package com.example.xingw.mygankapp.Manager;

import com.example.xingw.mygankapp.Model.Goods;

/**
 * Created by Xingw on 2016/3/9.
 */
public class CollectManager {
    private static CollectManager instance;

    public static CollectManager getIns() {
        if (null == instance) {
            synchronized (CollectManager.class) {
                if (null == instance) {
                    instance = new CollectManager();
                }
            }
        }
        return instance;
    }

    public boolean isCollect(Goods goods){
        return false;
    }
}
