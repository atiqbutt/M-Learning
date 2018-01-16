package com.softvilla.m_learning;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Malik on 01/08/2017.
 */

public class Recycler_View_Adapter_Trip extends RecyclerView.Adapter<Recycler_View_Adapter_Trip.View_Holder_Trips> {

    //List<ImageInfo> list = Collections.emptyList();
    Context context;
    List<TripModel> list;
    String id;
    OnCardClickListner onCardClickListner;
    private int lastSelectedPosition;


    public Recycler_View_Adapter_Trip(Context context, List<TripModel> list, int checkedIndex) {
        this.list = list;
        this.context = context;
        //this.id = id;
        this.list = list;
        this.lastSelectedPosition = checkedIndex;
    }

    @Override
    public View_Holder_Trips onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_trips, parent, false);
        View_Holder_Trips holder = new View_Holder_Trips(v);
        return holder;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(View_Holder_Trips holder, final int position) {
       // TripModel tripModel = list.get(position);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
         holder.name.setText(list.get(position).name);

        holder.selectionState.setChecked(lastSelectedPosition == position);






    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    /*public void insert(int position, ImageInfo data) {
        list.add(position, data);
        notifyItemInserted(position);
    }*/

    // Remove a RecyclerView item containing a specified Data object
    public void remove(ImageInfo data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }


    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    public class View_Holder_Trips extends RecyclerView.ViewHolder {

        CardView cvtrips;
        TextView name;
        public RadioButton selectionState;
        // TextView description;
        // ImageView imageView;



        View_Holder_Trips(View itemView) {
            super(itemView);
            cvtrips = (CardView) itemView.findViewById(R.id.cardViewtrips);
            name = (TextView) itemView.findViewById(R.id.offer_amount);
            selectionState = (RadioButton) itemView.findViewById(R.id.offer_select);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    TripModel.id = list.get(lastSelectedPosition).tripid;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("selectedTrip",TripModel.id);
                    editor.apply();

                   /* Toast.makeText(Recycler_View_Adapter_Trip.this.context,
                            String.valueOf(list.get(lastSelectedPosition).tripid),
                            Toast.LENGTH_LONG).show();
*/                }
            });
        }
        //description = (TextView) itemView.findViewById(R.id.description);
        //imageView = (ImageView) itemView.findViewById(R.id.image);

    }
}
