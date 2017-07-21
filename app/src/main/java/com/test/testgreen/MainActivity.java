package com.test.testgreen;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.test.testgreen.event.PermissionStatusEvent;
import com.test.testgreen.fragment.RouteInfoFragment;
import com.test.testgreen.fragment.RoutesListFragment;
import com.test.testgreen.model.Route;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends FragmentActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    //getting permission to WRITE_EXTERNAL_STORAGE
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    RoutesListFragment routesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        boolean bool = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
        if (bool) {
            gettingPermissionToWrite();
        } else {
            System.out.println("FALSE");
            showFragmentList();
        }
    }//--onCreate

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkingPermission(PermissionStatusEvent permissionStatusEvent) {
        switch (permissionStatusEvent) {
            case ACCSESS:
                showFragmentList();
                break;
            case DENY:
                Toast.makeText(this, R.string.permissionsError, Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this, R.string.permissionsError, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

    }

    //showFragmentList the list
    public void showFragmentList() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        fragmentManager = getFragmentManager();
        routesListFragment = (RoutesListFragment) fragmentManager.findFragmentByTag("list");
        if (routesListFragment == null) {
            routesListFragment = new RoutesListFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, this.routesListFragment, "list");
            fragmentTransaction.commit();
        }
    }//--showFragmentList()

    public void showFragmentInfo(Route route) {
        RouteInfoFragment routeInfoFragment = (RouteInfoFragment) fragmentManager.findFragmentById(R.id.details_frag);
        Bundle bundle = new Bundle();
        bundle.putSerializable("routeObj", route);
        fragmentManager = getFragmentManager();

        if (routeInfoFragment != null) {
            RouteInfoFragment infoFragment = new RouteInfoFragment();
            infoFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frameLayoutInfo, infoFragment);
            transaction.commit();
        } else {
            routeInfoFragment = new RouteInfoFragment();
            routeInfoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, routeInfoFragment);
            fragmentTransaction.addToBackStack("backStack");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void gettingPermissionToWrite() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PERMISSION_GRANTED) {
            EventBus.getDefault().post(PermissionStatusEvent.ACCSESS);
        } else {
            EventBus.getDefault().post(PermissionStatusEvent.DENY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}//class MainActivity
