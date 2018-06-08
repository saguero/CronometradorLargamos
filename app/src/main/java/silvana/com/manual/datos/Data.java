package silvana.com.manual.datos;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import silvana.com.manual.database.AppDatabase;
import silvana.com.manual.database.Clasificacion;
import silvana.com.manual.database.Competencia;
import silvana.com.manual.mapping.ClasificacionMapper;

/**
 * Created by silvana-aguero on 07/02/2018.
 */

public class Data {

    private AppDatabase db ;

    private final String FORMATO_TIEMPO = "dd-MM-yyyy HH:mm:ss.SSS";
    private SimpleDateFormat simpleDateFormat ;

    private Long inicioTemporizador;

    private String inputDorsal;

    private List<ArriboAtleta> arribos;

    private int arribosCorrectos;

    private List<EstadoAtleta> atletasConEstadosEspeciales;

    private Context context;


    /**
     * Default constructor to start activity on the first time
     */
    public Data(Context context) {
        this.context = context;
        simpleDateFormat = new SimpleDateFormat(FORMATO_TIEMPO);
        inputDorsal = "";

        arribos = new ArrayList<>();
        arribosCorrectos = 0;

        atletasConEstadosEspeciales = new ArrayList<>();

        loadClasificaciones();
        insertCompetencia();
    }


    public String getFormatInicioTemporizador() {
        if(inicioTemporizador != null)
            return simpleDateFormat.format(inicioTemporizador);
        return "Cronómetro no iniciado";
    }

    public Long getInicioTemporizador() {
        return inicioTemporizador;
    }

    public void setInicioTemporizador(Long inicioTemporizador){
        this.inicioTemporizador = inicioTemporizador;
    }


    public String getInputDorsal() {
        return inputDorsal;
    }

    public void setInputDorsal(String inputDorsal) {
        this.inputDorsal = inputDorsal;
    }

    public void descartarDorsal(String dorsal) {
        /*Optional<ArriboAtleta> opt = arribos.stream().filter(a-> dorsal.equals(a.getDorsal())).findFirst();
        if(opt.isPresent()) {
            opt.get().setMalIngresado(true);
        }*/
        arribosCorrectos = arribosCorrectos - 1;
    }

    public String getFormat(Date llegada) {
        return simpleDateFormat.format(llegada);
    }

    public List<ArriboAtleta> getArribos() {
        return arribos;
    }

    public void addArribo(ArriboAtleta arribo) {
        arribos.add(0,arribo);
        arribosCorrectos = arribosCorrectos + 1;
        insertClasificacion(ClasificacionMapper.INSTANCE.toClasificacion(arribo));

    }

    public String getCantidadDeArribos() {
        return Integer.valueOf(arribosCorrectos).toString();
    }

    public void agregarEstadoDeAtleta(String estado, String comentario) {
        String dorsal = String.format("%04d", Integer.valueOf(getInputDorsal()));
        EstadoAtleta ea = new EstadoAtleta(dorsal, estado, comentario);
        atletasConEstadosEspeciales.add(ea);
    }

    public List<EstadoAtleta> getAtletasConEstadosEspeciales(){
        return atletasConEstadosEspeciales;
    }


    private void loadClasificaciones() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db = AppDatabase.getDatabase(context);
                String path = db.getOpenHelper().getWritableDatabase().getPath();
                File buckupDB = new File(path);
                try {
                    if (!buckupDB.exists())
                        buckupDB.createNewFile();
                }
                catch(Exception ex) {}
                arribos = ClasificacionMapper.INSTANCE.toListArriboAtleta(db.getClasificacionDao().findAll());
                return null;
            }
        }.execute();
    }

    private void insertCompetencia() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                if(db.getCompetenciaDao().countAll() == 0) {
                    Competencia c = new Competencia();
                    c.setNombre("Competencia por defecto");
                    db.getCompetenciaDao().insert(c);
                }
                return null;
            }
        }.execute();
    }

    private void insertClasificacion(Clasificacion c) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Competencia> competencias = db.getCompetenciaDao().findAll();
                if(!competencias.isEmpty()) {
                    c.setIdCompetencia(competencias.get(0).getIdCompetencia());
                    db.getClasificacionDao().insert(c);
                }

                return null;
            }
        }.execute();
    }
}
