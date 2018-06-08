package silvana.com.manual.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

@Entity(tableName = "competencia", indices = {@Index(value = "nombre", unique = true)})
public class Competencia {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_competencia")
    private int idCompetencia;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "tiempo_inicio")
    private Date tiempoInicio;

    public int getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(int idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(Date tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }
}
