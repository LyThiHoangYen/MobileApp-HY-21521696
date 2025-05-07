package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeHRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHRFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Task> taskList;
    private TaskUserAdapter mAdapter;

    public HomeHRFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeHRFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHRFragment newInstance(String param1, String param2) {
        HomeHRFragment fragment = new HomeHRFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        taskList = new ArrayList<Task>();
        taskList.add(new Task("Thêm nhân viên",R.drawable.task_themnhanvien,"Thêm nhân viên và chụp ảnh làm thẻ nhân viên"));
        taskList.add(new Task("Thông tin nhân viên",R.drawable.task_truyxuatthongtin,"Truy xuất thông tin nhân viên của bạn"));
        taskList.add(new Task("Thống kê báo cáo",R.drawable.task_baocao,"Thống kê, báo cáo tình trạng, số lượng nhân viên..."));
        taskList.add(new Task("Tạo thông báo",R.drawable.task_thongbao,"Tạo thông báo trên bảng tin"));

        mAdapter = new TaskUserAdapter(taskList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(mLayoutManager);

        return view;
    }
}