package com.link.cloud.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.contract.SplashContract;
import com.link.cloud.presenter.SplashPresenter;
import butterknife.BindView;

public class SplashActivity extends BaseActivity implements SplashContract.View {
    @BindView(R.id.sync_progress)
    LinearLayout syncProgress;
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this);
        presenter.getSyncData("");
    }

    @Override
    public void SyncSuccess() {
        showToast(getString(R.string.sync_success));
        syncProgress.setVisibility(View.GONE);
        startAcivity(EntranceActivity.class);
    }

    @Override
    public void SyncFail(String msg) {
        showToast(msg);
        syncProgress.setVisibility(View.GONE);
        startAcivity(EntranceActivity.class);
    }

    @Override
    public void NetWorkError(Boolean isNet, Throwable e) {
        if (isNet) {
            showToast(getResources().getString(R.string.net_error));
        } else {
            showToast(e.getMessage());
        }
        syncProgress.setVisibility(View.GONE);
        startAcivity(EntranceActivity.class);
    }

    @Override
    public void StartSync() {
        syncProgress.setVisibility(View.VISIBLE);
    }


    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.splash_activity;
    }

}
