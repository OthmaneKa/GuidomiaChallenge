package com.othmanek.guidomiacars.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.common.Resource
import com.othmanek.guidomiacars.databinding.FragmentHomeBinding
import com.othmanek.guidomiacars.domain.entity.Car
import com.othmanek.guidomiacars.domain.entity.FilterValue
import com.othmanek.guidomiacars.presentation.ui.home.list.CarListAdapter
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: CarListAdapter
    private val viewModel: HomeViewModel by viewModels()
    private var filterValue = FilterValue()

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
        initView()

        collectList()
        populateSpinners()
        setMakeListener()
        setModelListeners()
    }

    private fun initView() {
        binding.headerContainer.apply {
            headerTitle.text = "Tacoma 2021"
            headerDescription.text = "Get your's now"
        }
        binding.recyclerView.apply {
            val mLayoutManager = LinearLayoutManager(context)
            mAdapter = CarListAdapter()
            adapter = mAdapter
            layoutManager = mLayoutManager
            val decoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            decoration.setDrawable(resources.getDrawable(R.drawable.bg_separator, null))
            addItemDecoration(decoration)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    private fun collectList() {
        lifecycleScope.launch {
            viewModel.getFilteredList().collect() {
                when (it) {
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> showLoading(true)
                    is Resource.Success -> {
                        showLoading(false)
                        mAdapter.updateList(it.data!!)
                    }
                }
            }
        }
    }

    private fun populateSpinners() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.modelFilterValues.collect { populateModelSpinnerWithEntries(it) }
            }
            launch {
                viewModel.makeFilterValues.collect { populateMakeSpinnerWithEntries(it) }
            }
        }
    }

    private fun populateMakeSpinnerWithEntries(entries: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            entries
        )
        binding.filtersContainer.cardFilterMakeSpinner.adapter = adapter
    }

    private fun populateModelSpinnerWithEntries(entries: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            entries
        )
        binding.filtersContainer.cardFilterModelSpinner.adapter = adapter
    }

    private fun setMakeListener() {
        binding.filtersContainer.cardFilterMakeSpinner.setSelection(0)
        binding.filtersContainer.cardFilterMakeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (binding.filtersContainer.cardFilterMakeSpinner.selectedItem.toString() != "Any Make")
                        filterValue.makeValue =
                            binding.filtersContainer.cardFilterMakeSpinner.selectedItem.toString()
                    else
                        filterValue.makeValue = ""

                    viewModel.updateFilterValue(filterValue)
                    collectList()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
    }

    private fun setModelListeners() {
        binding.filtersContainer.cardFilterModelSpinner.setSelection(0)
        binding.filtersContainer.cardFilterModelSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (binding.filtersContainer.cardFilterModelSpinner.selectedItem.toString() != "Any Model")
                        filterValue.modelValue =
                            binding.filtersContainer.cardFilterModelSpinner.selectedItem.toString()
                    else
                        filterValue.modelValue = ""
                    viewModel.updateFilterValue(filterValue)
                    collectList()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}