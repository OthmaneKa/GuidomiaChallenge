package com.othmanek.guidomiacars.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.databinding.FragmentHomeBinding
import com.othmanek.guidomiacars.domain.entity.Car
import com.othmanek.guidomiacars.presentation.ui.home.list.CarListAdapter
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: CarListAdapter

    @Inject
    lateinit var moshiAdapter: JsonAdapter<List<Car>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(binding.root, savedInstanceState)
        binding.headerContainer.apply {
            headerTitle.text = "Tacoma 2021"
            headerDescription.text = "Get your's now"
        }
        binding.recyclerView.apply {
            val mLayoutManager = LinearLayoutManager(context)
            mAdapter = CarListAdapter()
            adapter = mAdapter
            layoutManager = mLayoutManager
        }

        val jsonFile =
            resources.openRawResource(R.raw.car_list).bufferedReader().use { it.readText() }
        val data = moshiAdapter.fromJson(jsonFile)
        data?.map {
            it.setImage()
            if (data.indexOf(it) == 0) it.isExpanded = true
        }
        data?.let { mAdapter.updateList(it) }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}