package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 宣告元件變數
        val edName = findViewById<EditText>(R.id.edName)
        val edPhone = findViewById<EditText>(R.id.edPhone)
        val btnSend = findViewById<Button>(R.id.btnSend)

        // 接收 MainActivity 傳來的資料
        val position = intent.getIntExtra("position", -1)
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")

        // 如果是編輯模式，填入傳來的資料
        if (position != -1 && name != null && phone != null) {
            edName.setText(name)
            edPhone.setText(phone)
        }

        // 設定按鈕監聽器，取得輸入的姓名與電話
        btnSend.setOnClickListener {
            // 判斷是否輸入資料
            when {
                edName.text.isEmpty() -> showToast("請輸入姓名")
                edPhone.text.isEmpty() -> showToast("請輸入電話")
                else -> {
                    val b = Bundle().apply {
                        putString("name", edName.text.toString())
                        putString("phone", edPhone.text.toString())
                        putInt("position", position) // 加入位置索引
                    }
                    // 使用 setResult() 回傳聯絡人資料
                    setResult(Activity.RESULT_OK, Intent().putExtras(b))
                    finish()
                }
            }
        }
    }

    // 建立 showToast 方法顯示 Toast 訊息
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
