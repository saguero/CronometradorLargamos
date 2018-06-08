package silvana.com.manual.datos;

/**
 * Created by silvana-aguero on 10/05/2018.
 */

public class ArriboAtleta extends DataAtleta {

    private String tiempo;

    private Boolean malIngresado;

    public static ArriboAtleta newInstance(String dorsal) {
        ArriboAtleta instance = new ArriboAtleta(dorsal);
        return instance;
    }

    public ArriboAtleta(String dorsal) {
        super(dorsal);
    }

    public ArriboAtleta(){
        super();
    }

    public ArriboAtleta(String dorsal, String tiempo) {
        super(dorsal);
        this.tiempo = tiempo;
        this.malIngresado = false;
    }

    @Override
    public String toString() {
        return dorsal + " - " + tiempo;
    }

    @Override
    public String getEntidad() {
        return Entidad.ARRIBO.toString();
    }

    @Override
    public boolean filter(String textoPlano) {
        return dorsal.contains(textoPlano) || tiempo.contains(textoPlano);
    }


    public Boolean getMalIngresado() {
        return malIngresado;
    }

    public void setMalIngresado(Boolean malIngresado) {
        this.malIngresado = malIngresado;
    }

    public String getTiempo(){
        return tiempo;
    }

    @Override
    public boolean equals(Object o) {
        return dorsal.equals( ((ArriboAtleta) o).getDorsal());
    }

}
