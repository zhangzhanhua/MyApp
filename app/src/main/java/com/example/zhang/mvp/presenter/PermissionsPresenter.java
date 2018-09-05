package com.example.zhang.mvp.presenter;

import com.example.zhang.base.BasePresenter;
import com.example.zhang.mvp.contract.PermissionsContract;
import com.example.zhang.mvp.model.PermissionsModel;

public class PermissionsPresenter extends BasePresenter<PermissionsContract.ISettingView, PermissionsModel> {
    public PermissionsPresenter(PermissionsContract.ISettingView view) {
        super(view);
        model = new PermissionsModel();
    }
}
