package vendingmachine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CoinSet {
    _500_COIN(500), _100_COIN(100), _50_COIN(50), _10_COIN(10);

    private final int value;

    CoinSet(int value) {
        this.value = value;
    }

    public static CoinSet valueOf(int amount) {
        return Arrays.stream(values())
                .filter(coin -> coin.getValue() == amount)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(amount + "원의 동전은 존재하지 않습니다."));
    }

    public static List<CoinSet> highestOrder() {
        return Arrays.stream(values())
                .sorted((o1, o2) -> Integer.compare(o2.getValue(), o1.getValue()))
                .collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }
}
