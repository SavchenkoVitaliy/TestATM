import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CardStorage {
    private static final String FILE_NAME = "src/cards.txt";

    public CardStorage() {
    }

    public static List<Card> loadCards() {
        List<Card> cards = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/cards.txt"));

            String line;
            try {
                while((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    String cardNumber = parts[0];
                    String pinCode = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    boolean blocked = Boolean.parseBoolean(parts[3]);
                    Date blockTime = blocked ? new Date(Long.parseLong(parts[4])) : null;
                    cards.add(new Card(cardNumber, pinCode, balance, blocked, blockTime));
                }
            } catch (Throwable var11) {
                try {
                    br.close();
                } catch (Throwable var10) {
                    var11.addSuppressed(var10);
                }

                throw var11;
            }

            br.close();
        } catch (IOException var12) {
            IOException e = var12;
            e.printStackTrace();
        }

        return cards;
    }

    public static void saveCards(List<Card> cards) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/cards.txt"));

            try {
                Iterator var2 = cards.iterator();

                while(var2.hasNext()) {
                    Card card = (Card)var2.next();
                    String var10001 = card.getCardNumber();
                    bw.write(var10001 + " " + card.getPinCode() + " " + card.getBalance() + " " + card.isBlocked() + " " + (card.isBlocked() ? card.getBlockTime().getTime() : "") + "\n");
                }
            } catch (Throwable var5) {
                try {
                    bw.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            bw.close();
        } catch (IOException var6) {
            IOException e = var6;
            e.printStackTrace();
        }

    }
}

