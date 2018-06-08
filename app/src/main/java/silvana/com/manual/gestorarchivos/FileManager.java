package silvana.com.manual.gestorarchivos;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import silvana.com.manual.datos.Data;

/**
 * Created by silvana-aguero on 10/02/2018.
 */

public class FileManager {

    private final String ANTENA = "10";
    private final String PATTERN_TIEMPO = "dd-MM-yyyy HH:mm:ss.SSS";
    private final String SEPARATOR = ";";

    private final String PREFFIX_ID = "000600000000000000000";

    private SimpleDateFormat formatTiempo;

    private File fTiempoLargada;
    private File fTiempos;
    private File fTiemposError;
    private File fAtletasConEstadosEspeciales;

    private static Data data ;

    public FileManager(Data data) {
        this.data = data;
    }

    public void create(Context context) {

        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmSS");
        String ahora = s.format(new Date());

        try {

            File dir = context.getExternalFilesDir(null);
            if(!dir.exists()) {
                dir.mkdir();
            }
            try {
                fTiempoLargada = new File(dir, ahora + "-" + "tiempoLargada.txt");
                fTiempoLargada.createNewFile();
                fTiempos = new File(dir, ahora + "-" + "tiempos.txt");
                fTiempos.createNewFile();

                fTiemposError = new File(dir, ahora + "-" + "tiemposError.txt");
                fTiemposError.createNewFile();

                fAtletasConEstadosEspeciales = new File(dir, ahora + "-" + "atletasConEstadosEspeciales.txt");
                fAtletasConEstadosEspeciales.createNewFile();

            } catch(Exception e) {
            }
        } catch(Exception ex) {
        }

        formatTiempo = new SimpleDateFormat(PATTERN_TIEMPO);
    }

    public void writeTiempoLargada(Date startTime) {
        try {
            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(fTiempoLargada));
            bf.write(formatTiempo.format(startTime));
            bf.close();
        }
        catch(Exception ex) {

        }
    }

    public void writeTiempos(Date llegada, String formatTime, String dorsal) {
        try {
            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(fTiempos, true), "UTF-8");
            String format = formatTime(llegada,formatTime, dorsal);
            bf.write(format);
            bf.write("\n");
            bf.close();
        }
        catch(Exception ex) {

        }
    }

    public void writeTiemposError(Date llegada, String formatTime, String dorsal) {
        try {
            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(fTiemposError, true), "UTF-8");
            String format = formatTime(llegada,formatTime, dorsal);
            //data.addFormatIngresosPorError(format);
            bf.write(format);
            bf.write("\n");
            bf.close();
        }
        catch(Exception ex) {

        }
    }

    private String formatTime(Date llegada, String formatTime, String dorsal) {
        String format = "";

        String hexFormat = Integer.toHexString(Integer.parseInt(dorsal));
        String idHex = String.format("%0"+ (3 - hexFormat.length() )+"d%s",0 ,hexFormat).toUpperCase();
        String formatLlegada = data.getFormat(llegada);

        format =    PREFFIX_ID + idHex +
                    SEPARATOR + formatLlegada +
                    SEPARATOR + ANTENA +
                    SEPARATOR + dorsal +
                    SEPARATOR + "0:" + formatTime ;
        return format;
    }

    private String formatEstadoEspecial(String dorsal, Date tiempoEvento, String estado, String comentario){
        String format = "";
        String hexFormat = Integer.toHexString(Integer.parseInt(dorsal));
        String idHex = String.format("%01d%s",0 ,hexFormat).toUpperCase();

        String formatTiempoEvento = data.getFormat(tiempoEvento);

        format =    PREFFIX_ID + idHex +
                SEPARATOR + formatTiempoEvento +
                SEPARATOR + ANTENA +
                SEPARATOR + dorsal +
                SEPARATOR + estado +
                SEPARATOR + comentario;
        //data.addFormatAtletasConEtadosEspeciales(format);
        return format;
    }

    public void writeAtletasConEstadosEspeciales(String dorsal, Date tiempoEvento, String estado, String comentario) {
        try {
            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(fAtletasConEstadosEspeciales, true), "UTF-8");
            bf.write(formatEstadoEspecial(dorsal,tiempoEvento,estado,comentario));
            bf.write("\n");
            bf.close();
        }
        catch(Exception ex) {
        }
    }

    public void createExcelSheet(Context context) {

    /*
        try {
            //file path
            File dir = context.getExternalFilesDir(null);
            File file = new File(dir, "resumenManual.xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //@Param Nombre y numero de la hoja respectivamente
            WritableSheet hoja_tiempoLargada = workbook.createSheet("Tiempo largada", 0);
            WritableSheet hoja_tiempos = workbook.createSheet("Tiempos", 1);
            WritableSheet hoja_atletas_estados_especiales = workbook.createSheet("Estados especiales", 2);
            WritableSheet hoja_ingresos_por_error = workbook.createSheet("Ingresos por error", 3);

            // TIEMPO LARGADA
            hoja_tiempoLargada.addCell(new Label(0, 0, data.getFormatInicioTemporizador()));

            //TIEMPOS DE ARRIBOS
            List<String> tiempos = data.getFormaTiempos();
            for(int i = 0; i < tiempos.size(); i++ ) {
                String[] datos = tiempos.get(i).split(";");
                for(int j = 0; j < datos.length; j++) {
                    hoja_tiempos.addCell(new Label(j,i,datos[j]));
                }
            }

            //ESTADOS ESPECIALES DE ATLETAS (DQ, DNS, DNF)
            List<String> especiales = data.getFormaAtletasConEtadosEspeciales();
            for(int i = 0; i < especiales.size(); i++ ) {
                String[] datos = especiales.get(i).split(";");
                for(int j = 0; j < datos.length; j++) {
                    hoja_atletas_estados_especiales.addCell(new Label(j,i,datos[j]));
                }
            }

            //INGRESOS POR ERROR
            List<String> ingresosPorError = data.getFormatIngresosPorError();
            for(int i = 0; i < ingresosPorError.size(); i++ ) {
                String[] datos = ingresosPorError.get(i).split(";");
                for(int j = 0; j < datos.length; j++) {
                    hoja_ingresos_por_error.addCell(new Label(j,i,datos[j]));
                }
            }

            workbook.write();
            workbook.close();
            Toast.makeText(context,
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
        }

        */
    }
}
