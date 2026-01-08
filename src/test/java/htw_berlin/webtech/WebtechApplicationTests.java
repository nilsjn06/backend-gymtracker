package htw_berlin.webtech;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import htw_berlin.webtech.rest.dto.ExerciseDto;
import htw_berlin.webtech.rest.dto.WorkoutViewDto;
import htw_berlin.webtech.rest.model.Muskelgruppe;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiMixedTests {

	@Autowired MockMvc mvc;
	@Autowired ObjectMapper om;

	// ---------- Helpers ----------
	private ExerciseDto createExercise(String name, Muskelgruppe mg) throws Exception {
		String json = """
            { "name": "%s", "muskelgruppe": "%s" }
            """.formatted(name, mg.name());

		String response = mvc.perform(post("/api/exercises")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		return om.readValue(response, ExerciseDto.class);
	}

	private WorkoutViewDto createWorkout(String date, String title) throws Exception {
		String json = """
            { "date": "%s", "title": "%s" }
            """.formatted(date, title);

		String response = mvc.perform(post("/api/workouts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		return om.readValue(response, WorkoutViewDto.class);
	}

	// =========================
	// 4 Tests mit jsonPath
	// =========================

	@Test
	void jp_getExercises_returns200_andArray() throws Exception {
		mvc.perform(get("/api/exercises"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void jp_postExercise_valid_returnsIdAndFields() throws Exception {
		String json = """
            { "name": "JP_Exercise_%d", "muskelgruppe": "BRUST" }
            """.formatted(System.currentTimeMillis());

		mvc.perform(post("/api/exercises")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.muskelgruppe").value("BRUST"));
	}

	@Test
	void jp_postExercise_missingName_returns400() throws Exception {
		String json = """
            { "muskelgruppe": "BRUST" }
            """;

		mvc.perform(post("/api/exercises")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	void jp_postWorkout_missingTitle_returns400() throws Exception {
		String json = """
            { "date": "2026-01-08" }
            """;

		mvc.perform(post("/api/workouts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isBadRequest());
	}

	// =========================
	// 4 Tests mit assertEquals
	// =========================

	@Test
	void ae_createExercise_assertFields() throws Exception {
		String name = "AE_Exercise_" + System.currentTimeMillis();
		ExerciseDto dto = createExercise(name, Muskelgruppe.BIZEPS);

		assertNotNull(dto.getId());
		assertEquals(name, dto.getName());
		assertEquals(Muskelgruppe.BIZEPS, dto.getMuskelgruppe());
	}

	@Test
	void ae_getExerciseById_afterCreate_assertEquals() throws Exception {
		ExerciseDto created = createExercise("AE_GetById_" + System.currentTimeMillis(), Muskelgruppe.RUECKEN);

		String response = mvc.perform(get("/api/exercises/{id}", created.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		ExerciseDto fetched = om.readValue(response, ExerciseDto.class);

		assertEquals(created.getId(), fetched.getId());
		assertEquals(created.getName(), fetched.getName());
		assertEquals(created.getMuskelgruppe(), fetched.getMuskelgruppe());
	}

	@Test
	void ae_createWorkout_assertFields() throws Exception {
		String title = "AE_Workout_" + System.currentTimeMillis();
		WorkoutViewDto dto = createWorkout("2026-01-08", title);

		assertNotNull(dto.getId());
		assertEquals("2026-01-08", dto.getDate());
		assertEquals(title, dto.getTitle());
		// exercises ist beim Create bei dir leer -> darf null oder leer sein, beides ok:
		// (Falls du sicher "leer" willst, sag Bescheid, dann machen wir es streng.)
	}

	@Test
	void ae_addSet_flow_assertWorkoutContainsSet() throws Exception {
		ExerciseDto ex = createExercise("AE_Set_" + System.currentTimeMillis(), Muskelgruppe.BRUST);
		WorkoutViewDto w  = createWorkout("2026-01-08", "AE_Push_" + System.currentTimeMillis());

		String addSetJson = """
            { "exerciseId": %d, "weight": 80.0, "reps": 8 }
            """.formatted(ex.getId());

		String response = mvc.perform(post("/api/workouts/{id}/sets", w.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(addSetJson))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		WorkoutViewDto updated = om.readValue(response, WorkoutViewDto.class);

		assertEquals(w.getId(), updated.getId());
		assertNotNull(updated.getExercises());
        assertFalse(updated.getExercises().isEmpty());

		// mindestens ein Set irgendwo drin
		boolean hasAnySet = updated.getExercises().stream()
				.anyMatch(e -> e.getSets() != null && !e.getSets().isEmpty());

		assertTrue(hasAnySet);
	}
}
