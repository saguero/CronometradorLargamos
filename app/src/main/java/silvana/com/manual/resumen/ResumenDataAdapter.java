package silvana.com.manual.resumen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import silvana.com.manual.Main2Activity;
import silvana.com.manual.R;
import silvana.com.manual.datos.ArriboAtleta;
import silvana.com.manual.datos.DataAtleta;
import silvana.com.manual.datos.EstadoAtleta;

import static silvana.com.manual.Main2Activity.data;

/**
 * Created by silvana-aguero on 16/03/2018.
 */

public class ResumenDataAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> items;
    private HashMap<String, List<DataAtleta>> detalles;
    private HashMap<String, List<DataAtleta>> copyDetalles;
    private String mFilter;


    public ResumenDataAdapter(Context context, List<String> items) {
        this.context=context;
        this.items = items;

        HashMap<String, List<DataAtleta>> detalles = new HashMap<>();
        for (String s : items) {
            detalles.put(s,new ArrayList<>());
        }
        this.detalles = detalles;
        this.copyDetalles = copy();
        mFilter = "";
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return detalles.get(items.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return detalles.get(items.get(groupPosition)).get(childPosition).toString();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String item = (String) getGroup(groupPosition);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_resumen, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.label_list_group_resumen);
        textView.setText(item);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String detalle = (String) getChild(groupPosition, childPosition);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_resumen, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.label_list_item_resumen);
        textView.setText(detalle);

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click sobre boton " + childPosition + " del grupo "+ groupPosition, Toast.LENGTH_LONG).show();
            }
        });*/
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public void agregarDetalle(String entidad, List<? extends DataAtleta> datos) {

        for(int i = 0; i < datos.size(); i ++) {
            DataAtleta data = datos.get(i);
            if( data instanceof ArriboAtleta) {
                if( ((ArriboAtleta) data).getMalIngresado() && ((ArriboAtleta) data).getIdentificador() == null) {
                    detalles.get(entidad).add(data);
                    datos.get(i).setIdentificador();
                }
            }
            else if(data instanceof EstadoAtleta) {
                if( ((EstadoAtleta) data).getIdentificador() == null) {
                    detalles.get(entidad).add(data);
                    datos.get(i).setIdentificador();
                }
            }
         }
        notifyDataSetChanged();
    }

    public void filter(String textoPlano) {
        mFilter = textoPlano.toLowerCase(Locale.getDefault());
        clearDetalles();
        if(textoPlano.length() == 0){
            addAllDetalles();
        }
        else {
            //FILTRAR
            for(DataAtleta d : data.getArribos()) {
                if(d.filter(textoPlano)) {
                    if(d instanceof ArriboAtleta){
                        detalles.get(DataAtleta.Entidad.ARRIBO.getNombre()).add(d);
                    }
                    else if(d instanceof EstadoAtleta){
                        detalles.get(DataAtleta.Entidad.ESTADO.getNombre()).add(d);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void clearDetalles(){
        for(String i : items)
            detalles.get(i).clear();
    }

    public void addAllDetalles() {
        detalles = new HashMap<>(copyDetalles);
    }

    private HashMap<String,List<DataAtleta>> copy() {
        /*HashMap<String, List<DataAtleta>> copia = new HashMap<>();
        Set<String> keys = detalles.keySet();
        for(String k : keys) {
            copia.put(k,new ArrayList<>(detalles.get(k)));
        }
       return copia; */
        return copyDetalles = new HashMap<>(detalles);
    }
}


