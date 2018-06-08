package silvana.com.manual.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

@Database(entities = {Clasificacion.class, Competencia.class}, version = 1, exportSchema = false)
@TypeConverters({ConvertidorTipos.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final String NOMBRE_DB = "ManualDatabase.db";

    public abstract ClasificacionDao getClasificacionDao();

    public abstract CompetenciaDao getCompetenciaDao();


    public static AppDatabase getDatabase(Context context){
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if(INSTANCE == null){
                    INSTANCE = Room
                      .databaseBuilder(context.getApplicationContext(), AppDatabase.class,NOMBRE_DB)
                      .addCallback(new RoomDatabase.Callback(){
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Log.d("AppDatabase", "populating with data...");
                            }
                      })
                      .build();
                }
            }
        }
        return INSTANCE;
    }
}
