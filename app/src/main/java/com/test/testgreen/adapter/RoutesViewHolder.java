package com.test.testgreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.test.testgreen.R;
import com.test.testgreen.model.Route;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nick Demenko on 29.03.2017.
 */

public class RoutesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fromToCityDescriptionTextView)
    TextView fromToCityDescriptionTextView;

    @BindView(R.id.fromDateTextView)
    TextView fromDateTextView;

    @BindView(R.id.fromTimeTextView)
    TextView fromTimeTextView;

    @BindView(R.id.toDateTextView)
    TextView toDateTextView;

    @BindView(R.id.toTimeTextView)
    TextView toTimeTextView;

    @BindView(R.id.infoTextView)
    TextView priceTextView;

    RoutesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Route route, final RoutesRecyclerViewAdapter.OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(route);
            }
        });
    }
}
