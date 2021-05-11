package vendingmachine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Red(질문) -> Green(응답) -> Refactor(정제)
 */
class ChangeModuleTest {

    @Test
    void 자판기_생성() {
        ChangeModule changeModule = new ChangeModule();
        assertThat(changeModule).isNotNull();
    }

    @ParameterizedTest(name = "자판기에 {0}원을 넣으면 {0}원이 들어있음을 알 수 있다.")
    @ValueSource(ints = {500, 1000})
    void 자판기에_n원을_넣으면_n원이_들어있음을_알_수_있다(int changes) {
        ChangeModule changeModule = new ChangeModule();
        changeModule.put(changes);
        assertThat(changeModule.getChanges()).isEqualTo(changes);
    }

    @Test
    void _1000원이_들어있는_자판기에_500원을_넣으면_1500원이_들어있음을_알_수_있다() {
        ChangeModule changeModule = new ChangeModule(1000);
        changeModule.put(500);
        assertThat(changeModule.getChanges()).isEqualTo(1500);
    }

    @Test
    void _500원이_들어있는_자판기에_500원을_차감하면_0원이_된다() {
        ChangeModule changeModule = new ChangeModule(500);
        changeModule.withdraw(500);
        assertThat(changeModule.getChanges()).isEqualTo(0);
    }

    @Test
    void _0원이_들어있는_자판기에_500원을_차감할_수_없다() {
        ChangeModule changeModule = new ChangeModule();
        assertThat(changeModule.getChanges()).isEqualTo(0);
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> changeModule.withdraw(500));
    }
}
