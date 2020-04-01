package me.aberdeener.ezdev.variables;

import me.aberdeener.ezdev.ezDev;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import java.io.File;
import java.util.logging.Logger;

public class VariableManager {

    static Logger logger = ezDev.getInstance().getLogger();
    static File variables_file = new File(ezDev.getInstance().getDataFolder() + File.separator + "variables.json");
    static TreeBidiMap<String, String> variables = new TreeBidiMap<>();

    static boolean VARIABLES_ENABLED = false;

    public static void initVariables() {
        logger.info("Initiating custom user Variables...");
        // Open JSON file and insert variables to hashmap
        try {
            VARIABLES_ENABLED = true;
        } catch (Exception e) {
            e.printStackTrace();
            VARIABLES_ENABLED = false;
        }
    }

    public String getVariable(String variable) {
        if (VARIABLES_ENABLED && variables.containsValue(variable)) return variables.get(variables.getKey(variable));
        else return variable;
    }
}
