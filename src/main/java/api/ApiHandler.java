package api;

import error.ErrorHandler;
import files.SetupFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApiHandler {

    public  String API_URL;
    public ApiHandler(){
        String filePath = System.getenv("PROGRAMDATA") + "/SteelSeries/SteelSeries Engine 3/coreProps.json";
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String regex = "\"address\"\\s*:\\s*\"(.*?)\"";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            this.API_URL=matcher.group(1);
        } else {
            throw new RuntimeException("ApiUrl not found");
        }
    }
    private void updateTime() {
        String time=getTimeAsString();
        post(ApiParams.UPDATE_EVENT,2,time);
    }
    public void start(){
        post(ApiParams.GAME_METADATA,0);
        post(ApiParams.BIND_EVENT,1);
        ScheduledExecutorService e= Executors.newSingleThreadScheduledExecutor();
        e.scheduleAtFixedRate(this::updateTime,0,1, TimeUnit.SECONDS);
    }
    private void post(ApiParams param,int code,String... extras){
        try {
            URL urlForGetRequest = new URL("http://"+API_URL +"/"+ param.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlForGetRequest.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            Date d=new Date();
            SimpleDateFormat t=new SimpleDateFormat("dd.MM.yy, HH:mm:ss");
            String time=t.format(d);
            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                String message=param.getParameter(extras);
                byte[] input = message.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                if(code==2) {
                    System.out.println(time + ": Updated OLED successfully");
                }else if(code==1){
                    System.out.println(time + ": Bound Game successfully");
                }else {
                    System.out.println(time + ": Set Metadata successfully");
                }
            } else {
                if(code==2) {
                    new ErrorHandler("There was an error while setting Metadata: "+getErrorAsString(httpURLConnection)).throwError();
                }else if(code==1){

                    new ErrorHandler("There was an error while binding Game: "+getErrorAsString(httpURLConnection)).throwError();
                }else {
                    new ErrorHandler("There was an error while updating OLED: "+getErrorAsString(httpURLConnection)).throwError();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getErrorAsString(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        StringBuilder response = new StringBuilder();
        String readLine;
        while ((readLine = in.readLine()) != null) {
            response.append(readLine);
        }
        in.close();

        return response.toString();
    }
    public static String getTimeAsString(){
        Date d=new Date();
        String s="";
        String dateFormat=SetupFile.getDateFormat();
        String timeFormat=SetupFile.getTimeFormat();
        if(dateFormat==null||timeFormat==null){
            new ErrorHandler("Date format or time format is null").throwError();
        }else {
            SimpleDateFormat date = new SimpleDateFormat(dateFormat);
            SimpleDateFormat time = new SimpleDateFormat(timeFormat);
            s = time.format(d) + "   " + date.format(d) + ".";
        }
        return s;
    }

}
