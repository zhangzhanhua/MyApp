package com.example.zhang.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zhang.BuildConfig;
import com.example.zhang.utils.LeakCanaryUtil;

/**
 * @author : zzh
 * @date : 2019/5/13
 * @desc :Fragment基类
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG){
            LeakCanaryUtil.getRefWatcher().watch(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
