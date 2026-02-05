package com.myludoapp.socketorchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocketOrchestratorApplication {

	public  SocketOrchestratorApplication(){
		System.out.println("Inside SocketOrchestratorApplication constructor..");
//		int proc = Runtime.getRuntime().availableProcessors();
//		System.out.println("Processors : " + proc);
//		ExecutorService executorService = Executors.newScheduledThreadPool(proc);
//		Runnable runnable = ()->{new SocketServer(executorService);};
//		CompletableFuture.runAsync(runnable,executorService);
	}

	public static void main(String[] args) {
		SpringApplication.run(SocketOrchestratorApplication.class, args);
	}


}
