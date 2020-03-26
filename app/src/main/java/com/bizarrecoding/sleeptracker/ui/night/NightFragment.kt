package com.bizarrecoding.sleeptracker.ui.night

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bizarrecoding.sleeptracker.database.AlarmDB
import com.bizarrecoding.sleeptracker.database.AlarmRepositoryImpl
import com.bizarrecoding.sleeptracker.databinding.FragmentNightBinding
import com.bizarrecoding.sleeptracker.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_night.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class NightFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val factory: ViewModelFactory by instance()

    private val vmodel: NightViewModel by lazy {
        ViewModelProvider(this, factory).get(NightViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNightBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.model = vmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmodel.night.observe(viewLifecycleOwner, Observer { night->
            if (night==null){
                vmodel.cancelNight()
            }
        })

        vmodel.turnBack.observe(viewLifecycleOwner, Observer {
            if (it) findNavController().popBackStack()
        })
        cancelBtn.setOnClickListener {
            vmodel.cancelNight()
        }
        finishBtn.setOnClickListener {
            vmodel.updateNight()
        }
    }
}
