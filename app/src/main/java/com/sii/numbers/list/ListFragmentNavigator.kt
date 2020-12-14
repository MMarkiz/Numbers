package com.sii.numbers.list

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.sii.numbers.R
import com.sii.numbers.core.Constants.DETAILS_NAME_ARGUMENT_KEY

class ListFragmentNavigator(private val fragment: ListFragment) {

    fun navigateToDetails(name: String) {
        fragment.run {
            val isTabletLandscape = context?.resources?.getBoolean(R.bool.isTabletLandscape)

            val bundle = Bundle()
            bundle.putString(DETAILS_NAME_ARGUMENT_KEY, name)

            if (isTabletLandscape == true) {
                val navHostFragment =
                    childFragmentManager.findFragmentById(R.id.details_fragment_container) as NavHostFragment

                navHostFragment.navController.setGraph(R.navigation.nav_graph_details, bundle)

            } else {
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToDetailsFragment(name)
                )
            }
        }
    }
}