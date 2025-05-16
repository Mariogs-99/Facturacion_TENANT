package sv.gov.cnr.cnrpos.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    public static String[] splitBySecondSpace(String input) {
        int secondSpaceIndex = input.indexOf(' ', input.indexOf(' ') + 1);
        if (secondSpaceIndex == -1) {
            return new String[]{input}; // No second space found
        }
        String part1 = input.substring(0, secondSpaceIndex);
        String part2 = input.substring(secondSpaceIndex + 1);
        return new String[]{part1, part2};
    }


}
