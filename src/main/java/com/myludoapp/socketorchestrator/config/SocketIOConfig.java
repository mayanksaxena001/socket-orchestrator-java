package com.myludoapp.socketorchestrator.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class SocketIOConfig {

    @Value("${socket-server.host}")
    private String host;

    @Value("${socket-server.port}")
    private Integer port;


    @Value("${cors.origin}")
    private String origin;
    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        //cors
//        String whitelist = "http://localhost:8085, http://129.168.0.105:8085, http://172.18.5.3:8085";
        config.setOrigin(origin);
        //  config.setContext("/socket.io");
        return new SocketIOServer(config);
    }


    @Bean
    public ExecutorService executorService() {
        int proc = Runtime.getRuntime().availableProcessors();
        return Executors.newScheduledThreadPool(proc);
    }

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource primaryDataSource() {
//        return DataSourceBuilder.create().build();
//    }

}