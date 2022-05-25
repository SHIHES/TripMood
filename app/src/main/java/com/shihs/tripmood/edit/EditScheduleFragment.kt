package com.shihs.tripmood.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shihs.tripmood.databinding.FragmentScheduleEditBinding
import com.shihs.tripmood.dataclass.Schedule
import java.text.SimpleDateFormat
import java.util.*

class EditScheduleFragment : Fragment() {

    lateinit var binding: FragmentScheduleEditBinding

    private val arg: EditScheduleFragmentArgs by navArgs()

    val schedule = Schedule()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleEditBinding.inflate(inflater, container, false)

        setupUI()
        setupBtn()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        val time = arg.selectedSchedule?.time
        val catalog = arg.selectedSchedule?.catalog
        val title = arg.selectedSchedule?.title
        val cost = arg.selectedSchedule?.cost
        val note = arg.selectedSchedule?.note
        val notificationStatus = arg.selectedSchedule?.notification
        val dayInt = arg.selectedSchedule?.theDay
        val location = arg.selectedSchedule?.location
        val planId = arg.selectedSchedule?.planID
        val scheduleID = arg.selectedSchedule?.scheduleId
        val address = arg.selectedSchedule?.location?.address

        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN).format(time)

        binding.editCatalogEditText.setText(catalog)
        binding.contentEditText.setText(note)
        binding.titleEditText.setText(title)
        binding.costEditText.setText(cost)
        binding.addressEditText.setText(address)
        binding.scheduleDayTv.text = "Day$dayInt"
        binding.scheduleDateTv.text = fmt
    }

    private fun setupBtn() {
        binding.editScheduleBtn.setOnClickListener {
        }

        binding.scheduleTimeTv.setOnClickListener {
        }
    }
}
