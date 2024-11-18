package com.example.lab8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(
    private val data: ArrayList<Contact>
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder 用來儲存和管理每個項目的視圖
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvName: TextView = v.findViewById(R.id.tvName)
        private val tvPhone: TextView = v.findViewById(R.id.tvPhone)
        private val imgDelete: ImageView = v.findViewById(R.id.imgDelete)

        // 綁定資料和視圖
        fun bind(item: Contact, deleteListener: (Contact) -> Unit, clickListener: (Int) -> Unit) {
            tvName.text = item.name
            tvPhone.text = item.phone

            // 設定刪除按鈕監聽器
            imgDelete.setOnClickListener {
                deleteListener.invoke(item)
            }

            // 設定項目點擊監聽器
            itemView.setOnClickListener {
                clickListener.invoke(adapterPosition)
            }
        }
    }

    // 建立ViewHolder並將其與布局關聯
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_row, viewGroup, false)
        return ViewHolder(v)
    }

    // 綁定資料到每個項目的視圖
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], { item ->
            // 刪除資料
            data.remove(item)
            notifyDataSetChanged() // 更新列表
        }, { position ->
            // 處理項目點擊
            onItemClickListener?.invoke(position)
        })
    }

    // 回傳資料項目的數量
    override fun getItemCount() = data.size

    // 用來處理項目點擊事件
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}
