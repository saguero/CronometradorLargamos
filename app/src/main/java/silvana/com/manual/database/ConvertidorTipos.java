package silvana.com.manual.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by silvana-aguero on 28/05/2018.
 */

public class ConvertidorTipos {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
