package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var myAdapter: MyAdapter
    private val contacts = ArrayList<Contact>()

    // 用來處理來自 SecActivity 的結果
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val name = intent?.getStringExtra("name") ?: ""
            val phone = intent?.getStringExtra("phone") ?: ""
            val position = intent?.getIntExtra("position", -1) ?: -1

            if (position != -1) {
                // 更新聯絡人資料
                contacts[position] = Contact(name, phone)
                myAdapter.notifyItemChanged(position) // 更新指定位置的項目
            } else {
                // 新增聯絡人
                contacts.add(Contact(name, phone))
                myAdapter.notifyDataSetChanged() // 刷新列表
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定視窗邊界
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 設定 RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        // 初始化 Adapter
        myAdapter = MyAdapter(contacts).apply {
            setOnItemClickListener { position ->
                val contact = contacts[position]
                val intent = Intent(this@MainActivity, SecActivity::class.java).apply {
                    putExtra("position", position)
                    putExtra("name", contact.name)
                    putExtra("phone", contact.phone)
                }
                startForResult.launch(intent)
            }
        }

        recyclerView.adapter = myAdapter

        // 點擊按鈕新增聯絡人
        btnAdd.setOnClickListener {
            val intent = Intent(this, SecActivity::class.java)
            startForResult.launch(intent)
        }
    }
}
