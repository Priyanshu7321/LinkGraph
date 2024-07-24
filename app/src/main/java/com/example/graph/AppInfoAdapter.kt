package com.example.graph

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AppInfoAdapter(private val context: Context,public val appInfoList: ArrayList<layout_item>) :
    RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder>() {
    inner class AppInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appIconImageView: ImageView =itemView.findViewById(R.id.appIcon)
        val appNameTextView: TextView = itemView.findViewById(R.id.appName)
        val dateTextView: TextView = itemView.findViewById(R.id.date)
        val totalClicksTextView: TextView = itemView.findViewById(R.id.totalClicks)
        val linkTextView: TextView = itemView.findViewById(R.id.link)
        val copyLinkButton: ImageButton = itemView.findViewById(R.id.copyLink)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppInfoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return AppInfoViewHolder(view)
    }
    override fun onBindViewHolder(holder: AppInfoViewHolder, position: Int) {
        val appInfo = appInfoList[position]
        holder.appNameTextView.text = appInfo.appName1
        holder.dateTextView.text = appInfo.date1
        holder.totalClicksTextView.text = appInfo.totalClicks1.toString()
        holder.linkTextView.text = appInfo.link1
        Glide.with(holder.itemView.context)
            .load(appInfo.uri1)
            .into(holder.appIconImageView)
        holder.copyLinkButton.setOnClickListener{
            copyTextToClipboard(position)
        }
    }

    override fun getItemCount(): Int = appInfoList.size
    fun updateData(newData: List<layout_item>) {
        appInfoList.clear()
        appInfoList.addAll(newData)
        notifyDataSetChanged()
    }
    private fun copyTextToClipboard(position: Int) {
        val text = appInfoList[position].link1 // Get the text from the list at the given position
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)

        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}