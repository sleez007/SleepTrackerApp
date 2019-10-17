package com.example.android.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

//class SleepNightAdapter :RecyclerView.Adapter<TextItemViewHolder>() {

     class SleepNightAdapter(val clickListener: SleepNightListener) :ListAdapter<SleepNight,TextItemViewHolder>(SleepNightDiffCallback()) {
//    var data = listOf<SleepNight>()
//    set(value){
//        field = value
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder{
        return TextItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder:  TextItemViewHolder, position: Int) {
   //  val item = data[position]

        val item = getItem(position)
      //val res = holder.itemView.context.resources

       // holder.bind(item, res)

        holder.bind(item, clickListener)
    }


 //if us extend the list adapter class, you dont need to override the getItemCount
  //  override fun getItemCount(): Int =  data.size

}
