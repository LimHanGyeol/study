package vendingmachine;

import java.util.ArrayList;
import java.util.List;

public class Changes {

    private int amount;

    public Changes(int amount) {
        this.amount = amount;
    }

    List<CoinSet> coin() {
        List<CoinSet> list = new ArrayList<>();

        for (CoinSet coin : CoinSet.values()) {
            int count = amount / coin.getValue();
            for (int i = 0; i < count; i++) {
                list.add(coin);
            }
            amount = amount - (count * coin.getValue());
        }
        return list;
    }
}
