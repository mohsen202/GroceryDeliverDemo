package com.gogrocerdb.tccm.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.franmontiel.localechanger.LocaleChanger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gogrocerdb.tccm.Adapter.My_order_detail_adapter;
import com.gogrocerdb.tccm.AppController;
import com.gogrocerdb.tccm.Config.BaseURL;
import com.gogrocerdb.tccm.MainActivity;
import com.gogrocerdb.tccm.Model.My_order_detail_model;
import com.gogrocerdb.tccm.R;
import com.gogrocerdb.tccm.util.ConnectivityReceiver;
import com.gogrocerdb.tccm.util.CustomVolleyJsonArrayRequest;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {
    public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date;
    private String sale_id;
    RelativeLayout Mark_Delivered;
    private RecyclerView rv_detail_order;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.orderdetail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });


        rv_detail_order = (RecyclerView) findViewById(R.id.product_recycler);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_detail_order.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        tv_orderno = (TextView) findViewById(R.id.tv_order_no);
        tv_status = (TextView) findViewById(R.id.tv_order_status);
        relativetextstatus = (TextView) findViewById(R.id.status);
        tv_tracking_date = (TextView) findViewById(R.id.tracking_date);
        tv_date = (TextView) findViewById(R.id.tv_order_date);
        tv_time = (TextView) findViewById(R.id.tv_order_time);
        tv_price = (TextView) findViewById(R.id.tv_order_price);
        tv_item = (TextView) findViewById(R.id.tv_order_item);


        sale_id = getIntent().getStringExtra("sale_id");
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
        String placed_on = getIntent().getStringExtra("placedon");
        String time = getIntent().getStringExtra("time");
        String item = getIntent().getStringExtra("item");
        String amount = getIntent().getStringExtra("ammount");
        String stats = getIntent().getStringExtra("status");

        Mark_Delivered = (RelativeLayout) findViewById(R.id.btn_mark_delivered);
        Mark_Delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetail.this, GetSignature.class);
                intent.putExtra("sale", sale_id);
                startActivity(intent);

            }
        });

        if (stats.equals("0")) {
            tv_status.setText(getResources().getString(R.string.pending));
            relativetextstatus.setText(getResources().getString(R.string.pending));
        } else if (stats.equals("1")) {
            tv_status.setText(getResources().getString(R.string.confirm));
            relativetextstatus.setText(getResources().getString(R.string.confirm));
        } else if (stats.equals("2")) {
            tv_status.setText(getResources().getString(R.string.outfordeliverd));
            relativetextstatus.setText(getResources().getString(R.string.outfordeliverd));
        } else if (stats.equals("4")) {
            tv_status.setText(getResources().getString(R.string.delivered));
            Mark_Delivered.setVisibility(View.GONE);
            relativetextstatus.setText(getResources().getString(R.string.delivered));
        }

        tv_orderno.setText(sale_id);
        tv_date.setText(placed_on);
        tv_time.setText(time);
        tv_item.setText(item);
        tv_tracking_date.setText(placed_on);
        tv_price.setText(getResources().getString(R.string.currency) + amount);


    }

    private void makeGetOrderDetailRequest(String sale_id) {
        String tag_json_obj = "json_order_detail_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.OrderDetail, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(OrderDetail.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(OrderDetail.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


}
