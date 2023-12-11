package edu.augustana;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CardDatabase {
    private final HashMap<Integer, CardView> cardHashMap;

    public CardDatabase() {
        cardHashMap = new HashMap<>();
    }

    public void addCardPack(String filePath, String newDirName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/" + filePath));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] cardData = line.split(",");    // "splitting using comma because it's a csv"
            Card newCard = new Card(cardData, newDirName);
            cardHashMap.put(newCard.getCardId(), new CardView(newCard));
        }
    }

    public HashMap<Integer, CardView> getCards() {
        return cardHashMap;
    }
}
