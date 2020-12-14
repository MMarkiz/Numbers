package com.sii.numbers.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.navArgs
import com.sii.numbers.R
import com.sii.numbers.core.BaseFragment
import com.sii.numbers.core.Constants
import com.sii.numbers.core.Constants.GET_DATA_DETAILS_URL
import com.sii.numbers.data.ModelDetails
import com.sii.numbers.databinding.FragmentDetailsBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class DetailsFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Response> {

    lateinit var binding: FragmentDetailsBinding

    private val viewModel by viewModels<DetailsFragmentViewModel>()

    private val navigator by lazy { DetailsFragmentNavigator(this) }

    val fragmentArgs: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        if (viewModel.modelDetails.value?.name != fragmentArgs.name) {
            LoaderManager.getInstance(this)
                .initLoader(Constants.GET_DATA_DETAILS_LOADER_ID, null, this)
                .forceLoad()
        }

        if (context?.resources?.getBoolean(R.bool.isTabletLandscape) == true && viewModel.wasLastTimeTabletLandscape == false) {
            navigator.navigateToListFragment()
        }

        viewModel.wasLastTimeTabletLandscape = context?.resources?.getBoolean(R.bool.isTabletLandscape) == true

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Response?> {
        return object : AsyncTaskLoader<Response?>(requireContext()) {
            override fun loadInBackground(): Response? {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(GET_DATA_DETAILS_URL + fragmentArgs.name)
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
        if (response != null && response.isSuccessful && viewModel.modelDetails.value?.name != fragmentArgs.name) {
            try {
                val responseData: String = response.body()?.string().orEmpty()
                val modelDetails = Json.decodeFromString<ModelDetails>(responseData)
                viewModel.modelDetails.value = modelDetails
            } catch (e: IOException) {
                handleApiError(e)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Response>) = Unit

    override fun onInternetConnectionError(callId: Int?) {
        showInternetConnectionErrorDialog {
            LoaderManager.getInstance(this)
                .restartLoader(Constants.GET_DATA_DETAILS_LOADER_ID, null, this)
                .forceLoad()
        }
    }

    override fun onDestroyView() {
        LoaderManager.getInstance(this).initLoader(Constants.GET_DATA_DETAILS_LOADER_ID, null, this)
            .cancelLoad()

        super.onDestroyView()
    }
}