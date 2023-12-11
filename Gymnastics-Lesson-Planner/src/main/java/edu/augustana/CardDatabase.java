package edu.augustana;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.HashMap;

public class CardDatabase {
    private final HashMap<Integer, CardView> cardHashMap;

    public CardDatabase() {
        cardHashMap = new HashMap<>();
    }

    public void addCardPack(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/"+ filePath));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] cardData = line.split(",");    // "splitting using comma because it's a csv"
            Card newCard = new Card(cardData);
            cardHashMap.put(newCard.getCardId(), new CardView(newCard));
        }
    }

    public HashMap<Integer, CardView> getCards() {
        return cardHashMap;
    }
}
