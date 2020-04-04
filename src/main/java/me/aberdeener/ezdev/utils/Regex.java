package me.aberdeener.ezdev.utils;

import java.util.regex.Pattern;

public class Regex {

    public static Pattern ACTION_QUOTES = Pattern.compile("(.*?)");

    public static Pattern ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9]+$");

    public static Pattern VARIABLE_BRACKETS = Pattern.compile("\\{(.*?)}");

}
