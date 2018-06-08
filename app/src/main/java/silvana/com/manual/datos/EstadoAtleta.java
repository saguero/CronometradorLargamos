package silvana.com.manual.datos;

/**
 * Created by silvana-aguero on 10/05/2018.
 */
public class EstadoAtleta extends DataAtleta {

    private String estado;

    private String comentario;

    public  EstadoAtleta(String dorsal, String estado, String comentario) {
        super(dorsal);
        this.estado = estado;
        this.comentario = comentario;
    }

    public EstadoAtleta(){
        super();
    }

    @Override
    public String toString() {
        return dorsal + " " + estado + " - " + comentario;
    }

    @Override
    public String getEntidad() {
        return Entidad.ESTADO.toString();
    }

    @Override
    public boolean filter(String textoPlano) {
        return dorsal.contains(textoPlano) || comentario.contains(textoPlano);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getComentario(){
        return comentario;
    }

    public void setComentario(String comentario){
        this.comentario = comentario;
    }
}
