package com.example.xingw.mygankapp.Model;

import java.util.ArrayList;

/**
 * Created by Xingw on 2016/3/7.
 */
public class DayGoodsResult extends BaseResult{
    private DayGoods results;
    private ArrayList<String> category;

    public DayGoods getResults() {
        return results;
    }

    public void setResults(DayGoods results) {
        this.results = results;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }
}