package silvana.com.manual.resumen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silvana.com.manual.Main2Activity;
import silvana.com.manual.R;
import silvana.com.manual.datos.Data;
import silvana.com.manual.datos.DataAtleta;
import silvana.com.manual.gestorarchivos.FileManager;

/**
 * Created by silvana-aguero on 05/03/2018.
 */

public class TabResumen extends Fragment {

    private static Data data;
    private static ResumenDataAdapter resumenDataAdapter;
    private static FileManager fileManager;

    private TextView inicioTemporizador;
    private TextView resumenCantArribos;
    private TextView resumenLabelArribos;
    private SearchView search;
    private ExpandableListView resumenList;
    private FloatingActionButton descargarExcel;

    public static Fragment newInstance(){
        TabResumen fragment = new TabResumen();
        fragment.setData( Main2Activity.data);
        fragment.setFileManager(Main2Activity.fileManager);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        data = Main2Activity.data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_resumen,container,false);
        activate(v);
        return v;
    }

    private void activate(View v) {

        inicioTemporizador = (TextView) v.findViewById(R.id.tab_resumen_inicio_temporizador);
        inicioTemporizador.setText(data.getFormatInicioTemporizador());

        resumenCantArribos = (TextView) v.findViewById(R.id.tab_resumen_cant_arribos);
        resumenLabelArribos = (TextView) v.findViewById(R.id.tab_resumen_label_arribos);

        search = (SearchView) v.findViewById(R.id.search);
        EditText searchEditText = (EditText) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.CYAN);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                resumenDataAdapter.filter(newText);
                return false;
            }
        });

        descargarExcel = (FloatingActionButton) v.findViewById(R.id.tab_resumen_excel);

        try {
            descargarExcel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(!data.getArribos().isEmpty())
                        fileManager.createExcelSheet(getActivity());
                }
            });
        }
        catch(Exception e){};

        resumenList = (ExpandableListView) v.findViewById(R.id.tab_resumen_list);
        resumenDataAdapter = new ResumenDataAdapter(v.getContext(),dataSource());
        resumenList.setAdapter(resumenDataAdapter);

    }

    private List<String> dataSource() {
        List<String> data = new ArrayList<>();
        data.add(DataAtleta.Entidad.ESTADO.getNombre());
        data.add(DataAtleta.Entidad.ARRIBO.getNombre());
        return data;
    }

    public void setData(Data data){
        this.data = data;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void actualizarDatos() {
        inicioTemporizador.setText(data.getFormatInicioTemporizador());

        if(!data.getCantidadDeArribos().equals("0")) {
            resumenCantArribos.setText(data.getCantidadDeArribos());
            resumenLabelArribos.setText(R.string.arribos);
        }

        resumenDataAdapter.agregarDetalle(DataAtleta.Entidad.ARRIBO.getNombre(),data.getArribos());
        resumenDataAdapter.agregarDetalle(DataAtleta.Entidad.ESTADO.getNombre(),data.getAtletasConEstadosEspeciales());
    }
}
