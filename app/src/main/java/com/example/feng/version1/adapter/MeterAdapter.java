package com.example.feng.version1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.version1.R;
import com.example.feng.version1.bean.Meter;

import java.util.List;

public class MeterAdapter  extends RecyclerView.Adapter<MeterAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Meter> mData;
    private MeterAdapter.OnItemListener onItemListener;
    private MeterAdapter.onItemLongClickListener onItemLongClickListener;

    public MeterAdapter(Context context, List<Meter> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

        private TextView txtName,textID;

        public ViewHolder(@NonNull View itemView, OnItemListener listener, MeterAdapter.onItemLongClickListener onItemLongClick) {
            super(itemView);
            onItemListener = listener;
            itemView.setOnClickListener(this);
            onItemLongClickListener = onItemLongClick;
            itemView.setOnLongClickListener(this);
            txtName = itemView.findViewById(R.id.text_item_equipment_1);
            textID = itemView.findViewById(R.id.text_device_id);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.text_item_equipment:{
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
            onItemLongClickListener.onItemLongClick(v,getPosition(),textID.getText().toString());
            return true;
        }
    }
    @NonNull
    @Override
    public MeterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_select_tab, viewGroup, false);
        return new MeterAdapter.ViewHolder(view,onItemListener,onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MeterAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.txtName.setText(mData.get(position).getMeterName());
        viewHolder.textID.setText(mData.get(position).getMeterId());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemListener(MeterAdapter.OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        void onItemClick(View view, int position);
    }
    public interface onItemLongClickListener{
        void onItemLongClick(View view,int position,String id);
    }
    public void setOnItemLongClickListener(MeterAdapter.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
}
