import com.github.rkumsher.collection.IterableUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONObject;


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
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();


            //Set commands
            if (message_text.equals("/start")) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("In case of random string, this bot will just echo it back\nAvailible commands are: /help  /cat");

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setResizeKeyboard(true);
                List keyboard = new ArrayList<>();
                KeyboardRow row = new KeyboardRow();
                row.add("Give me some cat picture");
                keyboard.add(row);
                keyboardMarkup.setKeyboard(keyboard);

                message.setReplyMarkup(keyboardMarkup);

                //KeyboardButton button = new KeyboardButton();

                //button.setText("Cat to All");

                try {
                    execute(message); // Sending our message object to user
                    check(user_first_name, user_last_name, (int) user_id, user_username);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
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
            if (message_text.equals("/cat_pic") || message_text.equals("Give me some cat picture")) {
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
        return "randCat_bot";
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

    private String check(String first_name, String last_name, int user_id, String username) {
        // Set loggers
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);

        MongoClientURI connectionString = new MongoClientURI("mongodb://zaymax:<q1w2e3r4>@cluster0-shard-00-00-8vhff.mongodb.net:27017,cluster0-shard-00-01-8vhff.mongodb.net:27017,cluster0-shard-00-02-8vhff.mongodb.net:27017/admin?replicaSet=Cluster0-shard-0&ssl=true");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_tgBot");
        MongoCollection<Document> collection = database.getCollection("users");

        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
        if (found == 0) {
            Document doc = new Document("first_name", first_name)
                    .append("last_name", last_name)
                    .append("id", user_id)
                    .append("username", username);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            return "no_exists";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            return "exists";
        }
    }
}
