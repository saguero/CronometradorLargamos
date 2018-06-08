package silvana.com.manual.datos;

/**
 * Created by marmota on 10/05/2018.
 */

public abstract class DataAtleta {

    public final static int MAX_LENGTH_DORSAL = 4;

    public enum Entidad {
        ESTADO("Atletas con estados especiales"), ARRIBO("Dorsales ingresados por error");

        private String nombre;

        Entidad(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre(){
            return this.nombre;
        }

    }

    protected static Integer autoincrement = 1;

    protected String dorsal;

    protected Integer idCompetencia;

    protected Integer identificador;

    public DataAtleta(String dorsal) {
        this.dorsal = dorsal;
    }

    public DataAtleta() {}

    public String getDorsal(){
        return dorsal;
    }

    public void setDorsal(String dorsal){this.dorsal = dorsal; }

    public Integer getIdCompetencia() {return idCompetencia;}

    public void setIdCompetencia(Integer idCompetencia) {this.idCompetencia = idCompetencia; }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(){
        this.identificador = autoincrement;
        autoincrement = autoincrement + 1;
    }


    public abstract String toString();

    public abstract String getEntidad();

    public abstract boolean filter(String textoPlano);


}
