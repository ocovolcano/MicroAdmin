package com.example.caro.microadmin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainInventarioActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nombreHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inventario);
        this.setTitle("MicroAdmin");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nombreHeader = (TextView)header.findViewById(R.id.header_nombre);
        nombreHeader.setText(getIntent().getStringExtra("nombre"));

        displaySelectedScreen(R.id.nav_inventario);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_inventario, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);


        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_inventario) {
            Bundle bundle = new Bundle();
            bundle.putInt("IDUsuario", getIntent().getIntExtra("IDUsuario", 0));
            fragment = new InventarioFragment();
            fragment.setArguments(bundle);


        } else if (id == R.id.nav_ventas) {
            Bundle bundle = new Bundle();
            bundle.putInt("IDUsuario", getIntent().getIntExtra("IDUsuario", 0));
            fragment = new Ventas();
            fragment.setArguments(bundle);
        } else if (id == R.id.nav_recordatorios) {
            Bundle bundle = new Bundle();
            bundle.putInt("IDUsuario", getIntent().getIntExtra("IDUsuario", 0));
            fragment = new EncargoFragment();
            fragment.setArguments(bundle);
        }else if (id == R.id.nav_clientes) {
        Bundle bundle = new Bundle();
        bundle.putInt("IDUsuario", getIntent().getIntExtra("IDUsuario", 0));
        fragment = new ClienteFragment();
        fragment.setArguments(bundle);
    }else  if(id == R.id.nav_logout){
            Intent intent = new Intent(MainInventarioActivity.this, Login_Registro_Activity.class);
            startActivity(intent);
            finish();
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
