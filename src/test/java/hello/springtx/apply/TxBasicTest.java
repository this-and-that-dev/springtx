package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    public BasicService basicService;

    @Test
    public void proxyCheck() {
        log.info("aop class = {}", basicService.getClass());
        Assertions.assertTrue(AopUtils.isAopProxy(basicService));

    }

    @Test
    public void txTest() {
        basicService.tx();
        basicService.noneTx();
    }

    @TestConfiguration
    public static class TxApplyBasicConfig {

        @Bean
        public BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    public static class BasicService {
        @Transactional
        public void tx() {

            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("call tx = {}", txActive);
        }

        public void noneTx() {
            log.info("call noneTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("call noneTx = {}", txActive);
        }


    }


}
