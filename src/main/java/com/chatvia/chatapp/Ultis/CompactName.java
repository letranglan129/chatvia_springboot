package com.chatvia.chatapp.Ultis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompactName {
    public static Map<String, String> divideFullName(String name) {
        if (name instanceof String) {
            String[] parts = name.split(" ");
            String lastname = parts[parts.length - 1];
            String firstname = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 1));

            Map<String, String> result = new HashMap<>();
            result.put("firstname", firstname);
            result.put("lastname", lastname);

            return result;
        }
        return null;
    }

    public static String get(String name) {
        Map<String, String> nameDivided = CompactName.divideFullName(name);

        try {
            if (nameDivided != null && nameDivided.size() > 1) {
                return String.valueOf(nameDivided.get("firstname").charAt(0)) + nameDivided.get("lastname").charAt(0);
            }
        } catch (Exception e) {
            return String.valueOf(nameDivided.get("lastname").charAt(0));
        }

        return "";
    }
}
