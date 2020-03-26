package com.bizarrecoding.sleeptracker.ui.alarmList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bizarrecoding.sleeptracker.databinding.AlarmItemBinding
import com.bizarrecoding.sleeptracker.models.AlarmWithDays
import kotlinx.android.synthetic.main.alarm_item.view.*

class AlarmListAdapter(ctx: Context): RecyclerView.Adapter<AlarmViewHolder>() {

    private var alarms = emptyList<AlarmWithDays>()
    private val inflater = LayoutInflater.from(ctx)
    var deleteListener: AlarmClickListener? = null


    override fun getItemCount(): Int = alarms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = AlarmItemBinding.inflate(inflater,parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarms[position]
        holder.bind(alarm)
        holder.itemView.delete.setOnClickListener {
            deleteListener?.onClickDelete(alarm)
        }
    }

    fun setAlarms(currentAlarms: List<AlarmWithDays>){
        alarms = currentAlarms
        notifyDataSetChanged()
    }

    class AlarmClickListener(val clickListener: (alarm: AlarmWithDays)->Unit){
        fun onClickDelete(alarm: AlarmWithDays) = clickListener(alarm)
    }
}
class AlarmViewHolder(private val binding: AlarmItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(alarm: AlarmWithDays){
        binding.CurrentAlarm
        with(binding){
            this.model = alarm
            binding.CurrentAlarm.setOnClickListener {
                val action = AlarmListFragmentDirections.toAlarmEdit(alarm.alarm.id_alarm)
                val extras = FragmentNavigatorExtras(
                    time to "text_time "+alarm.alarm.id_alarm
                )
                it.findNavController().navigate(action, extras)
            }
            executePendingBindings()
        }
    }
}