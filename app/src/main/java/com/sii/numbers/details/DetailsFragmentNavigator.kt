package com.sii.numbers.details

import androidx.navigation.fragment.findNavController
import com.sii.numbers.R

class DetailsFragmentNavigator(private val fragment: DetailsFragment) {

    fun navigateToListFragment() {
        fragment.run {
            findNavController().popBackStack(R.id.nav_graph, true);
            findNavController().navigate(R.id.ListFragment)
        }
    }
}