package com.bizarrecoding.sleeptracker.ui.stats

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizarrecoding.sleeptracker.databinding.NightItemBinding
import com.bizarrecoding.sleeptracker.models.Night

class NightsAdapter(ctx: Context): RecyclerView.Adapter<NightHolder>() {

    private val inflater = LayoutInflater.from(ctx)
    private var data: List<Night> = listOf()

    fun updateDataSet(newData: List<Night>){
        data = newData
        Log.d("HRK_NIGHT", "Count: ${newData.size}")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NightHolder {
        val binding = NightItemBinding.inflate(inflater, parent,false)
        return NightHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: NightHolder, position: Int) {
        val night = data[position]
        holder.bind(night)
    }
}

class NightHolder(private  val binding: NightItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(night: Night){
        with(binding){
            this.night = night
            executePendingBindings()
        }
    }
}