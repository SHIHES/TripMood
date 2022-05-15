package com.shihs.tripmood.plan.createplan

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.storage.FirebaseStorage
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentPlanCreateBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.ext.getVmFactory
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.UserManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer


class CreatePlanFragment : Fragment() {

    lateinit var binding : FragmentPlanCreateBinding

    private val viewModel by viewModels <CreatePlanViewModel> { getVmFactory() }

    lateinit var progressDialog : ProgressDialog

    var postPlan = Plan()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlanCreateBinding.inflate(inflater, container, false)


        progressDialog = ProgressDialog(requireContext())




        viewModel.imageStatus.observe(viewLifecycleOwner){it?.let {

            progressDialog.setMessage("Uploading file")
            progressDialog.setCancelable(false)

            when(it){

                LoadApiStatus.LOADING -> {
                    progressDialog.show()
                }

                LoadApiStatus.DONE -> {
                    Toast.makeText(requireContext(),"Upload Success", Toast.LENGTH_LONG).show()
                    binding.chooseCoverImage.visibility = View.INVISIBLE
                    progressDialog.dismiss()

                }

                LoadApiStatus.ERROR -> {
                    Toast.makeText(requireContext(),"Upload Fail", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }
            }
        }}

        viewModel.imageUriCallback.observe(viewLifecycleOwner){it?.let {
            Glide.with(requireContext()).load(it).centerCrop().into(binding.galleryImage)

            postPlan.image = it.toString()
        } }

        setCalendarBtn()

        return binding.root
    }

    private fun setCalendarBtn() {

        val constraintsBuilder = CalendarConstraints.Builder()
        val startDatePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setCalendarConstraints(constraintsBuilder.build())
            .build()



        binding.startDateBtn.setOnClickListener {

            val fmt = SimpleDateFormat("yyyy/MM/dd")
            val planDateRange = binding.startDateBtn

            startDatePicker.show(childFragmentManager,"tag")


                startDatePicker.apply {
                    addOnPositiveButtonClickListener {
                        planDateRange.setText("${fmt.format(it.first)} - ${fmt.format(it.second)} ")

                        postPlan.startDate = it.first
                        postPlan.endDate = it.second
                    }
                }


        }

        binding.createBtn.setOnClickListener{

            postPlan.title = binding.planET.text.toString()

            viewModel.postNewPlan(plan = postPlan)

            findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(postPlan))
        }

        binding.selectedPhoto.setOnClickListener {
            selectImage()
        }


    }

    private fun selectImage() {
        val intent = Intent().apply {
            setType("image/*")
            setAction(Intent.ACTION_GET_CONTENT)
        }
        startActivityForResult(intent, SELECED_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && data != null && data.data != null){

            val imageUri = data.data!!

            viewModel.uploadImage(imageUri)

            Log.d("SSSSS", "get uri ${imageUri}")

        }
    }

    companion object {

        private const val SELECED_IMAGE = 100

    }
}