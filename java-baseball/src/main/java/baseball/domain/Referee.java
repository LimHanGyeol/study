package baseball.domain;

import java.util.List;

public class Referee {

    /**
     * 몇 개의 숫자가 같은지 알아낸 뒤
     * 스트라이크의 개수를 구해 뺀다.
     * 남은 수는 볼의 개수이다.
     */
    public String compare(List<Integer> computer, List<Integer> player) {
        Judgement judgement = new Judgement();
        int correctCount = judgement.correctCount(computer, player);

        int strike = 0;
        for (int placeIndex = 0; placeIndex < computer.size(); placeIndex++) {
            if (judgement.hasPlace(computer, placeIndex, player.get(placeIndex))) {
                strike++;
            }
        }
        int ball = correctCount - strike;

        if (strike == 0) {
            return "아웃";
        }

        return strike + " 스트라이크" + ball + " 볼";
    }
}
