package com.sii.numbers.core

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sii.numbers.R
import java.net.UnknownHostException

abstract class BaseFragment : Fragment() {

    fun handleApiError(exception: Throwable?, callId: Int? = null) {
        activity?.runOnUiThread {
            when (exception) {
                is UnknownHostException -> onInternetConnectionError(callId)
                else -> Toast.makeText(context, getString(R.string.error_unknown_message), Toast.LENGTH_LONG).show()
            }
        }
    }

    open fun onInternetConnectionError(callId: Int?) = Unit

    fun showInternetConnectionErrorDialog(onConfirm: (() -> Unit)? = null, ) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error_title))
            .setMessage(R.string.error_internet_connection_message)
            .setPositiveButton(R.string.button_retry) { _, _ -> onConfirm?.invoke() }
            .setNegativeButton(R.string.button_cancel) { _, _ -> }
            .show()

    }
}