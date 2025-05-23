package LagranaExam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		run();
		SpringApplication.run(DemoApplication.class, args);
	}

	public static void run(){
		String plainPassword = "secret";
		String hash = new BCryptPasswordEncoder().encode(plainPassword);
		System.out.println(hash);
	}

}