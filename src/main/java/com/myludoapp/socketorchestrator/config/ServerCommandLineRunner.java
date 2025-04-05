package com.myludoapp.socketorchestrator.config;


import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {
        log.info(".....starting SocketIOServer server.....");
        try {
            server.start();
        } catch (Exception e) {
            log.info(".....shutting down SocketIOServer server.....[{}]",e);
            Runtime.getRuntime().exit(0);
        }


//        int proc = Runtime.getRuntime().availableProcessors();
//        ExecutorService executorService = Executors.newScheduledThreadPool(proc);
//		Runnable runnable = ()->{new CacheStorage(executorService);};
//		CompletableFuture.runAsync(runnable,executorService);
    }


}