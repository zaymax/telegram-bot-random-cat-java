import com.github.rkumsher.collection.IterableUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyBot extends TelegramLongPollingBot {
    private Set<String> catSet = new HashSet<>();


    @Override
    public void onUpdateReceived(Update update) {
        String catLink;

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            //Set commands
            if (message_text.equals("/help")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("In case of random string, this bot will just echo it back\nAvailible commands are: /help  /cat");
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (message_text.equals("/cat_pic")) {
                if (catSet.isEmpty()) {
                    fillCatSet();
                }
                catLink = IterableUtils.randomFrom(catSet);
                SendPhoto photo = new SendPhoto()
                        .setChatId(chat_id)
                        .setPhoto(catLink)
                        .setCaption("Random cat photo");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                rowInline.add(new InlineKeyboardButton().setText("Share to friend").setSwitchInlineQuery(catLink));    //"http://thecatapi.com/api/images/get"
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                photo.setReplyMarkup(markupInline);

                try {
                    sendPhoto(photo); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = new SendMessage()
                        .setChatId(chat_id)
                        .setText(message_text);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public String getBotUsername() {
        return "echotest001_bot";
    }

    @Override
    public String getBotToken() {
        return "488222275:AAGEXDbebQ-uAFF2z81BAtULygjFEVp5rN8";
    }

    private void fillCatSet() {
        catSet.add("http://25.media.tumblr.com/tumblr_m12hnp9KdP1qbe5pxo1_1280.jpg");
        catSet.add("http://25.media.tumblr.com/tumblr_m4362vO7vH1qzuoq3o1_1280.jpg");
        catSet.add("http://24.media.tumblr.com/tumblr_lgzjn88B9h1qfyzelo1_500.jpg");
        catSet.add("http://24.media.tumblr.com/Qo5gA2rU2pou0glauKlzyESwo1_1280.jpg");
        catSet.add("http://24.media.tumblr.com/tumblr_mb69oxOHoD1qhwmnpo1_1280.jpg");
        catSet.add("http://24.media.tumblr.com/tumblr_lll0l6XBIr1qenqklo1_1280.jpg");
        catSet.add("http://24.media.tumblr.com/tumblr_m3na9kmEO61qzccddo1_500.jpg");
        catSet.add("http://25.media.tumblr.com/tumblr_m4ol05GC2h1qd477zo1_1280.jpg");
        catSet.add("http://24.media.tumblr.com/tumblr_m1k2lqeRAX1r6j0hbo1_500.jpg");
    }
}
