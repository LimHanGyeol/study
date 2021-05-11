import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Junit5Test {

    @Test
    void defaultTest() {
        assertThat(true).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void parameterizedTest(int value) {
        assertThat(value).isEqualTo(value);
    }
}
