package com.shihs.tripmood.plan.createplan

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentPlanCreateBinding
import com.shihs.tripmood.dataclass.Plan
import java.text.SimpleDateFormat
import java.util.*


class CreatePlanFragment : Fragment() {

    lateinit var binding : FragmentPlanCreateBinding

    private val viewModel by viewModels <CreatePlanViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanCreateBinding.inflate(inflater, container, false)


        setCalendarBtn()

        return binding.root
    }

    private fun setCalendarBtn() {
//        val time = LocalDate.now().dayOfWeek.name
//        val sss = LocalDate.now().
        val calendar = Calendar.getInstance()
        val time = calendar.time
        val fmt = SimpleDateFormat("EE", Locale.ENGLISH).format(time.time)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.startDateBtn.text = "$year-$fmt\n${month + 1}-$day"
        binding.endDateBtn.text = "$year-$fmt\n${month + 1}-$day"


        binding.startDateBtn.setOnClickListener {

            val startDatePicker = DatePickerDialog( requireActivity(), { view, mYear, mMonth, mDay ->
                binding.startDateBtn.text = "${mYear}-${mMonth + 1}-$mDay"
            }, year, month, day)

            startDatePicker.show()
        }

        binding.endDateBtn.setOnClickListener {

            val startDatePicker = DatePickerDialog( requireActivity(), { view, mYear, mMonth, mDay->
                binding.endDateBtn.text = "${mYear}-${mMonth + 1}-$mDay"
            }, year, month, day)

            startDatePicker.show()

        }

        binding.createBtn.setOnClickListener{

            val formater = SimpleDateFormat("yyyy-MM-dd")
            val start = binding.startDateBtn.text.toString().let { formater.parse(it) }?.time
            val end = binding.endDateBtn.text?.toString().let { formater.parse(it) }?.time
            val plan = Plan(binding.planET.text.toString(), start, end )
            viewModel.postNewPlan(plan = plan)
            findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(plan))
        }

    }
}