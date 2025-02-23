package lk.javainstitute.petbuddy;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import lk.javainstitute.petbuddy.fragments.HomeFragment;
import lk.javainstitute.petbuddy.fragments.SettingsFragment;
import lk.javainstitute.petbuddy.fragments.ShopFragment;
import lk.javainstitute.petbuddy.fragments.ProfileFragment; // Assuming you have a WishlistFragment

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Load the default fragment (HomeFragment) when the activity starts
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());  // Default Fragment
        }

        // Set a listener to handle fragment switching based on BottomNavigationView item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:  // Corrected ID to match menu item "nav_home"
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.nav_shop:  // Corrected ID to match menu item "nav_shop"
                        loadFragment(new ShopFragment());
                        return true;
                    case R.id.nav_profile:  // Corrected ID to match menu item "nav_profile"
                        loadFragment(new ProfileFragment());
                        return true;
                    case R.id.nav_wishlist:  // Corrected ID to match menu item "nav_wishlist"
                        loadFragment(new SettingsFragment()); // Assuming WishlistFragment is defined
                        return true;
                }
                return false;
            }
        });
    }

    // Method to load the selected fragment dynamically
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
