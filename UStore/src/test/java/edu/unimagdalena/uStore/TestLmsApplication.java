
package edu.unimagdalena.uStore;

import org.springframework.boot.SpringApplication;

public class TestLmsApplication{
	public static void main(String[] args){
		SpringApplication.from(UStoreApplication::main).with(TestcontainersConfiguration.class).run(args);
	}
}
