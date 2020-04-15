package com.omer.socialapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omer.socialapp.model.PlainPage;

@SpringBootTest
@AutoConfigureMockMvc
public class PageControllerTest
{	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	
	@Test
	public void pageCreationFailureTest() throws Exception {
		PlainPage page = new PlainPage(" leading space", null);
		String body = objectMapper.writeValueAsString(page);

		mockMvc.perform(post("/pages/plain").contentType(MediaType.APPLICATION_JSON).content(body))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void pageCreationPassTest() throws Exception {
		PlainPage page = new PlainPage("valid page", "Page description...");
		String body = objectMapper.writeValueAsString(page);

		mockMvc.perform(post("/pages/plain").contentType(MediaType.APPLICATION_JSON).content(body))
				.andDo(print())
				.andExpect(status().isCreated());
	}
	
	@Test
	public void pageGetallTest() throws Exception {
		mockMvc.perform(get("/pages").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
