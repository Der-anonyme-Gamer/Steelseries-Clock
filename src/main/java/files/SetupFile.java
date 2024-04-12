package files;

import error.ErrorHandler;
import java.io.*;

public class SetupFile {
    private static final String fileName = "Steelseries-Clock-Settings.txt";
    public static void create(){
        File f=new File(fileName);
        if(!f.exists()) {
            try {
                f.createNewFile();
                FileWriter w=new FileWriter(fileName,true);
                BufferedWriter bufferedWriter = new BufferedWriter(w);
                bufferedWriter.write("Settings File for Steelseries-Clock:\nSetup your time format using this characters:\n- HH (Hours 24h format)\n- hh (Hours 12h format)\n- a (AM/PM)\n- mm (Minutes)\n- ss (Seconds)\n\nSetup your date format using this characters:\n- dd (Day)\n - mm (Month)\n - yy/yyyy (Year)\n\nTimeFormat=\"hh:mm:ss a\"\nDateFormat=\"MM.dd.yyyy\"");
                bufferedWriter.close();
            } catch (IOException e) {
                new ErrorHandler("Error while creating Settings file: "+e.getCause()).throwError();
            }
            ErrorHandler.openEditor(f);
        }
    }
   public static String getTimeFormat(){
       String timeFormat = null;
            if(new File(fileName).exists()) {
                try {
                    FileReader reader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.startsWith("TimeFormat=")) {
                            line=line.substring(line.indexOf("=")+1);
                            timeFormat =line.replace("\"","");
                        }
                    }

                    reader.close();
                } catch (IOException e) {
                    new ErrorHandler("Error reading file: " + e.getMessage()).throwError();
                }

                return timeFormat;
            }else{
                create();
                return null;
            }
    }
    public static String getDateFormat(){
        String dateFormat = null;
        if(new File(fileName).exists()) {
            try {
                FileReader reader = new FileReader(fileName);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("DateFormat=")) {
                        line=line.substring(line.indexOf("=")+1);
                        dateFormat =line.replace("\"","");
                    }
                }

                reader.close();
            } catch (IOException e) {
                new ErrorHandler("Error reading file: " + e.getMessage()).throwError();
            }

            return dateFormat;
        }else{
            create();
            return null;
        }

    }
}
