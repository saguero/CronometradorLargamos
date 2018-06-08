package silvana.com.manual.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

@Dao
public interface ClasificacionDao {

    @Query("SELECT * FROM clasificacion WHERE id_competencia = :idCompetencia ORDER BY orden DESC")
    List<Clasificacion> findByCompetencia(Integer idCompetencia);

    @Query("SELECT * FROM clasificacion")
    List<Clasificacion> findAll();

    @Insert
    void insert(Clasificacion c);

    @Update
    void update(Clasificacion c);

    @Delete
    void delete(Clasificacion c);

}
