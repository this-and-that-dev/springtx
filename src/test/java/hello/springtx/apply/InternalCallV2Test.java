 package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class InternalCallV2Test {

    @Autowired CallService callService;
    @Autowired InternalService internalService;

    @Test
    void printProxy() {
        log.info("callService class = {}", callService.getClass());
    }

    @Test
    void internalCall() {
        internalService.internal();
    }

    @Test
     void externalCall() {
        callService.external();
    }

    @TestConfiguration
    public static class InternalCallConfig {

        @Bean
        public CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        public InternalService internalService() {
            return new InternalService();
        }

    }

    @Slf4j
    @RequiredArgsConstructor
    public static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx txActive = {}", txActive);
        }

    }
    public static class InternalService {
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }
        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx txActive = {}", txActive);
            log.info("getCurrentTransactionName = {}", TransactionSynchronizationManager.getCurrentTransactionName());
        }


    }

}
