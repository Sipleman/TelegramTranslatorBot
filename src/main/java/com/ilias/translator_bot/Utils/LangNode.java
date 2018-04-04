package com.ilias.translator_bot.Utils;

import java.util.Map;

public class LangNode implements Map.Entry {
    String first;
    String last;

    public Object getKey() {
        return first;
    }

    public Object getValue() {
        return last;
    }

    public Object setValue(Object value) {
        return last = (String) value;
    }
    public LangNode(String first, String last){
        this.first = first;
        this.last = last;
    }
}
