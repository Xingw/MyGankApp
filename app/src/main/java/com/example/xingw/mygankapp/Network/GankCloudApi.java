package com.example.xingw.mygankapp.Network;

import com.example.xingw.mygankapp.Config.Constants;

/**
 * Created by Xingw on 2015/12/1.
 */
public class GankCloudApi {
    public static GankCloudApi instance;

    /**每次加载条目*/
    public static final int LOAD_LIMIT = 20;
    /**加载起始页面*/
    public static final int LOAD_START = 1;

    public static final String ENDPOINT = Constants.GANK_SERVER_IP;

}
