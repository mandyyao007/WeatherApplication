package com.example.weatherapplication;

import android.os.Bundle;

import com.example.weatherapplication.util.StatusBarUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weatherapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavController navController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTransparent(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard ,  R.id.navigation_home, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    protected void onResume() {
        // 获取从其他Activity发送过来跳转Fragment的标志fragment_flag(名称随意)
        int fragmentFlag = this.getIntent().getIntExtra("fragment_flag", 0);
        switch (fragmentFlag){
            case 1:
                // 控制跳转到底部导航项(navigation_home为该Fragment的对应控件的id值)
                navController.navigate(R.id.navigation_dashboard);
                break;
            case 2:
                navController.navigate(R.id.navigation_home);
                break;
            case 3:
                navController.navigate(R.id.navigation_notifications);
                break;
        }
        super.onResume();
    }
}