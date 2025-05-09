package com.example.assignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsfeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsfeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Announcement> announcementList;
    private AnnouncementAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvNoAnnouncements;
    private FloatingActionButton fabAddAnnouncement;
    private LinearLayout emptyView;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userRole = "";

    public NewsfeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsfeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsfeedFragment newInstance(String param1, String param2) {
        NewsfeedFragment fragment = new NewsfeedFragment();
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
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        // Ánh xạ view
        recyclerView = view.findViewById(R.id.recycler_view_announcements);
        progressBar = view.findViewById(R.id.progress_bar);
        tvNoAnnouncements = view.findViewById(R.id.tv_no_announcements);
        fabAddAnnouncement = view.findViewById(R.id.fab_add_announcement);
        emptyView = view.findViewById(R.id.empty_view);

        // Thiết lập RecyclerView
        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter(announcementList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Kiểm tra quyền người dùng
        checkUserRole();

        // Tải danh sách thông báo
        loadAnnouncements();

        // Thiết lập sự kiện click cho FAB
        fabAddAnnouncement.setOnClickListener(v -> {
            // Hiển thị dialog tạo thông báo mới
            showCreateAnnouncementDialog();
        });

        return view;
    }

    private void checkUserRole() {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            db.collection("employees").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            userRole = documentSnapshot.getString("role");
                            if (userRole == null) userRole = "";
                            
                            // Chỉ quản lý và HR mới có quyền tạo thông báo
                            boolean canCreateAnnouncement = userRole.equalsIgnoreCase("MANAGER") 
                                    || userRole.equalsIgnoreCase("HR");
                            fabAddAnnouncement.setVisibility(canCreateAnnouncement ? View.VISIBLE : View.GONE);
                        }
                    });
        }
    }

    private void loadAnnouncements() {
        showLoading(true);
        
        db.collection("announcements")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    showLoading(false);
                    announcementList.clear();
                    
                    if (queryDocumentSnapshots.isEmpty()) {
                        showEmptyView(true);
                        return;
                    }
                    
                    showEmptyView(false);
                    
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Announcement announcement = document.toObject(Announcement.class);
                        if (announcement != null) {
                            announcement.setId(document.getId());
                            announcementList.add(announcement);
                        }
                    }
                    
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    showEmptyView(true);
                    Toast.makeText(getContext(), "Lỗi khi tải thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    
    private void showCreateAnnouncementDialog() {
        CreateAnnouncementDialog dialog = new CreateAnnouncementDialog();
        dialog.setOnAnnouncementCreatedListener(() -> loadAnnouncements());
        dialog.show(getChildFragmentManager(), "create_announcement");
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
    
    private void showEmptyView(boolean isEmpty) {
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}