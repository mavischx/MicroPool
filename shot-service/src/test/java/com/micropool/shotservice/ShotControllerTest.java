package com.micropool.shotservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnPotTwoForAngle37Power72() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 37, "power": 72}
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome").value("POT_TWO"))
                .andExpect(jsonPath("$.inputAngle").value(37))
                .andExpect(jsonPath("$.inputPower").value(72));
    }

    @Test
    void shouldReturnFoulWhenModIs0() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 10, "power": 20}
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome").value("FOUL"));
    }

    @Test
    void shouldReturnMissWhenModIs1To4() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 10, "power": 21}
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome").value("MISS"));
    }

    @Test
    void shouldReturnPotOneWhenModIs5To8() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 10, "power": 25}
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.outcome").value("POT_ONE"));
    }

    @Test
    void shouldReturn400ForAngleAbove359() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 400, "power": 50}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForAngleBelowZero() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": -1, "power": 50}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForPowerAbove100() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 45, "power": 101}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForPowerBelowOne() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"angle": 45, "power": 0}
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForMissingFields() throws Exception {
        mockMvc.perform(post("/shots")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {}
                    """))
                .andExpect(status().isBadRequest());
    }
}
