package com.chatvia.chatapp;

//import com.chatvia.chatapp.WS.ChatServerConfig;
import com.chatvia.chatapp.WS.ChatServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class ChatviaApplication {
	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(ChatviaApplication.class, args);
		customComponent();
//		context.getBean(ChatServerConfig.class);
	}

	public static void customComponent() throws IOException, InterruptedException {
		ChatServer s = new ChatServer();
		s.start();
		System.out.println("ChatServer started on port: " + s.getPort());

		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			String in = sysin.readLine();
			s.broadcast(in);
			if (in.equals("exit")) {
				s.stop(1000);
				break;
			}
		}
	}
}
