package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDashboardActivity extends AppCompatActivity {
    
    protected LinearLayout menuContainer;
    protected LinearLayout homeNav, messageNav, statsNav, profileNav;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_template);
        
        // Initialize views
        menuContainer = findViewById(R.id.menu_container);
        homeNav = findViewById(R.id.homeNav);
        messageNav = findViewById(R.id.messageNav);
        statsNav = findViewById(R.id.statsNav);
        profileNav = findViewById(R.id.profileNav);
        
        // Set dashboard title
        TextView headerTitle = findViewById(R.id.headerTitle);
        TextView textTitle = findViewById(R.id.textTitle);
        headerTitle.setText(getDashboardTitle());
        textTitle.setText(getDashboardTitle());
        
        // Set up bottom navigation
        setupBottomNavigation();
        
        // Initialize menu items
        setupMenuItems();
    }
    
    // Method to be implemented by subclasses to provide dashboard title
    protected abstract String getDashboardTitle();
    
    // Method to be implemented by subclasses to add menu items
    protected abstract List<MenuItem> getMenuItems();
    
    // Set up menu items for the dashboard
    private void setupMenuItems() {
        List<MenuItem> menuItems = getMenuItems();
        
        LayoutInflater inflater = getLayoutInflater();
        for (MenuItem item : menuItems) {
            View menuItemView = inflater.inflate(R.layout.dashboard_menu_item, menuContainer, false);
            
            // Set menu item data
            ImageView icon = menuItemView.findViewById(R.id.menuItemIcon);
            TextView title = menuItemView.findViewById(R.id.menuItemTitle);
            TextView description = menuItemView.findViewById(R.id.menuItemDescription);
            CardView card = menuItemView.findViewById(R.id.menuItemCard);
            
            icon.setImageResource(item.getIconResource());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            
            // Set click listener
            card.setOnClickListener(v -> {
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getActivityClass() != null) {
                    Intent intent = new Intent(this, item.getActivityClass());
                    startActivity(intent);
                }
            });
            
            // Add to container
            menuContainer.addView(menuItemView);
        }
    }
    
    // Set up bottom navigation
    private void setupBottomNavigation() {
        statsNav.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        });
        
        // Other navigation items can be implemented by subclasses
    }
    
    // Inner class for menu items
    public static class MenuItem {
        private final String title;
        private final String description;
        private final int iconResource;
        private final Class<?> activityClass;
        
        public MenuItem(String title, String description, int iconResource, Class<?> activityClass) {
            this.title = title;
            this.description = description;
            this.iconResource = iconResource;
            this.activityClass = activityClass;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getIconResource() {
            return iconResource;
        }
        
        public Class<?> getActivityClass() {
            return activityClass;
        }
    }
} 