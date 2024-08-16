package ua.kiev.its.assertstruct.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConversionUtils {
    public Object convert(String value) {
        if (value == null) {
            return null;
        }
        char ch = value.charAt(0);
        if (ch == 't') {
            if (value.equals("true"))
                return true;
        } else if (ch == 'f') {
            if (value.equals("false"))
                return false;
        } else if (ch == 'n') {
            if (value.equals("null"))
                return null;
        } else if (ch == '+' || ch == '-' || (ch >= '0' && ch <= '9') || ch == '.') {
            if (value.indexOf('.') >= 0 || value.indexOf('e') >= 0 || value.indexOf('E') >= 0) {
                try {
                    return Double.valueOf(value);
                } catch (Exception e) {
                    //ignore
                }
            } else {
                try {
                    return Integer.valueOf(value);
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return value;
    }

    public static boolean isSimpleJsonType(Object value) {
        return value==null || value instanceof String || value instanceof Number || value instanceof Boolean;
    }

    public static Boolean String2Boolean(String value) {
        return Boolean.getBoolean(value);
    }

    public static Integer Integer2Boolean(String value) {
        return Integer.parseInt(value);
    }

}
