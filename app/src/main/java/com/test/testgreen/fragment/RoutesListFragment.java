package com.test.testgreen.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.testgreen.MainActivity;
import com.test.testgreen.R;
import com.test.testgreen.adapter.RoutesRecyclerViewAdapter;
import com.test.testgreen.api.Constant;
import com.test.testgreen.event.MyDataEvent;
import com.test.testgreen.logger.Logger;
import com.test.testgreen.model.Route;
import com.test.testgreen.service.QueryService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class RoutesListFragment extends Fragment {
    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;
    public final static int STATUS_ERROR = 300;
    public final static String TAG = RoutesListFragment.class.getSimpleName();
    private static List<Route> routes;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.btnReload)
    Button btnReload;
    @BindView(R.id.toDateEdit)
    TextView toDateEdit;
    @BindView(R.id.fromDateEdit)
    TextView fromDateEdit;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    Context context;
    ;
    View view;
    EventBus eventBus = EventBus.getDefault();
    boolean checkQueryStatus = false;
    //for saving dialog
    boolean checkFromDialog = false;
    boolean checkToDialog = false;
    int day, year, month;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private Realm realmHelper;

    @OnClick(R.id.toDateEdit)
    public void toDateEdit(View view) {
        // TODO submit data to server...
        Logger.d(TAG, "edit2");
        toDatePickerDialog.show();
    }

    @OnClick(R.id.fromDateEdit)
    public void fromDateEdit(View view) {
        // TODO submit data to server...
        Logger.d(TAG, "edit1");
        fromDatePickerDialog.show();
    }

    @OnClick(R.id.find)
    public void findRoutes(View view) {
        // TODO submit data to server...
        Date fromDate, toDate;
        try {
            fromDate = Constant.dateFormatter.parse(fromDateEdit.getText().toString());
            toDate = Constant.dateFormatter.parse(toDateEdit.getText().toString());

            routes = realmHelper.where(Route.class)
                    .greaterThanOrEqualTo("fromDate", fromDate)
                    .lessThanOrEqualTo("toDate", toDate)
                    .findAll();
            initList(routes);
            Logger.d(TAG, fromDate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout, null);
        ButterKnife.bind(this, view);
        setDate(getActivity());
        eventBus.register(this);
        setRetainInstance(true);
        Logger.d(TAG, "onCreateView");
        context = getActivity().getApplicationContext();
        realmHelper = Realm.getInstance(getActivity().getApplicationContext());

        if (savedInstanceState != null) {
            //restore satuses
            checkQueryStatus = savedInstanceState.getBoolean("checkQueryStatus");
            checkFromDialog = savedInstanceState.getBoolean("checkFromDialog");
            checkToDialog = savedInstanceState.getBoolean("checkToDialog");
            //restore calendar values
            year = savedInstanceState.getInt("year");
            day = savedInstanceState.getInt("day");
            month = savedInstanceState.getInt("month");
            //restore current dates
            fromDateEdit.setText(savedInstanceState.getString("fromDate"));
            toDateEdit.setText(savedInstanceState.getString("toDate"));

            Log.d(TAG, "RestoreInstance" + year + month + day);
            if (checkFromDialog) {
                fromDatePickerDialog.updateDate(year, month, day);
                fromDateEdit(container);
                //checkFromDialog = false;
            }
            if (checkToDialog) {
                toDatePickerDialog.updateDate(year, month, day);
                toDateEdit(container);
                //checkToDialog = false;
            }
        } else {
            routes = realmHelper.allObjects(Route.class);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startQueryToServer();
            }
        });

        if (routes.isEmpty()) {
            //start loading operation
            recyclerView.setAdapter(null);
            startQueryToServer();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.GONE);
            Logger.d(TAG, "Already loaded.");
            if (checkQueryStatus) {
                checkQueryStatus(STATUS_START);
            } else {
                initList(routes);
            }
        }

        setFields();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyDataEvent event) {
        // processing answer
        int status = event.getMessage();
        // when loading starts - showingTheList Progressbar
        checkQueryStatus(status);
    }

    void checkQueryStatus(int status) {
        if (status == STATUS_START) {
            checkQueryStatus = true;
            swipeRefreshLayout.setRefreshing(true);
            recyclerView.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            frameLayout.setClickable(false);
        }
        // when loading starts - hiding Progressbar
        if (status == STATUS_FINISH) {
            checkQueryStatus = false;
            swipeRefreshLayout.setRefreshing(false);
            recyclerView.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.GONE);
            Toast.makeText(context, R.string.loaded, Toast.LENGTH_SHORT).show();
            frameLayout.setClickable(true);
            fromDateEdit.setText(Constant.dateFormatter.format(realmHelper.where(Route.class).minimumDate("fromDate")));
            toDateEdit.setText(Constant.dateFormatter.format(realmHelper.where(Route.class).maximumDate("toDate")));
            initList(routes);
        }
        //when error while loading data - appear button reload
        if (status == STATUS_ERROR) {
            checkQueryStatus = false;
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            btnReload.setVisibility(View.VISIBLE);
        }
    }

    private void startQueryToServer() {
        context.startService(new Intent(context, QueryService.class));
    }

    void initList(List<Route> routes) {
        Logger.d(TAG, "showingTheList");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        RoutesRecyclerViewAdapter adapter = new RoutesRecyclerViewAdapter(getActivity().getApplicationContext(), routes, new RoutesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(Route route) {
                //taking object of route to showingTheList method
                ((MainActivity) getActivity()).showFragmentInfo(route);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.btnReload)
    public void reload() {
        context.startService(new Intent(getActivity().getApplicationContext(), QueryService.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        eventBus.unregister(this);
    }


    public void setDate(Context context) {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEdit.setText(Constant.dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEdit.setText(Constant.dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setFields() {
        fromDateEdit.setInputType(InputType.TYPE_NULL);
        fromDateEdit.requestFocus();

        toDateEdit.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "SavedInstance" + year + month + day);
        if (fromDatePickerDialog.isShowing()) {
            checkFromDialog = true;
            year = fromDatePickerDialog.getDatePicker().getYear();
            month = fromDatePickerDialog.getDatePicker().getMonth();
            day = fromDatePickerDialog.getDatePicker().getDayOfMonth();
        } else {
            checkFromDialog = false;
        }
        if (toDatePickerDialog.isShowing()) {
            checkToDialog = true;
            year = toDatePickerDialog.getDatePicker().getYear();
            month = toDatePickerDialog.getDatePicker().getMonth();
            day = toDatePickerDialog.getDatePicker().getDayOfMonth();
        } else {
            checkToDialog = false;
        }

        fromDatePickerDialog.cancel();
        toDatePickerDialog.cancel();
        //saving statuses of dialogs and query
        outState.putBoolean("checkQueryStatus", checkQueryStatus);
        outState.putBoolean("checkFromDialog", checkFromDialog);
        outState.putBoolean("checkToDialog", checkToDialog);
        //saving calendar dialog
        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);
        //saving current dates
        outState.putString("fromDate", fromDateEdit.getText().toString());
        outState.putString("toDate", toDateEdit.getText().toString());
    }
}
