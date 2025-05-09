package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HrDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HrDashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView hrTaskRecyclerView;
    private RecyclerView announcementsRecyclerView;
    private TaskUserAdapter taskAdapter;
    private List<Task> taskList;
    private TextView tvViewAllAnnouncements;

    public HrDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HrDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HrDashboardFragment newInstance(String param1, String param2) {
        HrDashboardFragment fragment = new HrDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hr_dashboard, container, false);
        
        // Initialize views
        initializeViews(view);
        
        // Set up HR tasks
        setupHrTasks();
        
        // Set up announcements
        setupAnnouncements();
        
        // Set click listeners
        setupClickListeners();
        
        return view;
    }
    
    private void initializeViews(View view) {
        hrTaskRecyclerView = view.findViewById(R.id.hr_task_recycler_view);
        announcementsRecyclerView = view.findViewById(R.id.announcements_recycler_view);
        tvViewAllAnnouncements = view.findViewById(R.id.tv_view_all_announcements);
    }
    
    private void setupHrTasks() {
        taskList = new ArrayList<>();
        
        // HR specific tasks
        taskList.add(new Task("Thêm nhân viên", R.drawable.task_themnhanvien, "Thêm nhân viên và chụp ảnh làm thẻ nhân viên"));
        taskList.add(new Task("Quản lý nhân viên", R.drawable.task_truyxuatthongtin, "Quản lý thông tin nhân viên"));
        taskList.add(new Task("Báo cáo chấm công", R.drawable.task_tomtat, "Xem báo cáo chấm công của nhân viên"));
        taskList.add(new Task("Duyệt đơn nghỉ phép", R.drawable.task_duyetdon, "Duyệt đơn xin nghỉ phép của nhân viên"));
        taskList.add(new Task("Tạo thông báo", R.drawable.task_thongbao, "Tạo thông báo mới cho nhân viên"));
        taskList.add(new Task("Thống kê báo cáo", R.drawable.task_baocao, "Xem thống kê, báo cáo về nhân viên"));
        
        // Set up the RecyclerView
        taskAdapter = new TaskUserAdapter(taskList, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        hrTaskRecyclerView.setLayoutManager(gridLayoutManager);
        hrTaskRecyclerView.setAdapter(taskAdapter);
        hrTaskRecyclerView.setNestedScrollingEnabled(false);
    }
    
    private void setupAnnouncements() {
        // Sample code - you would replace this with actual data from Firebase
        List<Task> announcements = new ArrayList<>();
        announcements.add(new Task("Chính sách mới", R.drawable.task_thongbao, "Cập nhật chính sách công ty"));
        announcements.add(new Task("Sự kiện công ty", R.drawable.task_thongbao, "Thông báo về sự kiện sắp tới"));
        
        TaskUserAdapter announcementAdapter = new TaskUserAdapter(announcements, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        announcementsRecyclerView.setLayoutManager(linearLayoutManager);
        announcementsRecyclerView.setAdapter(announcementAdapter);
        announcementsRecyclerView.setNestedScrollingEnabled(false);
    }
    
    private void setupClickListeners() {
        tvViewAllAnnouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to announcements list
                Fragment fragment = new NewsfeedFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
} 