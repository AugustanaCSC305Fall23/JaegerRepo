package edu.augustana;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CardDatabase {
    private final HashMap<Integer, Card> cardHashMap;

    public CardDatabase() {
        cardHashMap = new HashMap<>();
    }

    public void addCardPack(String filePath) throws IOException {
        System.out.println(filePath);
        BufferedReader br = new BufferedReader(new FileReader("/"+ filePath));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            String[] cardData = line.split(",");    // "splitting using comma because it's a csv"
            Card newCard = new Card(cardData);
            cardHashMap.put(newCard.getCardId(), newCard);
        }
    }

    public HashMap<Integer, Card> getCards() {
        return cardHashMap;
    }
}
