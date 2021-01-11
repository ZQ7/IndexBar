package com.yi2580.indexbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ResourceListView extends FrameLayout {

    private RecyclerView mRv;
    private IndexBar mIndexBar;//右侧边栏导航区域
    private TextView mTvSideBarHint;//显示指示器DialogText
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;

    private int mHeaderViewCount = 0;
    private List<? extends IBaseIndex> mSourceDatas;//Adapter的数据源

    public ResourceListView(Context context) {
        super(context);
        init();
    }

    public ResourceListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ResourceListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.resource_list, this);
        mRv = findViewById(R.id.recycler_view);
        mTvSideBarHint = findViewById(R.id.tv_index_bar_hint);
        mIndexBar = findViewById(R.id.index_bar);
        //设置index触摸监听器
        mIndexBar.setOnIndexPressedListener(new IndexBar.OnIndexPressedListener() {
            @Override
            public void onIndexPressed(int index, String text) {
                if (mTvSideBarHint != null) { //显示hintTexView
                    mTvSideBarHint.setVisibility(View.VISIBLE);
                    mTvSideBarHint.setText(text);
                }
                //滑动Rv
                if (mManager != null) {
                    int position = getPosByTag(text);
                    if (position != -1) {
                        mManager.scrollToPositionWithOffset(position, 0);
                    }
                }
            }

            @Override
            public void onMotionEventEnd() {
                //隐藏hintTextView
                if (mTvSideBarHint != null) {
                    mTvSideBarHint.setVisibility(View.GONE);
                }
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return mRv;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapter(adapter, new LinearLayoutManager(getContext()));
    }

    public void setAdapter(RecyclerView.Adapter adapter, LinearLayoutManager manager) {
        mManager = manager;
        mRv.setLayoutManager(mManager);
        mAdapter = adapter;
        mRv.setAdapter(mAdapter);
    }

    public int getHeaderViewCount() {
        return mHeaderViewCount;
    }

    /**
     * 设置Headerview的Count
     */
    public void setHeaderViewCount(int headerViewCount) {
        mHeaderViewCount = headerViewCount;
    }


    public void setDataSource(List<? extends IBaseIndex> data) {
        this.setDataSource(false, data);

    }

    public void setDataSource(boolean needRealIndex, List<? extends IBaseIndex> data) {
        mIndexBar.setNeedRealIndex(needRealIndex);
        if (needRealIndex) {
            mIndexBar.setSourceDatas(getIndexDatas(data));
        }
        mSourceDatas = data;

    }

    public List<String> getIndexDatas(List<? extends IBaseIndex> sourceDatas) {
        List<String> list = new ArrayList<>();
        if (null != sourceDatas && !sourceDatas.isEmpty()) {
            int size = sourceDatas.size();
            String baseIndexTag;
            for (int i = 0; i < size; i++) {
                baseIndexTag = sourceDatas.get(i).getBaseIndexTag();
                if (!list.contains(baseIndexTag)) {//则判断是否已经将这个索引添加进去，若没有则添加
                    list.add(baseIndexTag);
                }
            }
        }
        return list;
    }

    /**
     * 根据传入的pos返回tag
     *
     * @param tag
     * @return
     */
    private int getPosByTag(String tag) {
        // 解决源数据为空 或者size为0的情况,
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return -1;
        }
        if (TextUtils.isEmpty(tag)) {
            return -1;
        }
        for (int i = 0; i < mSourceDatas.size(); i++) {
            if (tag.equals(mSourceDatas.get(i).getBaseIndexTag())) {
                return i + getHeaderViewCount();
            }
        }
        return -1;
    }
}
