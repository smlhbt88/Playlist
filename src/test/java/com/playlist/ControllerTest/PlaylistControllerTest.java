package com.playlist.ControllerTest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(){
        playlistRepository.deleteAll();
    }

    @Test
    public void createPlaylist() throws Exception {
        PlaylistDto playlist = new PlaylistDto("playlist1", null);

        String response = mockMvc.perform(post("/playlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playlist)))
                .andExpect(status().isCreated())
                .andDo(document("createplaylist"))
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("successful",response);

        Playlist result = playlistRepository.findByName("playlist1");

        assertTrue(result.getSong()==null);
    }

    @Test
    public void createPlaylistWithExistingName() throws Exception {
        PlaylistDto playlist = new PlaylistDto("playlist1", null);
        Playlist playlistEntity = new Playlist("playlist1", null);

        playlistRepository.save(playlistEntity);

        String response = mockMvc.perform(post("/playlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playlist)))
                .andExpect(status().isBadRequest())
                .andDo(document("createplaylistwithexitingname"))
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("unsuccessful",response);

    }

}
