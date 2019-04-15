package com.example.feng.version1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feng.version1.R;
import com.example.feng.version1.bean.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<User> mData;
    private OnItemListener onItemListener;
    private Context mContext;

    public UserAdapter(Context context, List<User> data) {
        mData = data;
        mContext = context;
        inflater = LayoutInflater.from(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName,userId;

        public ViewHolder(@NonNull View itemView, OnItemListener listener) {
            super(itemView);
            onItemListener = listener;
            itemView.setOnClickListener(this);
            userName = itemView.findViewById(R.id.text_item_user_name);
            userId = itemView.findViewById(R.id.text_item_user_id);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(v, getPosition());
        }
    }
    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_user, viewGroup, false);
        return new ViewHolder(view,onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.userName.setText(mData.get(position).getUserName());
        viewHolder.userId.setText(String.valueOf(mData.get(position).getuserNo()));
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
