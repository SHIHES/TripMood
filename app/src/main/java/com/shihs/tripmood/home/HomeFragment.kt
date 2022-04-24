package com.shihs.tripmood.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.R
import com.shihs.tripmood.databinding.DialogPlanFuctionMoreBinding
import com.shihs.tripmood.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recyclerView = binding.planRV
        val adapter = MyPlanAdapter( MyPlanAdapter.OnClickListener{
            viewModel.navigateToDetail(it)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.selectedPlan.observe(viewLifecycleOwner){
            it?.let{
                findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(it))
                viewModel.onPlanNavigated()
            }
        }

        viewModel.livePlans.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {

        val dialog = Dialog(requireContext())
        val binding = DialogPlanFuctionMoreBinding.inflate(LayoutInflater.from(context))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_plan_fuction_more)

        binding.deleteLayout
        binding.privateLayout
        binding.openLayout
        binding.friendListLayout
        binding.editLayout
        binding.shareLayout

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.show()

    }
}