package com.shihs.tripmood.home.childpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.appworks.school.publisher.ext.getVmFactory
import com.shihs.tripmood.databinding.FragmentPlanChildViewpagerBinding
import com.shihs.tripmood.home.HomeFragmentDirections
import com.shihs.tripmood.home.adapter.MyPlanAdapter

class ChildFragment : Fragment() {

    lateinit var binding: FragmentPlanChildViewpagerBinding

    private val viewModel by viewModels<ChildHomeViewModel> { getVmFactory() }

    companion object {
        fun newInstance(position: Int): Fragment {
            val childFragment = ChildFragment()
            val ARG_OBJECT = "object"
            val bundle = Bundle()

            bundle.putInt(ARG_OBJECT, position)
            childFragment.setArguments(bundle)
            return childFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlanChildViewpagerBinding.inflate(inflater, container, false)

        val recyclerPlan = binding.planRV

        val adapter = MyPlanAdapter(MyPlanAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
        })

        recyclerPlan.adapter = adapter
        recyclerPlan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        viewModel.selectedPlan.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.actionGlobalMyPlanFragment(it))
                viewModel.onPlanNavigated()
            }
        }

        viewModel.livePlans.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }



        return binding.root
    }
}