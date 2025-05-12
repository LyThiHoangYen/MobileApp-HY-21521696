package com.example.mobileapp

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView

class HorizontalMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val menuContainer: LinearLayout
    private var onMenuItemClickListener: ((MenuItem) -> Unit)? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.horizontal_menu, this, true)
        menuContainer = findViewById(R.id.menu_container)
    }

    fun setMenu(menu: Menu) {
        menuContainer.removeAllViews()
        
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val itemView = LayoutInflater.from(context)
                .inflate(R.layout.horizontal_menu_item, menuContainer, false)
            
            val iconView = itemView.findViewById<ImageView>(R.id.menu_icon)
            val titleView = itemView.findViewById<TextView>(R.id.menu_title)
            
            iconView.setImageDrawable(menuItem.icon)
            titleView.text = menuItem.title
            
            itemView.setOnClickListener {
                onMenuItemClickListener?.invoke(menuItem)
            }
            
            menuContainer.addView(itemView)
        }
    }

    fun setOnMenuItemClickListener(listener: (MenuItem) -> Unit) {
        onMenuItemClickListener = listener
    }
} 