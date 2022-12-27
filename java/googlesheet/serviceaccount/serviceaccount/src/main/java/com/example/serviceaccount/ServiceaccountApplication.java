package com.example.serviceaccount;

import com.example.serviceaccount.service.ServiceAccount;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class ServiceaccountApplication {

	public static void main(String[] args) throws GeneralSecurityException, IOException {

		ServiceAccount serviceAccount = new ServiceAccount();

		serviceAccount.main();

		SpringApplication.run(ServiceaccountApplication.class, args);
	}

}
