package baseball;

import baseball.domain.Judgement;
import baseball.domain.NumberGenerator;

import java.util.List;

/**
 * 객체 지향 프로그래밍
 * 1. 기능을 가지고 있는 클래스를 인스턴스화(=객체)한다.
 * 2. 필요한 기능을 역할에 맞는 인스턴스가 수행하게 한다. (의인화)
 * 3. 각 결과를 종합한다.
 */
public class BaseballApplication {


    public static void main(String[] args) {
//        NumberGenerator numberGenerator = new NumberGenerator();
//        List<Integer> randomNumbers = numberGenerator.createRandomNumbers();
//        System.out.println("randomNumbers = " + randomNumbers);

        Judgement judgement = new Judgement();
        int count = judgement.correctCount(List.of(1, 2, 3), List.of(1, 2, 3));
        System.out.println(count);
    }
}
