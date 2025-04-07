package br.edu.iff.ccc.bsi.dpskt_api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DpsktApiApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Context Loads Successfully")
	void contextLoads() {
		assertThat(context).isNotNull();
	}

	@Test
	@DisplayName("Application Starts Successfully")
	void applicationStarts() {
		DpsktApiApplication.main(new String[] {});
	}

	@Test
	@DisplayName("Root Endpoint Returns 404 When No Controllers Defined")
	void rootEndpointReturns404() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isNotFound());
	}
}
