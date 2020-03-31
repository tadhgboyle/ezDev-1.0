package me.aberdeener.ezdev.variables;

import me.aberdeener.ezdev.ezDev;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;

import java.util.HashMap;
import java.util.logging.Logger;

public class VariableManager {

    static Logger logger = ezDev.getInstance().getLogger();
    TreeBidiMap<Integer, String> variables = new TreeBidiMap<>();

    public static void initVariables() {
        logger.info("Initiating custom user Variables...");
        // Open JSON file and insert variables to hashmap
    }

    public boolean checkVariable(String variable) {
        if (variables.containsValue(variable)) return true;
        else return false;
    }

    public String getVariable(String variable) {
        if (checkVariable(variable)) return variables.get(variables.getKey(variable));
        else return null;
    }
}
