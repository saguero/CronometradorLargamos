package silvana.com.manual.clasificador;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.Optional;

import silvana.com.manual.Main2Activity;
import silvana.com.manual.R;
import silvana.com.manual.datos.ArriboAtleta;
import silvana.com.manual.datos.Data;
import silvana.com.manual.datos.DataAtleta;
import silvana.com.manual.datos.EstadoAtleta;
import silvana.com.manual.gestorarchivos.FileManager;

/**
 * Created by slvana-aguero on 05/03/2018.
 */

public class TabCronometro extends Fragment {

    /* Componentes UI */
    private FloatingActionButton iniciadorCronometro;
    private EditText inicioTemporizador;
    private ListView listaArribos;
    private Button cantidadArribos;
    private EditText inputDorsal;

    private ImageView suprimir;

    private Button numero_0;
    private Button numero_1;
    private Button numero_2;
    private Button numero_3;
    private Button numero_4;
    private Button numero_5;
    private Button numero_6;
    private Button numero_7;
    private Button numero_8;
    private Button numero_9;

    private Button estadoAtleta;
    private FloatingActionButton ingresarArribo;
    private Button descartarArribo;

    private View viewAlertDialogEstadoAtleta;
    private RadioGroup grupoEstadoAtleta;
    private EditText comentarioEstadoAtleta;
    private int estadoAtletaSeleccionado;

    /* */
    private Data data;
    private static FileManager fileManager;
    private Thread mThreadCronometro;
    private static Cronometro cronometro;

    private DorsalAdapter dorsalAdapter;


    public static Fragment newInstance(int page, Data data) {
        TabCronometro fragment = new TabCronometro();
        fragment.setData(Main2Activity.data);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configRetainedFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_cronometro, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activate(getView());
    }

    public void updateTimerText(final String time) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inicioTemporizador.setText(time);
            }
        });
    }

    private void configRetainedFragment() {
        data = Main2Activity.data;
        fileManager = new FileManager(data);
        fileManager.create(getActivity());
    }

    private void activate(View v) {

        inicioTemporizador = (EditText) v.findViewById(R.id.tab_cronometro_temporizador);
        inicioTemporizador.setShowSoftInputOnFocus(false);
        inicioTemporizador.setInputType(InputType.TYPE_NULL);
        inicioTemporizador.setFocusable(false);

        iniciadorCronometro = (FloatingActionButton) v.findViewById(R.id.tab_crono_iniciador);

        listaArribos = (ListView) v.findViewById(R.id.tab_crono_list_arribos);
        dorsalAdapter = new DorsalAdapter(getActivity(), data.getArribos());
        listaArribos.setAdapter(dorsalAdapter);

        cantidadArribos = (Button) v.findViewById(R.id.teclado_cant_arribos);

        inputDorsal = (EditText) v.findViewById(R.id.teclado_input_dorsal);
        inputDorsal.setShowSoftInputOnFocus(false);
        inputDorsal.setInputType(InputType.TYPE_NULL);
        inputDorsal.setFocusable(false);

        suprimir = (ImageView) v.findViewById(R.id.teclado_suprimir);

        numero_0 = (Button) v.findViewById(R.id.teclado_numero_0);
        numero_1 = (Button) v.findViewById(R.id.teclado_numero_1);
        numero_2 = (Button) v.findViewById(R.id.teclado_numero_2);
        numero_3 = (Button) v.findViewById(R.id.teclado_numero_3);
        numero_4 = (Button) v.findViewById(R.id.teclado_numero_4);
        numero_5 = (Button) v.findViewById(R.id.teclado_numero_5);
        numero_6 = (Button) v.findViewById(R.id.teclado_numero_6);
        numero_7 = (Button) v.findViewById(R.id.teclado_numero_7);
        numero_8 = (Button) v.findViewById(R.id.teclado_numero_8);
        numero_9 = (Button) v.findViewById(R.id.teclado_numero_9);

        estadoAtleta = (Button) v.findViewById(R.id.teclado_estados);
        ingresarArribo = (FloatingActionButton) v.findViewById(R.id.teclado_ingresar_arribo);

        if(data.getInicioTemporizador() != null) {
            ingresarArribo.setEnabled(true);
            ingresarArribo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        }
        else {
            ingresarArribo.setEnabled(false);
            ingresarArribo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryAlpha)));
        }
        descartarArribo = (Button) v.findViewById(R.id.teclado_descartar);

        configAlertDialogView();

        configAcciones();
    }

    public void configAcciones() {

        try {

            suprimir.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int length = data.getInputDorsal().length();
                    if(length > 0 ) {
                        data.setInputDorsal(data.getInputDorsal().substring(0,length - 1));
                        inputDorsal.setText(data.getInputDorsal());
                    }
                }
            });

            descartarArribo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (!data.getInputDorsal().isEmpty()) {
                        String dorsal = data.getInputDorsal();
                        ArriboAtleta arribo = containsDorsal(dorsal);
                        if(arribo != null && !arribo.getMalIngresado()){
                            Date llegada = new Date();
                            arribo.setMalIngresado(true);
                            //data.descartarDorsal(dorsal);


                            String formatTime = cronometro.getFormatTime(llegada.getTime());
                            fileManager.writeTiemposError(llegada,formatTime, data.getInputDorsal());

                            cantidadArribos.setText(data.getCantidadDeArribos());
                            dorsalAdapter.notifyDataSetChanged();
                        }
                    }
                    data.setInputDorsal("");
                    inputDorsal.setText("");

                }
            });

            ingresarArribo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //write file
                    if (!data.getInputDorsal().isEmpty()) {
                        Date llegada = new Date();
                        String dorsal = data.getInputDorsal();
                        ArriboAtleta arribo = containsDorsal(dorsal);
                        if (arribo == null || (arribo!=null && arribo.getMalIngresado()) ) {

                            ArriboAtleta optArribo = filterDorsal(dorsal);

                            if (optArribo == null) {
                                String formatTime = cronometro.getFormatTime(llegada.getTime());
                                fileManager.writeTiempos(llegada, formatTime, dorsal);

                                String formatArribo = String.format("%04d", Integer.valueOf(dorsal));
                                data.addArribo(createArriboAtleta(formatArribo, formatTime));

                                dorsalAdapter.notifyDataSetChanged();

                                cantidadArribos.setText(data.getCantidadDeArribos());
                            }
                        }
                    }
                    data.setInputDorsal("");
                    inputDorsal.setText("");

                }
            });

            estadoAtleta.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    if (!data.getInputDorsal().isEmpty()) {
                        final Date tiempoEvento = new Date();

                        configAlertDialogView();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CustomDialogTheme)
                            .setView(viewAlertDialogEstadoAtleta)
                            .setTitle(R.string.titulo_estado_atleta)
                            .setIcon(R.mipmap.ic_agregar_estado)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int opcion = grupoEstadoAtleta.getCheckedRadioButtonId();
                                    if(opcion != -1) {
                                        int index = grupoEstadoAtleta.indexOfChild(viewAlertDialogEstadoAtleta.findViewById(opcion));

                                        String estado =  getResources().getStringArray(R.array.estados_atleta)[index];
                                        String comentario =  comentarioEstadoAtleta.getText().toString();
                                        if(comentario == null)
                                            comentario = "Sin comentarios";
                                        data.agregarEstadoDeAtleta(estado,comentario);

                                        fileManager.writeAtletasConEstadosEspeciales(data.getInputDorsal(),tiempoEvento,estado,comentario);
                                    }
                                    data.setInputDorsal("");
                                    inputDorsal.setText("");
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    data.setInputDorsal("");
                                    inputDorsal.setText("");
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.setCancelable(false);
                    alert.show();
                    }
                }
            });


        }
        catch (Exception ex) {
            ex.printStackTrace();
        }



        //config Cronometro

        iniciadorCronometro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cronometro == null) {
                    //Primera vez iniciado el cronometro
                    Date d = new Date();
                    Long startTime = d.getTime();
                    fileManager.writeTiempoLargada(d);

                    //run the chronometer on a separate thread
                    cronometro = new Cronometro(TabCronometro.this, startTime);
                    mThreadCronometro = new Thread(cronometro);
                    mThreadCronometro.start();
                    cronometro.start();

                    data.setInicioTemporizador(startTime);

                    inicioTemporizador.setText(data.getFormatInicioTemporizador());

                    ingresarArribo.setEnabled(true);
                    ingresarArribo.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                    iniciadorCronometro.setEnabled(false);
                    iniciadorCronometro.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentAlpha)));
                }
            }
        });

        try {
        View.OnClickListener handler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (data.getInputDorsal().length() < DataAtleta.MAX_LENGTH_DORSAL) {
                    data.setInputDorsal(data.getInputDorsal().concat(b.getText().toString()));
                    inputDorsal.setText(data.getInputDorsal());
                }
            }
        };

        //each key with click listener
        numero_0.setOnClickListener(handler);
        numero_1.setOnClickListener(handler);
        numero_2.setOnClickListener(handler);
        numero_3.setOnClickListener(handler);
        numero_4.setOnClickListener(handler);
        numero_5.setOnClickListener(handler);
        numero_6.setOnClickListener(handler);
        numero_7.setOnClickListener(handler);
        numero_8.setOnClickListener(handler);
        numero_9.setOnClickListener(handler);

    }
        catch(
    Exception ex)

    {
        ex.printStackTrace();
    }
}

    public void setData(Data data) {
        this.data = data;
    }

    public void setThreadCronometro(Thread mThreadCronometro) {
        this.mThreadCronometro = mThreadCronometro;
    }

    public Thread getThreadCronometro() {
        return mThreadCronometro;
    }

    public Cronometro getCronometro(){
        return cronometro;
    }

    public void setCronometro(Cronometro cronometro) {
        TabCronometro.cronometro = cronometro;
    }

    private void configAlertDialogView() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        viewAlertDialogEstadoAtleta = inflater.inflate(R.layout.alert_dialog_estado_atleta,null,false);
        grupoEstadoAtleta = (RadioGroup) viewAlertDialogEstadoAtleta.findViewById(R.id.alert_dialog_estados);
        comentarioEstadoAtleta = (EditText) viewAlertDialogEstadoAtleta.findViewById(R.id.alert_dialog_comentario);
    }

    private ArriboAtleta createArriboAtleta(String dorsal, String tiempo) {
        return new ArriboAtleta(dorsal, tiempo);
    }

    private ArriboAtleta filterDorsal(String dorsal) {
        for (ArriboAtleta a : data.getArribos()) {
            if(a.getDorsal().equals(dorsal) && !a.getMalIngresado())
                return a;
        }
        return null;
    }

    private ArriboAtleta containsDorsal(String dorsal) {
        String format_dorsal = String.format("%04d", Integer.valueOf(dorsal));
        ArriboAtleta newInstance = ArriboAtleta.newInstance(format_dorsal);
        if (data.getArribos().contains(newInstance)) {
            return data.getArribos().get(data.getArribos().lastIndexOf(newInstance));
        }
        return null;
    }
}
