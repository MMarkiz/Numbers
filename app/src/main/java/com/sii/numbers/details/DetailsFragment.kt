package com.sii.numbers.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.sii.numbers.R
import com.sii.numbers.core.BaseFragment

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : BaseFragment() {

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		view.findViewById<Button>(R.id.button_second).setOnClickListener {
			findNavController().navigate(R.id.action_DetailsFragment_to_ListFragment)
		}
	}
}