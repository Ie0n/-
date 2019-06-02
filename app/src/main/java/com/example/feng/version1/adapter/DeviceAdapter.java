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

public class DeviceAdapter  extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<Equipment> mData;
    private Context mContext;

    public DeviceAdapter(Context context, List<Equipment> data) {
        mData = data;
        mContext = context;
    }


    public interface OnItemClickListener  {
        void onItemClick(View v,int position);
        void onImageClick(EditText name,int position);
        void onTextClick(View v,EditText ed,ImageView imageView,int position);
        void onItemLongClick(View v,int position,String id,String name);
    }

    private OnItemClickListener mOnItemClickListener;

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textID;
        EditText txtName;
        ImageView confirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textID = itemView.findViewById(R.id.text_device_id);
            txtName = itemView.findViewById(R.id.text_item_equipment_1);
            confirm = itemView.findViewById(R.id.img_confirm_in);
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_tab, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtName.setText(mData.get(position).getName());
        viewHolder.textID.setText(mData.get(position).getDeviceId());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(v,position,viewHolder.textID.getText().toString(),viewHolder.txtName.getText().toString());
                return true;
            }
        });
        viewHolder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onTextClick(v,viewHolder.txtName,viewHolder.confirm,position);
            }
        });
        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onImageClick(viewHolder.txtName,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



}
