import api.ApiHandler;
import error.ErrorHandler;
import files.SetupFile;

public class Main {

    public static void main(String... args){
        SetupFile.create();
        ErrorHandler.create();
        ApiHandler h=new ApiHandler();
         h.start();
        System.out.println("Time Format is: "+SetupFile.getTimeFormat());
        System.out.println("Date Format is: "+SetupFile.getDateFormat());

    }
}
