package com.test.testgreen.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.testgreen.R;
import com.test.testgreen.api.Constant;
import com.test.testgreen.model.Route;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class RouteInfoFragment extends Fragment {
    @BindView(R.id.title)
    TextView titleText;
    @BindView(R.id.numb)
    TextView numbText;
    @BindView(R.id.fromDate)
    TextView fromDateText;
    @BindView(R.id.fromTime)
    TextView fromTimeText;
    @BindView(R.id.fromInfo)
    TextView fromInfoText;
    @BindView(R.id.toDate)
    TextView toDateText;
    @BindView(R.id.toTime)
    TextView toTimeText;
    @BindView(R.id.toInfo)
    TextView toInfoText;
    @BindView(R.id.info)
    TextView infoText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.busNumb)
    TextView busNumbText;
    Realm realmHelper;
    private List<Route> routes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realmHelper = Realm.getInstance(getActivity().getApplicationContext());
        routes = realmHelper.allObjects(Route.class);
        Bundle bundle = getArguments();
        Route route;
        if (bundle != null) {
            route = (Route) bundle.getSerializable("routeObj"); // taking our object, which we selected in the list
        } else {
            route = routes.get(0);
        }
        View view = inflater.inflate(R.layout.fragment_info, null);
        ButterKnife.bind(this, view);
        //Adding info about our wayText

        titleText.setText(route.getFromCity().getCity() + " - " + route.getToCity().getCity());
        numbText.setText(route.getRouteId());
        fromDateText.setText(Constant.dateFormatter.format(route.getFromDate()));
        fromTimeText.setText(route.getFromTime());
        fromInfoText.setText(route.getFromInfo());
        toDateText.setText(Constant.dateFormatter.format(route.getToDate()));
        toTimeText.setText(route.getToTime());
        toInfoText.setText(route.getToInfo());
        infoText.setText(route.getInfo());
        priceText.setText(route.getPrice() + getString(R.string.grn));
        busNumbText.setText(route.getBusId());

        return view;
    }
}
