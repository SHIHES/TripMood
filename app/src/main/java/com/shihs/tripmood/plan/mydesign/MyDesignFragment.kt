package com.shihs.tripmood.plan.mydesign

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.shihs.tripmood.databinding.FragmentPlanMydesignBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MyDesignFragment : Fragment() {

    lateinit var binding : FragmentPlanMydesignBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanMydesignBinding.inflate(inflater, container, false)




        setCalendarBtn()


        return binding.root
    }

    private fun setCalendarBtn() {

        binding.startDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val startDatePicker = DatePickerDialog( requireActivity(), { view, mYear, mMonth, mDay ->
                binding.startDateBtn.text = " ${mYear} / $mMonth / $mDay"
            }, year, month, day)

            startDatePicker.show()
        }

        binding.endDateBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfweek = calendar.get(Calendar.DAY_OF_WEEK)

            val startDatePicker = DatePickerDialog( requireActivity(), { view, mYear, mMonth, mDay->
                binding.startDateBtn.text = " ${mYear} / $mMonth / $mDay /n $dayOfweek"
            }, year, month, day)

            startDatePicker.show()

        }

    }
}