package com.shihs.tripmood.dialog

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.DialogPlanModeBinding

class PlanModeDialog : AppCompatDialogFragment() {

    lateinit var binding: DialogPlanModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPlanModeBinding.inflate(inflater, container, false)

        setBtn()

        return binding.root
    }

    private fun setBtn() {
        binding.relaxModeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_myGPSFragment)
        }

        binding.seriousModeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_global_createScheduleFragment)
        }
    }
}
