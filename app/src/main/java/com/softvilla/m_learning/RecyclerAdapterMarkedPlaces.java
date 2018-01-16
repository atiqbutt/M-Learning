package com.softvilla.m_learning;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Malik on 17/07/2017.
 */

public class RecyclerAdapterMarkedPlaces extends RecyclerView.Adapter<RecyclerAdapterMarkedPlaces.MyViewHolder> {

    Context context;
    //ArrayList<DescriptionInfoData> data;
    List<DescriptionInfo> data;
    LayoutInflater inflater;

    public RecyclerAdapterMarkedPlaces(Context context, List<DescriptionInfo> data){
        this.context = context;
        this.data = data;//DescriptionInfo.listAll(DescriptionInfo.class);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardrow,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.Name.setText(data.get(position).descriptionTitle);
        Picasso.with(context).load(data.get(position).coordinates).placeholder(R.drawable.placeholder).into(holder.Image);
        /*List<AddLocaton> addLocatonList = AddLocaton.listAll(AddLocaton.class);
        for(AddLocaton obj : addLocatonList){
            boolean isSet = true;
            if(obj.ObjectIdentity.equalsIgnoreCase(data.get(position).identity)){
                if(isSet){
//                    byte[] decodedString = Base64.decode(obj.Image, Base64.URL_SAFE);
//                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] imageBytes = stream.toByteArray();
                    imageBytes = Base64.decode(obj.Image, Base64.DEFAULT);
                    Bitmap decadeImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    final Drawable drawable = new BitmapDrawable(context.getResources(),decadeImage);
                    holder.Image.setImageBitmap(decadeImage);
                    isSet = false;
                }

            }
        }*/
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, data.get(position).identity, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ListOfMarkedImages.class);
                intent.putExtra("id",data.get(position).identity);
                context.startActivity(intent);
            }
        });

        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] imageBytes = stream.toByteArray();
        imageBytes = Base64.decode(data.get(position).Image, Base64.DEFAULT);
        Bitmap decadeImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
        final Drawable drawable = new BitmapDrawable(context.getResources(),decadeImage);
        holder.Image.setBackground(drawable);*/
        //holder.Image.setImageBitmap(data.get(position).Image);

       /* Glide
                .with(context)
                .load(data.get(position).Image)
                .into(holder.Image);*/

        /*holder.Image.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.image_dialogue);
                        ImageView image = (ImageView) dialog.findViewById(R.id.dialogueImage);
                        image.setBackground(drawable);
                        dialog.show();
                    }
                }
        );*/


        /*holder.dots.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(context,holder.card, Gravity.RIGHT);
                        popup.getMenuInflater().inflate(R.menu.menu_popup,popup.getMenu());
                        popup.setOnMenuItemClickListener(
                                new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        if(item.getTitle().equals("Delete")){
                                            //List<AddLocaton> locatons = AddLocaton.listAll(AddLocaton.class);

                                            AddLocaton obj = AddLocaton.findById(AddLocaton.class,data.get(position).id);

                                            obj.delete();
                                            List<AddLocaton> locatons = AddLocaton.listAll(AddLocaton.class);
                                            ArrayList<AddLocationData> addLocationDatas = new ArrayList<AddLocationData>();
                                            for(AddLocaton addLocaton : locatons){
                                                AddLocationData obj2 = new AddLocationData();
                                                obj2.LocationName = addLocaton.LocationName;
                                                obj2.id = addLocaton.getId();
                                                obj2.LocationLat = addLocaton.LocationLat;
                                                obj2.LocationLng = addLocaton.LocationLng;
                                                obj2.Image = addLocaton.Image;
                                                addLocationDatas.add(obj2);
                                            }
                                            data = addLocationDatas;
                                            notifyDataSetChanged();
                                            //context.startActivity(new Intent(context,ListOfMarkedPlaces.class));



                                        }
                                        else {


                                            final EditText input = new EditText(context);
                                            input.setHint("Enter Location Name");
                                            input.setText(data.get(position).LocationName);

                                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.MarginLayoutParams.WRAP_CONTENT);
                                            lp.setMargins(10,10,10,10);


                                            input.setLayoutParams(lp);
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                            alertDialog.setTitle("Location Name");
                                            alertDialog.setMessage("Replace Location Name");
                                            alertDialog.setView(input);



                                            alertDialog.setPositiveButton("YES",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String password = input.getText().toString();
                                                            if(TextUtils.isEmpty(password)){
                                                                input.setError("Enter Name");
                                                            }
                                                            else {
                                                                AddLocaton obj = AddLocaton.findById(AddLocaton.class,data.get(position).id);
                                                                obj.LocationName = password;
                                                                obj.save();
                                                                List<AddLocaton> locatons = AddLocaton.listAll(AddLocaton.class);
                                                                ArrayList<AddLocationData> addLocationDatas = new ArrayList<AddLocationData>();
                                                                for(AddLocaton addLocaton : locatons){
                                                                    AddLocationData obj2 = new AddLocationData();
                                                                    obj2.LocationName = addLocaton.LocationName;
                                                                    obj2.id = addLocaton.getId();
                                                                    obj2.LocationLat = addLocaton.LocationLat;
                                                                    obj2.LocationLng = addLocaton.LocationLng;
                                                                    obj2.Image = addLocaton.Image;
                                                                    addLocationDatas.add(obj2);
                                                                }
                                                                data = addLocationDatas;
                                                                notifyDataSetChanged();
                                                            }
                                                        }
                                                    });

                                            alertDialog.setNegativeButton("NO",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            alertDialog.show();

                                        }
                                        return true;
                                    }
                                }
                        );
                        popup.show();
                    }
                }
        );*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        CardView card;
        ImageView dots, Image;


        public MyViewHolder(android.view.View itemView){
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Markedname);
            card = (CardView) itemView.findViewById(R.id.markeListCard);
            //dots = (ImageView) itemView.findViewById(R.id.dots);
            Image = (ImageView) itemView.findViewById(R.id.imageView2);
        }
    }
}
