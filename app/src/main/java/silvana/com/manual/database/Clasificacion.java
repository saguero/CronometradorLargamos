package silvana.com.manual.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

@Entity(tableName = "clasificacion",
        primaryKeys = {"id_competencia", "dorsal"},
        foreignKeys = @ForeignKey(entity = Competencia.class,
                parentColumns = "id_competencia",
                childColumns = "id_competencia"))
public class Clasificacion {

    @ColumnInfo(name = "id_competencia")
    private int idCompetencia;

    @ColumnInfo(name = "dorsal")
    private int dorsal;

    @ColumnInfo(name = "tiempo")
    private String tiempo;

    @ColumnInfo(name = "mal_ingreso")
    private boolean malIngresado;

    @ColumnInfo(name = "observacion")
    private String observacion;

    @ColumnInfo(name = "estado")
    private String estado;

    @ColumnInfo(name = "orden")
    private int orden;


    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isMalIngresado() {
        return malIngresado;
    }

    public void setMalIngresado(boolean malIngresado) {
        this.malIngresado = malIngresado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    //getters and setter
}
