package com.sii.numbers.core

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.net.UnknownHostException

abstract class BaseFragment : Fragment() {

    fun handleApiError(exception: Throwable?, callId: Int? = null) {
        activity?.runOnUiThread {
            when (exception) {
                is UnknownHostException -> onInternetConnectionError(callId)
                else -> Toast.makeText(context, "Unknown Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    open fun onInternetConnectionError(callId: Int?) = Unit

    fun showDialog(onConfirm: (() -> Unit)? = null, ) {
        AlertDialog.Builder(requireContext())
            .setTitle("ERROR")
            .setMessage("NO INTERNET CONNECTION")
            .setPositiveButton("try again") { _, _ -> onConfirm?.invoke() }
            .setNegativeButton("cancel") { _, _ -> }
            .show()

    }
}