package sv.gov.cnr.factelectrcnrservice.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Utils {

    public static ResponseEntity<Object> jsonResponse(Integer code, String message, Object data) {
        Map<String, Object> response = new HashMap<>();

        response.put("code", code != null ? code : 500);
        response.put("data", data);
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatusCode.valueOf(code));
    }
}
