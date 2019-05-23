package com.example.feng.version1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.version1.R;
import com.example.feng.version1.bean.DeviceReadyInput;

import java.util.List;

public class DeviceInputAdapter  extends RecyclerView.Adapter<DeviceInputAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<DeviceReadyInput> mData;

    public DeviceInputAdapter(Context context, List<DeviceReadyInput> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textSiteName,textMeterNo,textDeviceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDeviceName = itemView.findViewById(R.id.txt_device_name);
            textMeterNo = itemView.findViewById(R.id.txt_meter_num);
            textSiteName = itemView.findViewById(R.id.txt_site_name);
        }

    }
    @NonNull
    @Override
    public DeviceInputAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_device_ready_input, viewGroup, false);
        return new DeviceInputAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceInputAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.textSiteName.setText(mData.get(position).getSite());
        viewHolder.textMeterNo.setText(mData.get(position).getMeterNo());
        viewHolder.textDeviceName.setText(mData.get(position).getDeviceName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}