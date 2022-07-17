package com.othmanek.guidomiacars.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private val viewModel: HomeViewModel by viewModels()
    private var data: List<Car> = emptyList()
    private var filterValue = mutableListOf("", "")

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
        getListFromJsonFile()
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
        }
    }

    private fun getListFromJsonFile() {
        val makeEntries: MutableList<String> = mutableListOf()
        val modelEntries: MutableList<String> = mutableListOf()

        val jsonFile =
            resources.openRawResource(R.raw.car_list).bufferedReader().use { it.readText() }
        val data = moshiAdapter.fromJson(jsonFile)

        data?.map {
            it.setImage()
            if (data.indexOf(it) == 0) it.isExpanded = true
        }
        this.data = data!!
        data.let { mAdapter.updateList(it) }
        data.forEach {
            makeEntries.add(it.make)
            modelEntries.add(it.model)
        }
        modelEntries.add(0, "Select a Model")
        makeEntries.add(0, "Select a Make")
        populateModelSpinnerWithEntries(modelEntries)
        populateMakeSpinnerWithEntries(makeEntries)
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
                    if (p2 != 0)
                        filterValue[0] = data[p2 - 1].make
                    else
                        filterValue[0] = ""

                    mAdapter.filterList(filterValue)

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
                    if (p2 != 0)
                        filterValue[1] = data[p2 - 1].model
                    else
                        filterValue[1] = ""
                    mAdapter.filterList(filterValue)
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