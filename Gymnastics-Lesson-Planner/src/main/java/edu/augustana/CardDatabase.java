package edu.augustana;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class CardDatabase {
    private final HashMap<String, Card> cardHashMap;

    public CardDatabase() {
        cardHashMap = new HashMap<>();
    }

    public void addCardPack(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath.substring(6)));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] cardData = line.split(",");    // "splitting using comma because it's a csv"
            Card newCard = new Card(cardData);
            cardHashMap.put(cardData[0], newCard);
        }
    }

    public HashMap<String, Card> getCards() {
        return cardHashMap;
    }
}
