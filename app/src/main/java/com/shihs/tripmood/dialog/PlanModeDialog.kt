package com.shihs.tripmood.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.DialogPlanModeBinding

class PlanModeDialog : DialogFragment() {

    lateinit var binding: DialogPlanModeBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogPlanModeBinding.inflate(inflater, container, false)

        setBtn()

        return binding.root
    }

    private fun setBtn() {
        binding.relaxModeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_myGPSFragment)
        }

        binding.seriousModeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_myDesignFragment)

        }
    }
}