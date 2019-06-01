package com.example.feng.version1.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feng.version1.R;
import com.example.feng.version1.bean.Equipment;
import com.example.feng.version1.bean.StatusResponse;

import java.util.List;

public class DeviceAdapter  extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{

    private LayoutInflater inflater;
    private List<Equipment> mData;
    private DeviceAdapter.OnItemListener onItemListener;
    private DeviceAdapter.onItemLongClickListener onItemLongClickListener;

    public DeviceAdapter(Context context, List<Equipment> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

        private TextView textID;
        private EditText txtName;

        public ViewHolder(@NonNull View itemView,OnItemListener listener, DeviceAdapter.onItemLongClickListener onItemLongClick) {
            super(itemView);
            onItemListener = listener;
            onItemLongClickListener = onItemLongClick;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            textID = itemView.findViewById(R.id.text_device_id);
            txtName = itemView.findViewById(R.id.text_item_equipment_1);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text_item_equipment_1:{

                    break;
                }
                default:{
                    onItemListener.onItemClick(v, getPosition());
                    break;
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(v,getPosition(),textID.getText().toString(),txtName.getText().toString());
            return true;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_select_tab, viewGroup, false);
        return new ViewHolder(view,onItemListener,onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,int position) {
        viewHolder.txtName.setText(mData.get(position).getName());
        viewHolder.textID.setText(mData.get(position).getDeviceId());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemListener(DeviceAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        void onItemClick(View view, int position);
    }
    public interface onItemLongClickListener{
        void onItemLongClick(View view,int position,String id,String name);
    }
    public void setOnItemLongClickListener(DeviceAdapter.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
