package api;

public class ApiParams {

    private static final String name="STEELSERIES-CLOCK";
    private static final String dev="Der_anonyme_Gamer";
    private static final String event_name="UPDATE_TIME";
    private static final String game="{ " +
            "\"game\": \""+name+"\"," +
            "\"game_display_name\": \"Clock for Steelseries keyboard\"," +
            "\"developer\": \""+dev+"\"" +
            "}";
    private static final String bind_event="{" +
            "  \"game\": \"" + name + "\"," +
            "  \"event\": \"" + event_name + "\"," +
            "  \"handlers\":[{" +
            "    \"device-type\":\"screened\"," +
            "    \"mode\":\"screen\"," +
            "    \"zone\":\"one\"," +
            "    \"datas\":[{" +
            "      \"has-text\":true," +
            "      \"arg\": \"(custom-text: (context-frame: self))\","+
            "      \"wrap\":2," +
            "      \"bold\":true," +
            "      \"icon-id\":15" +
            "    }]" +
            "  }]" +
            "}";
    private static String update_event= "{" +
            "\"game\": \""+name+"\"," +
            "\"event\": \""+event_name+"\"," +
            "\"data\": {\"value\": \"%s\"}" +
            "}";


    private final String param,url;

    public ApiParams(String param,String url){
        this.param=param;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public String getParameter(String... s) {
        return String.format(param,s);
    }
    public static final ApiParams BIND_EVENT=new ApiParams(bind_event,"bind_game_event");
    public static final ApiParams UPDATE_EVENT=new ApiParams(update_event,"game_event");
    public static final ApiParams GAME_METADATA=new ApiParams(game,"game_metadata");

}
