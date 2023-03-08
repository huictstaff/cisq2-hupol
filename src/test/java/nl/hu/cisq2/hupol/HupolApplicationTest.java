package nl.hu.cisq2.hupol;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HupolApplicationTest {
    @Test
    @DisplayName("application boots")
    public void applicationContextTest() {
        assertDoesNotThrow(() -> HupolApplication.main(new String[] {}));
    }
}