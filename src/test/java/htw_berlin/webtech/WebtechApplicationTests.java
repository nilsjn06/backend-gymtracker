package htw_berlin.webtech;

import com.fasterxml.jackson.databind.ObjectMapper;
import htw_berlin.webtech.rest.dto.ExerciseDto;
import htw_berlin.webtech.rest.dto.WorkoutViewDto;
import htw_berlin.webtech.rest.model.Muskelgruppe;
import htw_berlin.webtech.rest.repository.ExerciseRepository;
import htw_berlin.webtech.rest.repository.WorkoutRepository;
import htw_berlin.webtech.rest.repository.WorkoutSetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerMethodTests {

	@Autowired MockMvc mvc;
	@Autowired ObjectMapper om;

	// Variation: DB-Zustand prüfen (Delete etc.)
	@Autowired ExerciseRepository exerciseRepository;
	@Autowired WorkoutRepository workoutRepository;
	@Autowired WorkoutSetRepository workoutSetRepository;

	// ---------- Helper: Exercise anlegen (ObjectMapper -> robustes JSON) ----------
	private ExerciseDto createExercise(String name, Muskelgruppe mg) throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("name", name);
		payload.put("muskelgruppe", mg.name());

		String json = om.writeValueAsString(payload);

		String response = mvc.perform(post("/api/exercises")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		return om.readValue(response, ExerciseDto.class);
	}

	// ---------- Helper: Workout anlegen (ObjectMapper -> robustes JSON) ----------
	private WorkoutViewDto createWorkout(String date, String title) throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("date", date);
		payload.put("title", title);

		String json = om.writeValueAsString(payload);

		String response = mvc.perform(post("/api/workouts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		return om.readValue(response, WorkoutViewDto.class);
	}

	// ---------- Helper: Set hinzufügen (ObjectMapper -> KEIN 80,0 Problem) ----------
	private WorkoutViewDto addSet(long workoutId, long exerciseId, double weight, int reps) throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("exerciseId", exerciseId);
		payload.put("weight", weight);
		payload.put("reps", reps);

		String json = om.writeValueAsString(payload);

		String response = mvc.perform(post("/api/workouts/{id}/sets", workoutId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		return om.readValue(response, WorkoutViewDto.class);
	}

	// =========================================================
	// ExerciseController – 1 Test pro Methode (5)
	// =========================================================

	// 1) getAll()
	@Test
	void exercise_getAll_returns200_andJson() throws Exception {
		mvc.perform(get("/api/exercises"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	// 2) getById(Long id)
	@Test
	void exercise_getById_afterCreate_assertEqualsFields() throws Exception {
		ExerciseDto created = createExercise("Ex_" + System.currentTimeMillis(), Muskelgruppe.BRUST);

		String body = mvc.perform(get("/api/exercises/{id}", created.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		ExerciseDto fetched = om.readValue(body, ExerciseDto.class);
		assertEquals(created.getId(), fetched.getId());
		assertEquals(created.getName(), fetched.getName());
		assertEquals(created.getMuskelgruppe(), fetched.getMuskelgruppe());
	}

	// 3) create(@Valid ExerciseCreateDto dto)
	@Test
	void exercise_create_valid_returnsDtoWithId_jsonPath() throws Exception {
		Map<String, Object> payload = new HashMap<>();
		payload.put("name", "Bank_" + System.currentTimeMillis());
		payload.put("muskelgruppe", "BRUST");

		mvc.perform(post("/api/exercises")
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(payload)))
				.andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.muskelgruppe").value("BRUST"));
	}

	// 4) update(Long id, @Valid ExerciseCreateDto dto)
	@Test
	void exercise_update_changesName_assertEquals() throws Exception {
		ExerciseDto created = createExercise("Old_" + System.currentTimeMillis(), Muskelgruppe.BIZEPS);

		Map<String, Object> payload = new HashMap<>();
		payload.put("name", "NewName_" + System.currentTimeMillis());
		payload.put("muskelgruppe", "BIZEPS");

		String body = mvc.perform(put("/api/exercises/{id}", created.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(payload)))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		ExerciseDto updated = om.readValue(body, ExerciseDto.class);
		assertEquals(created.getId(), updated.getId());
		assertTrue(updated.getName().startsWith("NewName_"));
		assertEquals(Muskelgruppe.BIZEPS, updated.getMuskelgruppe());
	}

	// 5) delete(Long id)
	@Test
	void exercise_delete_removesFromDb_repositoryCheck() throws Exception {
		ExerciseDto created = createExercise("Del_" + System.currentTimeMillis(), Muskelgruppe.TRIZEPS);
		long id = created.getId();
		assertTrue(exerciseRepository.existsById(id));

		mvc.perform(delete("/api/exercises/{id}", id))
				.andExpect(status().is2xxSuccessful());

		assertFalse(exerciseRepository.existsById(id));
	}

	// =========================================================
	// WorkoutController – 1 Test pro Methode (7)
	// =========================================================

	// 6) createWorkout(@Valid CreateWorkoutDto dto)
	@Test
	void workout_create_valid_returnsDto_assertEquals() throws Exception {
		String title = "Push_" + System.currentTimeMillis();
		WorkoutViewDto dto = createWorkout("2026-01-08", title);

		assertNotNull(dto.getId());
		assertEquals("2026-01-08", dto.getDate());
		assertEquals(title, dto.getTitle());
	}

	// 7) getAllWorkouts()
	@Test
	void workout_getAll_containsCreatedWorkout_jsonPath() throws Exception {
		createWorkout("2026-01-08", "All_" + System.currentTimeMillis());

		mvc.perform(get("/api/workouts"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThan(0)));
	}

	// 8) getWorkout(Long id)
	@Test
	void workout_getById_afterCreate_assertEquals() throws Exception {
		WorkoutViewDto created = createWorkout("2026-01-08", "Detail_" + System.currentTimeMillis());

		String body = mvc.perform(get("/api/workouts/{id}", created.getId()))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		WorkoutViewDto fetched = om.readValue(body, WorkoutViewDto.class);
		assertEquals(created.getId(), fetched.getId());
		assertEquals(created.getTitle(), fetched.getTitle());
		assertEquals(created.getDate(), fetched.getDate());
	}

	// 9) addSet(Long id, AddSetDto dto)
	@Test
	void workout_addSet_returnsWorkoutWithAtLeastOneSet_assertStyle() throws Exception {
		ExerciseDto ex = createExercise("SetEx_" + System.currentTimeMillis(), Muskelgruppe.RUECKEN);
		WorkoutViewDto w = createWorkout("2026-01-08", "SetW_" + System.currentTimeMillis());

		WorkoutViewDto updated = addSet(w.getId(), ex.getId(), 80.0, 8);

		assertEquals(w.getId(), updated.getId());
		assertNotNull(updated.getExercises());
		assertTrue(updated.getExercises().size() >= 1);
		assertNotNull(updated.getExercises().get(0).getSets());
		assertTrue(updated.getExercises().get(0).getSets().size() >= 1);
	}

	// 10) deleteAllWorkouts()
	@Test
	void workout_deleteAllWorkouts_repositoryCheck() throws Exception {
		createWorkout("2026-01-08", "A_" + System.currentTimeMillis());
		createWorkout("2026-01-09", "B_" + System.currentTimeMillis());
		assertTrue(workoutRepository.count() > 0);

		mvc.perform(delete("/api/workouts"))
				.andExpect(status().is2xxSuccessful());

		assertEquals(0, workoutRepository.count());
		assertEquals(0, workoutSetRepository.count());
	}

	// 11) deleteWorkout(Long id)
	@Test
	void workout_deleteWorkout_removesOne_repositoryCheck() throws Exception {
		WorkoutViewDto w1 = createWorkout("2026-01-08", "Del1_" + System.currentTimeMillis());
		WorkoutViewDto w2 = createWorkout("2026-01-08", "Del2_" + System.currentTimeMillis());

		mvc.perform(delete("/api/workouts/{id}", w1.getId()))
				.andExpect(status().is2xxSuccessful());

		assertFalse(workoutRepository.existsById(w1.getId()));
		assertTrue(workoutRepository.existsById(w2.getId()));
	}

	// 12) removeExerciseFromWorkout(Long id, Long exerciseId)
	@Test
	void workout_removeExerciseFromWorkout_removesSets_assertEquals() throws Exception {
		ExerciseDto ex = createExercise("RemEx_" + System.currentTimeMillis(), Muskelgruppe.BRUST);
		WorkoutViewDto w = createWorkout("2026-01-08", "RemW_" + System.currentTimeMillis());

		// Erst Set hinzufügen
		WorkoutViewDto withSet = addSet(w.getId(), ex.getId(), 60.0, 10);
		assertNotNull(withSet.getExercises());
		assertTrue(withSet.getExercises().stream().anyMatch(e -> e.getSets() != null && !e.getSets().isEmpty()));

		// Dann entfernen
		String body = mvc.perform(delete("/api/workouts/{id}/exercises/{exerciseId}", w.getId(), ex.getId()))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();

		WorkoutViewDto afterRemoval = om.readValue(body, WorkoutViewDto.class);

		assertEquals(w.getId(), afterRemoval.getId());

		boolean stillHasAnySet = afterRemoval.getExercises() != null
				&& afterRemoval.getExercises().stream().anyMatch(e -> e.getSets() != null && !e.getSets().isEmpty());

		assertFalse(stillHasAnySet);
	}
}
