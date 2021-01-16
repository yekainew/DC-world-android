package com.jkkg.hhtx.utils;


import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * Created by Administrator on 2019/6/12.
 */

public class PageUtils {
    //页数
    private Integer pagNumber = 0;
    //每页显示的条数
    private Integer pagSize = 50;
    //总共页数
    private Integer sumPage = 0;
    //刷新状态-true代表刷新-false代表加载更多
    private boolean refreshType = true;

    private SmartRefreshLayout fresh_layout;

    private OnPageRefresh onPageRefresh;

    public PageUtils() {
    }

    public PageUtils(SmartRefreshLayout fresh_layout) {
        this.fresh_layout = fresh_layout;
    }

    public void setOnPageRefresh(OnPageRefresh onPageRefresh) {
        this.onPageRefresh = onPageRefresh;
        fresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
                setRefreshType(true);
                if (onPageRefresh != null) {
                    onPageRefresh.onRefresh();
                }
                fresh_layout.finishRefresh();
            }
        });

        fresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
                setRefreshType(false);
                if (onPageRefresh != null && pagNumber < sumPage) {
                    onPageRefresh.onRefresh();
                }
                fresh_layout.finishLoadMore();
            }
        });
    }

    public void refresh() {
        pagNumber = 0;
        pagSize = 50;
        sumPage = 0;
        setRefreshType(true);
    }


    public void loadMore() {
        pagNumber++;
    }

    public void setSumPage(Integer sumPage) {
        this.sumPage = sumPage;
    }

    public Integer getSumPage() {
        return sumPage;
    }

    public Integer getPagNumber() {
        return pagNumber;
    }

    public void setPagNumber(Integer pagNumber) {
        this.pagNumber = pagNumber;
    }

    public Integer getPagSize() {
        return pagSize;
    }

    public boolean isRefreshType() {
        return refreshType;
    }

    public void setRefreshType(boolean refreshType) {
        this.refreshType = refreshType;
    }

    public interface OnPageRefresh {
        void onRefresh();
    }
}
