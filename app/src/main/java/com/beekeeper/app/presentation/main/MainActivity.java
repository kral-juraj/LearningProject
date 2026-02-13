package com.beekeeper.app.presentation.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.beekeeper.app.R;
import com.beekeeper.app.databinding.ActivityMainBinding;
import com.beekeeper.app.presentation.base.BaseActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupViews() {
        setSupportActionBar(binding.toolbar);

        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        // Setup Navigation Controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard,
                R.id.nav_apiaries,
                R.id.nav_calendar,
                R.id.nav_calculators,
                R.id.nav_analytics,
                R.id.nav_settings
        ).setOpenableLayout(drawerLayout).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void observeData() {
        // No data to observe in main activity yet
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            navController.navigate(R.id.nav_dashboard);
        } else if (id == R.id.nav_apiaries) {
            navController.navigate(R.id.nav_apiaries);
        } else if (id == R.id.nav_calendar) {
            navController.navigate(R.id.nav_calendar);
        } else if (id == R.id.nav_calculators) {
            navController.navigate(R.id.nav_calculators);
        } else if (id == R.id.nav_analytics) {
            navController.navigate(R.id.nav_analytics);
        } else if (id == R.id.nav_settings) {
            navController.navigate(R.id.nav_settings);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
