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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

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

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        taskList.add(new Task("Điểm danh/ Chấm công",R.drawable.task_qr,"Điểm danh bằng QR code"));
        taskList.add(new Task("Tóm tắt chấm công",R.drawable.task_tomtat,"Kiểm tra các kết quả chấm công"));
        taskList.add(new Task("Viết đơn xin nghỉ phép",R.drawable.task_vietdon,"Viết đơn xin nghỉ phép"));
        taskList.add(new Task("Tình trang đơn xin phép",R.drawable.task_tinhtrangdonxinphep,"Kiểm tra tình trạng các đơn xin nghỉ của mình"));
        taskList.add(new Task("Tạo thông báo",R.drawable.task_thongbao,"Tạo thông báo trên bảng tin"));
        taskList.add(new Task("Load Manager Fragment",R.drawable.task_thongbao,"Để tạm load màn hình của quản lý"));
        taskList.add(new Task("Load HR Fragment",R.drawable.task_thongbao,"Để tạm load màn hình của HR"));

        mAdapter = new TaskUserAdapter(taskList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(mLayoutManager);

        return view;

    }
}