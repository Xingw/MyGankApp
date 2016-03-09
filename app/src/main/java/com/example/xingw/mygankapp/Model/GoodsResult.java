package com.example.xingw.mygankapp.Model;

import java.util.ArrayList;

/**
 * Created by Xingw on 2015/12/1.
 */
public class GoodsResult extends BaseResult {
    private ArrayList<Goods> results;

    public GoodsResult(ArrayList<Goods> results) {
        this.results = results;
    }

    public ArrayList<Goods> getResults() {
        return results;
    }

    public void setResults(ArrayList<Goods> results) {
        this.results = results;
    }
}
