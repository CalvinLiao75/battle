import java.util.HashMap;
import java.util.Map;

public class ParameterBuilder {
    public static String build(HashMap<String, String> data) {
        String output = "";
        boolean firstComplete = false;

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (!firstComplete) {
                output += key + "=" + value;
                firstComplete = true;
            } else output += "&" + key + "=" + value;
        }

        return output;
    }
}
