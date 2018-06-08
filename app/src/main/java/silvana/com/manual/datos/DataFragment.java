package silvana.com.manual.datos;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by silvana-aguero on 07/02/2018.
 * This class is used to retain data when activity config-changes is activate, e.g. orientation change
 */

public class DataFragment extends Fragment {

    private Data data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        String hola = "hola";
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
