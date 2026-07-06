package com.damik3;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    static List<Character> stringToCharList(String string) {
        List<Character> charList = new ArrayList<>();
        for (char c : string.toCharArray()) {
            charList.add(c);
        }
        return charList;
    }

}
