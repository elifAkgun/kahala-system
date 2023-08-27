package com.bol.kahala.controller.exception.handler;


import com.bol.kahala.controller.GameController;
import com.bol.kahala.service.GameService;
import com.bol.kahala.service.input.GameStatusServiceInput;
import com.bol.kahala.service.output.GameStatusServiceOutput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.bol.kahala.helper.GameTestDataHelper.GAME_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CommonRequestTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private GameService gameService;

    @Test
    void givenMalformedJsonPayload_whenGetGameCalled_thenReturnBadRequestResponse() throws Exception {
        // Given
        String malformedJson = "{invalid json}";
        given(gameService.getGame(any(GameStatusServiceInput.class)))
                .willReturn(GameStatusServiceOutput.builder().game(null).build());

        // When and Then
        mockMvc.perform(post("/games/move/{gameId}", GAME_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }
}