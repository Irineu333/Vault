package com.neo.vault.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.neo.vault.R
import com.neo.vault.databinding.ActivityMainBinding
import com.neo.vault.presentation.model.Summation
import com.neo.vault.presentation.ui.feature.showSummation.SummationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment
        get() = supportFragmentManager
            .findFragmentById(R.id.vaults_host) as NavHostFragment

    private val navController
        get() = navHostFragment.navController

    private val valuesFragment
        get() = supportFragmentManager
            .findFragmentById(R.id.container_values) as? SummationFragment

    private val NavDestination.isInitialFragment
        get() = id == R.id.homeVaults

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            binding.toolbar.navigationIcon =
                if (!destination.isInitialFragment)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_back, theme) else null

            binding.toolbar.title = destination.label
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    fun setSummation(summations: List<Summation>) {

        valuesFragment?.setValues(
            summations = summations
        )
    }
}