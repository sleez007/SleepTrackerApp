package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.TextItemViewBinding

class TextItemViewHolder(val binding: TextItemViewBinding): RecyclerView.ViewHolder(binding.root){

    companion object {
        fun from(parent: ViewGroup): TextItemViewHolder {
           // val itemView = LayoutInflater.from(parent.context).inflate(R.layout.text_item_view, parent, false)
            val layoutInflater =LayoutInflater.from(parent.context)
            val binding= TextItemViewBinding.inflate(layoutInflater,parent,false)
            return TextItemViewHolder(binding)
        }
    }


    fun bind(item: SleepNight, click:  SleepNightListener) {
//        binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//        binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
//
//        binding.qualityImage.setImageResource(when(item.sleepQuality) {
//            0 -> R.drawable.ic_sleep_0
//            1 -> R.drawable.ic_sleep_1
//            2 -> R.drawable.ic_sleep_2
//            3 -> R.drawable.ic_sleep_3
//            4 -> R.drawable.ic_sleep_4
//            5 -> R.drawable.ic_sleep_5
//            else -> R.drawable.ic_sleep_2
//        })

        //code updated to use databinding
        binding.sleep = item
        binding.clickListener = click
        binding.executePendingBindings()

    }


}

