package com.shihs.tripmood.plan.createplan

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.appworks.school.publisher.ext.getVmFactory
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.shihs.tripmood.MobileNavigationDirections
import com.shihs.tripmood.databinding.FragmentPlanCreateBinding
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.UserManager
import java.text.SimpleDateFormat
import java.util.*


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

        setCalendarBtn()


        viewModel.imageStatus.observe(viewLifecycleOwner){it?.let {

            progressDialog.setMessage("Uploading file")
            progressDialog.setCancelable(false)

            when(it){

                LoadApiStatus.LOADING -> {
                    progressDialog.show()
                }

                LoadApiStatus.DONE -> {
                    Toast.makeText(requireContext(),"Upload Success", Toast.LENGTH_LONG).show()
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

        return binding.root
    }

    private fun setCalendarBtn() {
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

            

            viewModel.postNewPlan(plan = postPlan)
            findNavController().navigate(MobileNavigationDirections.actionGlobalMyPlanFragment(plan))
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