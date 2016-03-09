package com.example.xingw.mygankapp.Model;

/**
 * Created by Xingw on 2015/12/1.
 */
public class BaseResult {
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;
}
