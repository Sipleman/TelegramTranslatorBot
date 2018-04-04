package com.ilias.translator_bot.TranslatorAPI;

import java.io.IOException;

public interface Translator {
    String requestTranslation(String text, Object from, Object to) throws IOException;
}
