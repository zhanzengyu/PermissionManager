package com.nesger.permissionmanager;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nesger.permission.PermissionHelper;
import com.nesger.permission.callback.OnPermissionCallback;

public class MainActivity extends AppCompatActivity implements OnPermissionCallback{

    private PermissionHelper permissionHelper;
    //值唯一即可,这是为了返回做标识使用
    private final int REQUEST_SETTING = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doPermissionCheck();
    }

    /**
     * 检查是否拥有权限
     */
    private void doPermissionCheck(){
        permissionHelper = PermissionHelper.getInstance(this);
        permissionHelper
                .setForceAccepting(true) // default is false. its here so you know that it exists.
                .request(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        //权限点击允许
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {

    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        //权限已经打开了
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        //需要申请权限
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        //禁止权限
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.parse("package:" + getPackageName());
        intent.setData(uri);
        startActivityForResult(intent,REQUEST_SETTING);
        //TODO 跳转前可以做下简单的提示,下面的提示是简单的一个示例
        Toast.makeText(this,"请打开存储权限",Toast.LENGTH_SHORT).show();
    }

    //android6.0以下会触发
    @Override
    public void onNoPermissionNeeded() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionHelper.onActivityForResult(requestCode);
        //返回时重新进行检查
        if (requestCode == REQUEST_SETTING){
            doPermissionCheck();
        }
    }

    /**
     * 记得手动重写这个方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
