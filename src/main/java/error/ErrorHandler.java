package error;

import files.SetupFile;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ErrorHandler {
    private final String message;
    private final static String logs="Error_Logs.txt";
    public ErrorHandler(String message){
        this.message=message;
        logError();
    }
    public void throwError(){
        File f=new File(logs);
        openEditor(f);
        throw new RuntimeException(message);
    }
    public void logError(){
        try {
            FileWriter fileWriter = new FileWriter(logs, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            Date d=new Date();
            SimpleDateFormat s=new SimpleDateFormat(SetupFile.getDateFormat()+","+ SetupFile.getTimeFormat()+": ");
            bufferedWriter.write(s.format(d)+": "+message);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException("Error while logging Error: " + e.getMessage());
        }
    }
    public static void create(){
        File f=new File(logs);
        if(f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void openEditor(File f){
        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(f.getAbsoluteFile().toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + f.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}