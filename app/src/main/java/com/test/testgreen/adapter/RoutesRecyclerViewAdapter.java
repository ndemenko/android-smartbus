package com.test.testgreen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.testgreen.R;
import com.test.testgreen.api.Constant;
import com.test.testgreen.model.Route;

import java.util.List;

public class RoutesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Route> routes;
    private final OnItemClickListener listener;
    private final int DIV = 0, NONDIV = 1;
    Context context;

    public RoutesRecyclerViewAdapter(Context context, List<Route> routes, OnItemClickListener listener) {
        this.listener = listener;
        this.context = context;
        this.routes = routes;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return DIV;
        } else {
            return NONDIV;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case DIV:
                View firstView = inflater.inflate(R.layout.item_route, parent, false);
                viewHolder = new RoutesViewHolder(firstView);
                break;
            case NONDIV:
                View secondView = inflater.inflate(R.layout.sec_item_route, parent, false);
                viewHolder = new SecondRoutesViewHolder(secondView);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                viewHolder = new RoutesViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DIV:
                RoutesViewHolder vh1 = (RoutesViewHolder) viewHolder;
                configureFirstViewHolder(vh1, position);
                break;
            case NONDIV:
                SecondRoutesViewHolder vh2 = (SecondRoutesViewHolder) viewHolder;
                configureSecondViewHolder(vh2, position);
                break;
        }
    }

    private void configureFirstViewHolder(RoutesViewHolder vh1, int position) {
        Route route = routes.get(position);
        vh1.bind(route, listener);
        //forEntity
        vh1.fromToCityDescriptionTextView.setText(route.getFromCity().getCity() + " - " + route.getToCity().getCity());
        vh1.fromDateTextView.setText(Constant.dateFormatter.format(route.getFromDate()));
        vh1.fromTimeTextView.setText(route.getFromTime());
        vh1.toDateTextView.setText((Constant.dateFormatter.format(route.getToDate())));
        vh1.toTimeTextView.setText(route.getToTime());
        vh1.priceTextView.setText(route.getPrice() + context.getString(R.string.grn));

    }

    private void configureSecondViewHolder(SecondRoutesViewHolder vh2, int position) {
        Route route = routes.get(position);
        vh2.bind(route, listener);
        //forEntity
        vh2.fromToCityDescriptionTextView.setText(route.getFromCity().getCity() + " - " + route.getToCity().getCity());
        vh2.fromDateTextView.setText(Constant.dateFormatter.format(route.getFromDate()));//Constant.dateFormatter.format(newDate.getTime()))
        vh2.fromTimeTextView.setText(route.getFromTime());
        vh2.toDateTextView.setText(Constant.dateFormatter.format(route.getToDate()));
        vh2.toTimeTextView.setText(route.getToTime());
        vh2.priceTextView.setText(route.getPrice() + context.getString(R.string.grn));
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    public interface OnItemClickListener {
        void onClick(Route route);
    }

}
