package PetServiceApp.utils;

import PetServiceApp.response.Error;
import PetServiceApp.response.Errors;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class GetErrorsUtil{

    private static final String file = "/errors.json";

    public static HashMap<Integer, String> getCodes() throws IOException {
        InputStream inputStream = GetErrorsUtil.class.getResourceAsStream(file);
        ObjectMapper mapper = new ObjectMapper();
        Errors errors = mapper.readValue(inputStream, Errors.class);

        HashMap<Integer, String> errorList = new HashMap<>();
        errors.getErrors().forEach(e -> errorList.put(e.getErrorCode(), e.getErrorMessage()));
        return errorList;
    }

    public static Error getError(int code) throws IOException {
        Error error = new Error();
        error.setErrorCode(code);
        HashMap<Integer,String> errors = getCodes();
        error.setErrorMessage(errors.get(code));
        return error;
        }
}
