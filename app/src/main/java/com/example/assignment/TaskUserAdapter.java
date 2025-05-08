package com.example.assignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskUserAdapter extends RecyclerView.Adapter <TaskUserAdapter.MyViewHolder> {

    private List<Task> userTask;
    private Context context;
    private MyViewHolder holder;
    private int position;


    public TaskUserAdapter(List<Task> usertask, Context rcontext){
        this.userTask = usertask;
        this.context = rcontext;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_recycleview, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Task task = userTask.get(position);
        holder.taskName.setText(task.getTaskName());
        holder.taskDescription.setText((task.getDescription()));
        holder.taskImage.setImageResource(task.getPathImage());

    }

    @Override
    public int getItemCount() {
        return userTask.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName, taskDescription;
        public ImageView taskImage;



        public MyViewHolder(View view){
            super(view);
            taskName = view.findViewById(R.id.task_name);
            taskDescription = view.findViewById(R.id.task_description);
            taskImage = view.findViewById(R.id.task_image);

            CardView card;
            card = view.findViewById(R.id.card_view);
            card.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Toast.makeText(v.getContext(), taskName.getText().toString(), Toast.LENGTH_LONG).show();
                    switch (taskName.getText().toString()) {
                        case "Điểm danh/ Chấm công": {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), DiemDanhActivity.class);
                            activity.startActivity(intent);
                            break;
                        }
                        case "Tóm tắt chấm công": {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), tom_tat__diem_danh.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Viết đơn xin nghỉ phép":{
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), donxinphep.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Tình trang đơn xin phép":{
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), TinhTrangDonPhep.class);
                            activity.startActivity(intent);
                            break;
                        }
                        case "Tạo thông báo": {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), ThongBaoActivity.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Thống kê báo cáo":{
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), ThongKeBaoCaoActivity.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Thêm nhân viên": {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), ThemNhanVienActivity.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Thông tin nhân viên":{
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Intent intent = new Intent(v.getContext(), DanhBaNhanSuActivity.class);
                            activity.startActivity(intent);
                            //finish();
                            break;
                        }
                        case "Load Manager Fragment": {
                            Fragment fragment;
                            fragment = new HomeManagerFragment();
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        }
                        case "Load HR Fragment":{
                            Fragment fragment;
                            fragment = new HomeHRFragment();
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_container, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        }
                    }

                }
            });

        }





    }

}
