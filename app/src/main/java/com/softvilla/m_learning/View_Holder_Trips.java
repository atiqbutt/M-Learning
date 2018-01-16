package com.softvilla.m_learning;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Malik on 01/08/2017.
 */

public class View_Holder_Trips extends RecyclerView.ViewHolder {

    CardView cvtrips;
    TextView name;
    public RadioButton selectionState;
    private int lastSelectedPosition = -1;
   // TextView description;
   // ImageView imageView;



    View_Holder_Trips(View itemView) {
        super(itemView);
        cvtrips = (CardView) itemView.findViewById(R.id.cardViewtrips);
        name = (TextView) itemView.findViewById(R.id.offer_amount);
        selectionState = (RadioButton) itemView.findViewById(R.id.offer_select);

        /*selectionState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();

                Toast.makeText(OffersRecyclerViewAdapter.this.context,
                        "selected offer is " + offerName.getText(),
                        Toast.LENGTH_LONG).show();
            }
        });*/
    }
        //description = (TextView) itemView.findViewById(R.id.description);
        //imageView = (ImageView) itemView.findViewById(R.id.image);

}


