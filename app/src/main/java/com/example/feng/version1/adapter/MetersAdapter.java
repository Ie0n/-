package com.example.feng.version1.adapter;

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
import com.example.feng.version1.bean.StatusResponse;

import java.util.List;

public class MetersAdapter extends RecyclerView.Adapter<MetersAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<StatusResponse.DataBean.MetersBean> mData;
    private OnItemListener onItemListener;
    private Context mContext;

    public MetersAdapter(Context context, List<StatusResponse.DataBean.MetersBean> data) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtName;
        private ImageView confirm;

        public ViewHolder(@NonNull View itemView,OnItemListener listener) {
            super(itemView);
            onItemListener = listener;
            itemView.setOnClickListener(this);
            txtName = itemView.findViewById(R.id.text_item_equipment);
            txtName.setOnClickListener(this);
            confirm = itemView.findViewById(R.id.img_confirm_in);
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_select_tab, viewGroup, false);
        return new ViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.txtName.setText(mData.get(position).getMeterName());

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

    public void setConfirmVisible(int position){

    }
}
