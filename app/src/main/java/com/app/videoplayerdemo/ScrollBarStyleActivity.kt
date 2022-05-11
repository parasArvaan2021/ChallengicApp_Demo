package com.app.videoplayerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollBarStyleActivity : AppCompatActivity() {

    private lateinit var rvData:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_bar_style)

        rvData=findViewById(R.id.rvData)
        rvData.layoutManager=LinearLayoutManager(this)
        rvData.adapter=RecyclerAdapter(this)
        rvData.setHasFixedSize(true)
    }
}