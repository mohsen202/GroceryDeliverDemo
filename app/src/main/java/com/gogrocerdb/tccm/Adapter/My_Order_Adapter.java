package com.gogrocerdb.tccm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gogrocerdb.tccm.Activity.OrderDetail;
import com.gogrocerdb.tccm.Model.My_order_model;
import com.gogrocerdb.tccm.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class My_Order_Adapter extends RecyclerView.Adapter<My_Order_Adapter.MyViewHolder> {

    private List<My_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
SharedPreferences preferences;
    private Context context;

    public My_Order_Adapter(Context context, List<My_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modemodelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;

    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date,tv_socity,
                tv_recivername,tv_recivernumber,tv_house;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            tv_socity=view.findViewById(R.id.tv_societyname);
            tv_house=view.findViewById(R.id.tv_house);
            tv_recivername=view.findViewById(R.id.tv_recivername);
            tv_recivernumber=view.findViewById(R.id.tv_recivernmobile);
            cardView = view.findViewById(R.id.card_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String saleid = modelList.get(position).getSale_id();
                        String placedon = modelList.get(position).getOn_date();
                        String time = modelList.get(position).getDelivery_time_from() + "-" + modelList.get(position).getDelivery_time_to();
                        String item = modelList.get(position).getTotal_items();
                        String ammount = modelList.get(position).getTotal_amount();
                        String status = modelList.get(position).getStatus();
                        String society=modelList.get(position).getSocityname();
                        String house=modelList.get(position).getHouse();
                        String recivername=modelList.get(position).getRecivername();
                        String recivermobile=modelList.get(position).getRecivermobile();
                        Intent intent = new Intent(context, OrderDetail.class);
                        intent.putExtra("sale_id", saleid);
                        intent.putExtra("placedon", placedon);
                        intent.putExtra("time", time);
                        intent.putExtra("item", item);
                        intent.putExtra("ammount", ammount);
                        intent.putExtra("status", status);
                        intent.putExtra("socity_name",society);
                        intent.putExtra("house_no",house);
                        intent.putExtra("receiver_name",recivername);
                        intent.putExtra("receiver_mobile",recivermobile);
                        context.startActivity(intent);
//
                    }
                }
            });


        }
    }

    public My_Order_Adapter(List<My_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_Order_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_rv, parent, false);


        context = parent.getContext();

        return new My_Order_Adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(My_Order_Adapter.MyViewHolder holder, int position) {
        My_order_model mList = modelList.get(position);


        try {



            holder.tv_orderno.setText(mList.getSale_id());

            if (mList.getStatus().equals("0")) {
                holder.tv_status.setText(context.getResources().getString(R.string.pending));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
            } else if (mList.getStatus().equals("1")) {
                holder.tv_status.setText(context.getResources().getString(R.string.confirm));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
            } else if (mList.getStatus().equals("2")) {
                holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
            } else if (mList.getStatus().equals("4")) {
                holder.tv_status.setText(context.getResources().getString(R.string.delivered));
                holder.relativetextstatus.setText(context.getResources().getString(R.string.delivered));
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
            }
        } catch (Exception e) {

        }

            holder.tv_date.setText(mList.getOn_date());
            holder.tv_tracking_date.setText(mList.getOn_date());
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
            String timefrom=mList.getDelivery_time_from();
            String timeto=mList.getDelivery_time_to();

            timefrom=timefrom.replace("pm","م");
            timefrom=timefrom.replace("am","ص");

            timeto=timeto.replace("pm","م");
            timeto=timeto.replace("am","ص");

            String time=timefrom + "-" + timeto;

            holder.tv_time.setText(time);
        }else {

            String timefrom=mList.getDelivery_time_from();
            String timeto=mList.getDelivery_time_to();
            String time=timefrom + "-" + timeto;

            holder.tv_time.setText(time);

        }

        holder.tv_price.setText( mList.getTotal_amount()+context.getResources().getString(R.string.currency));
            holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
            holder.tv_socity.setText(mList.getSocityname());
            holder.tv_house.setText(mList.getHouse());
            holder.tv_recivername.setText(modelList.get(position).getRecivername());
            holder.tv_recivernumber.setText(mList.getRecivermobile());
        }


    @Override
    public int getItemCount() {
        return modelList.size();

    }

}
