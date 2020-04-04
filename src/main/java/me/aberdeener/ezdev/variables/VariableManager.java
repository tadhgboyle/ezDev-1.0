package me.aberdeener.ezdev.variables;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.utils.Regex;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableManager {

    static Logger logger = ezDev.getInstance().getLogger();
    static File variables_file = new File(ezDev.getInstance().getDataFolder() + File.separator + "variables.json");
    static String variables_file_string;
    static boolean VARIABLES_ENABLED = false;
    static Gson gson = new Gson();

    public static void initVariables() {
        logger.info("Initiating custom user variables...");
        try {
            variables_file_string = new String(Files.readAllBytes(variables_file.toPath()));
            VARIABLES_ENABLED = true;
            logger.info("Custom user variables enabled...");
        } catch (Exception e) {
            e.printStackTrace();
            VARIABLES_ENABLED = false;
        }
    }

    public static boolean isVariable(String request) {
        if (request.endsWith("\"") || request.startsWith("\"")) request = request.replaceAll("\"", "");
        Matcher matcher = Regex.VARIABLE_BRACKETS.matcher(request);
        if (matcher.matches()) {
            request = request.replace("{", "");
            request = request.replace("}", "");
            try {
                JsonObject jobj = gson.fromJson(variables_file_string, JsonObject.class);
                String s = jobj.get(request).getAsString();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getVariable(String request) {
        if (isVariable(request) && VARIABLES_ENABLED) {
            if (request.endsWith("\"") || request.startsWith("\"")) request = request.replaceAll("\"", "");
            request = request.replace("{", "");
            request = request.replace("}", "");
            JsonObject jobj = gson.fromJson(variables_file_string, JsonObject.class);
            return jobj.get(request).getAsString();
        } else return request;
    }
}
