package com.example.feng.version1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.version1.R;
import com.example.feng.version1.bean.Equipment;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Equipment> mData;
    private OnItemListener onItemListener;

    public EquipmentAdapter(Context context, List<Equipment> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private EditText txtName;

        public ViewHolder(@NonNull View itemView,OnItemListener listener) {
            super(itemView);
            onItemListener = listener;
            itemView.setOnClickListener(this);
            txtName = itemView.findViewById(R.id.text_item_equipment);
            txtName.setOnClickListener(this);
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
    }
    @NonNull
    @Override
    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_equipment, viewGroup, false);
        return new ViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipmentAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.txtName.setText(mData.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public interface OnItemListener{
        void onItemClick(View view, int position);
    }
}
