package com.ilias.translator_bot;

import com.ilias.translator_bot.TranslatorAPI.FreeGoogleTranslator;
import com.ilias.translator_bot.TranslatorAPI.Translator;
import com.ilias.translator_bot.Utils.LangNode;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorBot{

    private static final String BOT_USER_NAME = "TranslatePlsBot";
    private static final String BOT_TOKEN = "592446666:AAFj32lcT6WjLum8JZNFsu6v7tXJqSRmz-c";

    private static TelegramBot bot = new TelegramBot(BOT_TOKEN);

    private static int offset = 0;

    private static final LangNode enToRu = new LangNode("en", "ru");
    private static final LangNode ruToEn = new LangNode("ru", "en");

    private Map.Entry<String, String> entry;

    //TODO: REDO THIS FUCKING SHIT
    private static boolean isTranslating = false;

    private static Map<Long, Map.Entry> clients = new HashMap<Long, Map.Entry>();

    private static GetUpdates getUpdates = new GetUpdates().limit(100).offset(offset).timeout(0);
    public static void main(String[] args) {
        startBot();
    }
    private static void startBot() {
        while (true) {
            getUpdates.offset(offset);
            GetUpdatesResponse getUpdatesResponse;
            try {
                getUpdatesResponse = bot.execute(getUpdates);
            }catch(RuntimeException e){
                e.printStackTrace();
                continue;
            }
            List<Update> updatesList = getUpdatesResponse.updates();

            for (Update update : updatesList) {
                Message message = update.message();
                if(clients.containsKey(message.chat().id())){
                    Map.Entry langs = clients.get(message.chat().id());
                    translateWord(message, langs.getKey(), langs.getValue());
                    clients.remove(message.chat().id());
                }
                if(message.text().equals("/start")){
                    helloMessage(message);
                    continue;
                }

                if(message.text().equals("/translate") && !isTranslating){
                    translateCommand(message);
                    if(!clients.containsKey(message.chat().id())){
                        clients.put(message.chat().id(), new LangNode("en", "ru"));
                    }
                }
                if(message.text().equals("/toen")){
                    translateCommand(message);
                    if(!clients.containsKey(message.chat().id())){
                        clients.put(message.chat().id(), new LangNode("ru", "en"));
                    }
                }
            }
            if(!updatesList.isEmpty())
                offset = updatesList.get(updatesList.size()-1).updateId() + 1;
        }
    }
    private static void helloMessage(Message message){
        SendMessage request = new SendMessage(message.chat().id(), "Hello there, my little friend." +
                "To translate from russian to english type '/translate'. From russian to english type '/toen'. Good LUCK")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1);

        SendResponse response = bot.execute(request);
        boolean ok = response.isOk();
    }
    private static void translateWord(Message message, Object from, Object to){
        Translator translator = new FreeGoogleTranslator();
        String s = "";
        try {
            s = translator.requestTranslation(message.text(), from, to);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(String.format("Translating from %s to %s", from, to));
        System.out.println(s);
        SendMessage request = new SendMessage(message.chat().id(), s)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(message.messageId());

        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();
    }

    private static void translateCommand(Message msg){
        SendMessage request = new SendMessage(msg.chat().id(), "Send word to translate")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(msg.messageId());

        SendResponse sendResponse = bot.execute(request);
        boolean ok = sendResponse.isOk();


    }
}
