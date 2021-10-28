package com.example.mohinhbtl;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private List<User> mListUser;
    private List<User> mListUserAll;
    private IClickItemUser iClickItemUser;




    public interface IClickItemUser{
        void updateUser(User user);
        void deleteUser(User user);
    }

    public UserAdapter(IClickItemUser iClickItemUser) {
        this.iClickItemUser = iClickItemUser;
    }

    public void setData(List<User> list){
        this.mListUser=list;
        this.mListUserAll=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user=mListUser.get(position);
        if(user==null){
            return;
        }
        holder.tvUsername.setText(user.getUsername());
        holder.tvAge.setText(user.getAge());
        holder.tvAddress.setText(user.getAddress());
        holder.tvCccd.setText(user.getCccd());
        holder.tvPhone.setText(user.getPhone());
        holder.tvDate.setText(user.getDate());
        holder.tvStatus.setText(user.getStatus());

//        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iClickItemUser.updateUser(user);
//            }
//        });
//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                iClickItemUser.deleteUser(user);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(mListUser != null){
            return  mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
private static final String TAG="MyViewHolder";
        private TextView tvUsername;
        private TextView tvAddress;
        private TextView tvCount;
        private TextView tvCccd;
        private TextView tvAge;
        private TextView tvPhone;
        private TextView tvDate;
        private TextView tvStatus;
        private Button btnUpdate;
        private Button btnDelete;
        private Button btnMenuac;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);


            tvUsername=itemView.findViewById(R.id.tv_username);
            tvAddress=itemView.findViewById(R.id.tv_address);
            tvCount=itemView.findViewById(R.id.tv_count);
            tvCccd=itemView.findViewById(R.id.tv_cccd);
            tvAge=itemView.findViewById(R.id.tv_age);
            tvPhone=itemView.findViewById(R.id.tv_phone);
            tvDate=itemView.findViewById(R.id.tv_date);
            tvStatus=itemView.findViewById(R.id.tv_status);
//            btnUpdate=itemView.findViewById(R.id.btn_update);
//            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnMenuac=itemView.findViewById(R.id.btn_menuactivity);
            btnMenuac.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private  void showPopupMenu(View view){
            PopupMenu popupMenu=new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.context_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            User user=mListUser.get(getAdapterPosition());
            switch (menuItem.getItemId()){
                case R.id.m_edit:
                    iClickItemUser.updateUser(user);
                    return  true;
                case R.id.m_delete:
                    iClickItemUser.deleteUser(user);
                    return true;

                default:
                    return false;

            }

        }
//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.add(this.getAdapterPosition(),121,0,"Update");
//            contextMenu.add(this.getAdapterPosition(),122,1,"Delete");
//            contextMenu.add(this.getAdapterPosition(),123,1,"Exit");
//        }


    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String strSearch=charSequence.toString();
                if(strSearch.isEmpty()){
                    mListUser=mListUserAll;
                }else{
                    List<User> list=new ArrayList<>();
                    for(User user:mListUserAll){
                        if(user.getDate().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(user);
                        }
                    }
                    mListUser=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=mListUser;
                    return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListUser= (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
