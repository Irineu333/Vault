package com.neo.vault.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
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

    var actionModeEnabled
        get() = binding.appbar.actionMode.isVisible
        set(value) {
            binding.appbar.actionMode.isVisible = value
            binding.appbar.toolbar.isVisible = !value
        }

    val actionMode get() = binding.appbar.actionMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->

            binding.appbar.toolbar.navigationIcon =
                if (!destination.isInitialFragment)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_back, theme) else null

            binding.appbar.toolbar.title = destination.label
        }

        binding.appbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.appbar.actionMode.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setSummation(summations: List<Summation>) {

        valuesFragment?.setValues(
            summations = summations
        )
    }
}