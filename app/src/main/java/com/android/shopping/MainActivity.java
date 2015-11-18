package com.android.shopping;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.android.shopping.database.DatabaseQueryManager;
import com.android.shopping.settings.ApplicationSettings;

public class MainActivity extends AppCompatActivity{

    private Toolbar cToolbar;
    private static Menu menu;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public DatabaseQueryManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(cToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cToolbar.setLogo(R.drawable.ic_subject_white_24dp);
        displayView(0);

        ApplicationSettings.CreateApplicationFolder();
        ApplicationSettings.initDBAccessor(getApplicationContext());
        dbManager = DatabaseQueryManager.getDbInstance(this);
    }

    public void displayView(int fragmentIndex) {

        Fragment fragment = null;
        switch (fragmentIndex) {
            case 0:
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ItemFragment();
                break;
            case 2:
                break;
            default:
                break;
        }

        if (fragment != null) {
            switchContent(R.id.fragment_container, fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {
            displayView(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(int id, Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(id, fragment, fragment.toString());
        ft.addToBackStack(fragment.toString());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(getApplicationContext(),"onbackpress" + getSupportFragmentManager().getBackStackEntryCount(),Toast.LENGTH_SHORT).show();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }
}
