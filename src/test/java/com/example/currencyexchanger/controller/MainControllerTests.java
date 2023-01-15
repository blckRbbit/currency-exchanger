package com.example.currencyexchanger.controller;

import com.example.currencyexchanger.annotation.MockMvcTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@MockMvcTest
public class MainControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Value("${test.answer}")
    private String answer;

    @Test
    public void startTest() throws Exception {
        mockMvc.perform(
                get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void getTest() throws Exception {
        mockMvc.perform(
                        get("/RUB")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void getCurrenciesCodeTest() throws Exception {
        mockMvc.perform(
                        get("/currencies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(answer)));
    }
}
