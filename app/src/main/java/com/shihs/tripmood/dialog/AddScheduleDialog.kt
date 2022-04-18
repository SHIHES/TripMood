package com.shihs.tripmood.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.shihs.tripmood.databinding.DialogAddScheduleBinding
import com.shihs.tripmood.databinding.DialogPlanModeBinding
import com.shihs.tripmood.home.HomeViewModel
import com.shihs.tripmood.plan.MyPlanViewModel

class AddScheduleDialog : DialogFragment() {

    lateinit var binding: DialogAddScheduleBinding
    lateinit var viewModel:MyPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAddScheduleBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        viewModel = ViewModelProvider(requireActivity()).get(MyPlanViewModel::class.java)


        return binding.root
    }
}