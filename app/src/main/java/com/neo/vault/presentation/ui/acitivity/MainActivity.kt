package com.neo.vault.presentation.ui.acitivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.neo.vault.R
import com.neo.vault.databinding.ActivityMainBinding
import com.neo.vault.presentation.model.Value
import com.neo.vault.presentation.ui.feature.showValues.ShowValuesFragment
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
            .findFragmentById(R.id.container_values) as? ShowValuesFragment

    private val NavDestination.isInitialFragment
        get() = id == R.id.homeVaults

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        navController.addOnDestinationChangedListener { controller, destination, _ ->

            binding.toolbar.navigationIcon =
                if (!destination.isInitialFragment)
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_back, theme)
            else null

            valuesFragment?.setValues(
                when (destination.id) {
                    R.id.homeVaults -> {
                        listOf(
                            Value.Total(
                                value = 3000.0
                            ),
                            Value.SubTotal(
                                title = "Metas",
                                value = 1500.0,
                                action = {
                                    controller.navigate(R.id.action_homeVaults_to_piggyBankVaults)
                                }
                            ),
                            Value.SubTotal(
                                title = "Cofrinhos",
                                value = 1500.0,
                                action = {

                                }
                            ),
                        )
                    }
                    else -> listOf(
                        Value.Total(
                            value = 0.0
                        )
                    )
                }
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}