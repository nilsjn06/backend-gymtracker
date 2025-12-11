package htw_berlin.webtech;

import htw_berlin.webtech.rest.model.Exercise;
import htw_berlin.webtech.rest.model.Muskelgruppe;
import htw_berlin.webtech.rest.repository.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebtechApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebtechApplication.class, args);
	}

	@Bean
	CommandLineRunner initExercises(ExerciseRepository exerciseRepository) {
		return args -> {
			if (exerciseRepository.count() == 0) {
				exerciseRepository.save(Exercise.builder()
						.name("Bankdrücken")
						.muskelgruppe(Muskelgruppe.BRUST)
						.build());

				exerciseRepository.save(Exercise.builder()
						.name("Hammercurls")
						.muskelgruppe(Muskelgruppe.BIZEPS)
						.build());

				exerciseRepository.save(Exercise.builder()
						.name("Latziehen")
						.muskelgruppe(Muskelgruppe.RUECKEN)
						.build());
			}
		};
	}

}
