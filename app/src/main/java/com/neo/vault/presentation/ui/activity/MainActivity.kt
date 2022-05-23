package com.neo.vault.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        get() = binding.appbar.switcher.displayedChild == 1
        set(value) {
            val displayChild = if (value) 1 else 0

            val switcher = binding.appbar.switcher

            displayChild.takeIf { it != switcher.displayedChild }?.also {

                when(it) {
                    0 -> {
                        switcher.setInAnimation(this, R.anim.slide_in_left_short)
                        switcher.setOutAnimation(this, R.anim.slide_out_right_short)
                    }

                    1 -> {
                        switcher.setInAnimation(this, R.anim.slide_in_right_short)
                        switcher.setOutAnimation(this, R.anim.slide_out_left_short)
                    }
                }

                switcher.displayedChild = it
            }
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
                    ContextCompat.getDrawable(this, R.drawable.ic_back)
                else null

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