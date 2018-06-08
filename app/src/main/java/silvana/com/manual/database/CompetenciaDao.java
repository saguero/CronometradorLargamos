package silvana.com.manual.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

@Dao
public interface CompetenciaDao {

    @Query("SELECT * FROM competencia")
    List<Competencia> findAll();

    @Query("SELECT * FROM competencia WHERE nombre LIKE :nombre LIMIT 1")
    Competencia findByNombre(String nombre);

    @Query("SELECT COUNT(*) FROM competencia")
    int countAll();

    @Insert
    void insert(Competencia c);
}
