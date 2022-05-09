package com.shihs.tripmood.collect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shihs.tripmood.databinding.FragmentCollectionBinding

class CollectionFragment : Fragment() {

    lateinit var binding: FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCollectionBinding.inflate(inflater, container, false)




        return binding.root
    }
}