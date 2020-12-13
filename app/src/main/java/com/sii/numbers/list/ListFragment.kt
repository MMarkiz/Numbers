package com.sii.numbers.list

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.navGraphViewModels
import com.sii.numbers.R
import com.sii.numbers.core.BaseFragment
import com.sii.numbers.core.Constants.GET_DATA_LOADER_ID
import com.sii.numbers.core.Constants.GET_DATA_URL
import com.sii.numbers.data.Model
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class ListFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Response> {

    private val viewModel by navGraphViewModels<ListFragmentViewModel>(R.id.nav_graph)

    private val dataListAdapter by lazy { DataListAdapter(::onItemSelected) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        list.adapter = dataListAdapter

        setupObservers()
        if (viewModel.dataModels.value == null) {
            LoaderManager.getInstance(this).initLoader(GET_DATA_LOADER_ID, null, this).forceLoad()
        }
    }

    private fun setupObservers() {
        viewModel.dataModels.observe(viewLifecycleOwner, Observer {
            dataListAdapter.submitList(it)
        })

        viewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            dataListAdapter.updateSelectedItem(it)
        })
    }

    private fun onItemSelected(model: Model) {
        viewModel.selectedItem.value = model
        //TODO navigation
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Response?> {
        return object : AsyncTaskLoader<Response?>(requireContext()) {
            override fun loadInBackground(): Response? {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(GET_DATA_URL)
                    .build()
                return try {
                    client.newCall(request).execute()
                } catch (e: IOException) {
                    handleApiError(e)
                    null
                }
            }

        }
    }

    override fun onLoadFinished(loader: Loader<Response>, response: Response?) {
        if (response != null && response.isSuccessful) {
            try {
                val responseData: String = response.body()?.string().orEmpty()
                val dataModels = Json.decodeFromString<List<Model>>(responseData)
                viewModel.dataModels.value = dataModels
            } catch (e: IOException) {
                handleApiError(e)
            }
        }
    }

    override fun onInternetConnectionError(callId: Int?) {
        showDialog {
            LoaderManager.getInstance(this).restartLoader(GET_DATA_LOADER_ID, null, this).forceLoad()
        }
    }

    override fun onLoaderReset(loader: Loader<Response>) = Unit

    override fun onDestroyView() {
        LoaderManager.getInstance(this).initLoader(GET_DATA_LOADER_ID, null, this).cancelLoad()
        list.adapter = null
        super.onDestroyView()
    }
}