package silvana.com.manual.clasificador;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silvana.com.manual.R;
import silvana.com.manual.datos.ArriboAtleta;

/**
 * Created by marmota on 28/02/2018.
 */

public class DorsalAdapter extends ArrayAdapter<ArriboAtleta> {

    private Context mContext;
    private List<ArriboAtleta> dorsales = new ArrayList<>();

    public DorsalAdapter(@NonNull Context context, @NonNull List<ArriboAtleta> list) {
        super(context, -1, list);
        mContext = context;
        dorsales = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_dorsal, null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.item_dorsal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ArriboAtleta arribo = dorsales.get(position);
        holder.textView.setText(arribo.toString());

        if(arribo.getMalIngresado()) {
            holder.textView.setTextColor(Color.RED);
        }
        else {
            holder.textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }


    public static class ViewHolder {
        public TextView textView;
    }

    @Override
    public int getCount() {
        return dorsales.size();
    }

    @Override
    public ArriboAtleta getItem(int position) {
        return dorsales.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
