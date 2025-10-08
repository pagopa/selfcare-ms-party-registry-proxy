package it.pagopa.selfcare.party.registry_proxy.web.config;


import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

public class AsyncConfigTest {

    private final AsyncConfig asyncConfig = new AsyncConfig();

    @Test
    void testStorageTaskExecutorBeanCreation() {
        Executor executor = asyncConfig.storageTaskExecutor();

        assertNotNull(executor);
        assertInstanceOf(ThreadPoolTaskExecutor.class, executor);
    }

    @Test
    void testStorageTaskExecutorConfiguration() {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfig.storageTaskExecutor();

        assertEquals(3, executor.getCorePoolSize());
        assertEquals(10, executor.getMaxPoolSize());
        assertEquals(50, executor.getQueueCapacity());
        assertTrue(executor.getThreadNamePrefix().startsWith("StorageAsync-"));
    }

    @Test
    void testStorageTaskExecutorIsInitialized() {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfig.storageTaskExecutor();

        assertNotNull(executor.getThreadPoolExecutor());
        assertTrue(executor.getThreadPoolExecutor().isShutdown() == false);
    }

    @Test
    void testStorageTaskExecutorCanExecuteTasks() throws InterruptedException {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfig.storageTaskExecutor();
        final boolean[] taskExecuted = {false};

        executor.execute(() -> taskExecuted[0] = true);

        Thread.sleep(500);

        assertTrue(taskExecuted[0]);

        executor.shutdown();
    }
}
