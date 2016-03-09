package com.example.xingw.mygankapp.Main;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.xingw.mygankapp.Adapter.MainFragmentPagerAdapter;
import com.example.xingw.mygankapp.Model.GoodsResult;
import com.example.xingw.mygankapp.Fragment.BenefitListFragment;
import com.example.xingw.mygankapp.R;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observer;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ly_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.viewpage)
    ViewPager mViewPager;
    @Bind(R.id.tabs)
    TableLayout mTableLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.fab)
    FloatingActionButton mFABtn;

    private Realm mrealm;
    private MainFragmentPagerAdapter mPagerAdapter;
    private BenefitListFragment mBenefitListFragment;

    /***
     * 获取福利图的回调接口，拿到数据用来做背景
     */
    private Observer<GoodsResult> getImageGoodsObserver = new Observer<GoodsResult>() {
        @Override
        public void onCompleted() {
            Logger.d("获取背景图服务完成");
        }

        @Override
        public void onError(Throwable e) {
           Logger.e(e,"获取背景图服务失败");
        }

        @Override
        public void onNext(GoodsResult goodsResult) {
            if ( null != goodsResult && null != goodsResult.getResults()){
                ImageGoodsCache.getIns().addAllImageGoods(goodsResult.getResults());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mrealm = Realm.getInstance(this);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        setupDrawerContent(mNavigationView);

        setupViewPager();



        mFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Here's a Snackbar",Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
            }
        });

    }

    private void setupViewPager() {
        mBenefitListFragment  = new BenefitListFragment();
        mPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(CommonGoodsListFragment.newFragment("Android"), "Android");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mrealm.close();
    }
    private void setupDrawerContent(NavigationView navigationview) {
        navigationview.getMenu().findItem(R.id.nav_home).setChecked(true);
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                disposeMenuAction(item);
                return true;

            }
        });
    }

    private void disposeMenuAction(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_collect:
            case R.id.nav_time:
                Toast.makeText(this,"功能开发中",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_code:
                callWebView(com.example.xingw.mygankapp.Config.Constants.GITHUB_URL);
                break;
            case R.id.nav_author:
                Toast.makeText(this,"这个作品的作者是我^_^",Toast.LENGTH_LONG).show();
                break;
        }
    }
    private void callWebView(String url){
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }
}
