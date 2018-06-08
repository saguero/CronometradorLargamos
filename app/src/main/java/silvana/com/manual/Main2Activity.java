package silvana.com.manual;


import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import silvana.com.manual.clasificador.Cronometro;
import silvana.com.manual.clasificador.TabCronometro;
import silvana.com.manual.datos.Data;
import silvana.com.manual.gestorarchivos.FileManager;
import silvana.com.manual.resumen.TabResumen;

public class Main2Activity extends AppCompatActivity {

    /* Componentes UI*/
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static Data data;
    public static FileManager fileManager;

    private Fragment tabCronometro ;
    private Fragment tabResumen ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    ((TabResumen) mSectionsPagerAdapter.getItem(position)).actualizarDatos();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        data = new Data(getApplicationContext());
        fileManager = new FileManager(data);
        tabCronometro = TabCronometro.newInstance(0,data);
        tabResumen = TabResumen.newInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this, R.style.CustomDialogTheme);
        builder.setTitle(R.string.salir_app_titulo);
        builder.setIcon(R.mipmap.ic_salir_app);
        builder.setMessage(R.string.message_salir_app)
        .setCancelable(false)
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Thread t = ((TabCronometro)tabCronometro).getThreadCronometro();
                Cronometro c = ((TabCronometro)tabCronometro).getCronometro();
                if( t != null && c != null) {
                    c.stop();
                    t.interrupt();
                    ((TabCronometro)tabCronometro).setThreadCronometro(null);
                }
                finish();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
        ;
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }


    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int NUM_ITEMS = 2;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: //Fragment  # 0 This will show tab_cronometro
                    return ((TabCronometro) tabCronometro);
                case 1: //Fragment #1 This will show tab_resumen
                    return ((TabResumen) tabResumen);
                default:
                    return ((TabCronometro) tabCronometro);
            }
        }

        //return total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }
}

