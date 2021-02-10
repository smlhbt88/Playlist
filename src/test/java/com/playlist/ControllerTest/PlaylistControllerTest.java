package com.playlist.ControllerTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.models.Song;
import com.playlist.models.SongDto;
import com.playlist.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@Transactional
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

        assertTrue(result.getSongs()==null);
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

    @Test
    public void createPlaylistWithoutName() throws Exception {
        PlaylistDto playlist = new PlaylistDto();

        String response = mockMvc.perform(post("/playlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playlist)))
                .andExpect(status().isBadRequest())
                .andDo(document("createplaylistwithoutname"))
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals("name is required",response);

    }

    @Test
    public void addSongToPlayList() throws Exception {
        List<Song> songList = new ArrayList<>();
        Song song1  = new Song("Song 1");
        Song song2  = new Song("Song 2");
        songList.add(song1);
        Playlist playlist = new Playlist("playlist1", songList);
        playlistRepository.save(playlist);

        SongDto songDto = new SongDto("Song 2");

        mockMvc.perform(patch("/playlist/playlist1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isCreated())
                .andDo(document("addSongToPlayList"));

        Playlist playlist1FromDB = playlistRepository.findByName("playlist1");

        assertEquals(2, playlist1FromDB.getSongs().size());
        assertEquals(song1, playlist1FromDB.getSongs().get(0));
        assertEquals(song2, playlist1FromDB.getSongs().get(1));


    }

    @Test
    public void removeSongFromPlaylist() throws Exception {
        List<Song> songList = new ArrayList<>();
        Song song1  = new Song("Song 1");
        Song song2  = new Song("Song 2");
        songList.add(song1);
        songList.add(song2);
        Playlist playlist = new Playlist("playlist1", songList);
        Playlist playlistRepo = playlistRepository.save(playlist);

        SongDto songDto = new SongDto("Song 2");

        mockMvc.perform(delete("/playlist/"+playlistRepo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(songDto)))
                .andExpect(status().isOk())
                .andDo(document("removesongfromplaylist"));

        Playlist playlist1FromDB = playlistRepository.findByName("playlist1");
        assertEquals(1, playlist1FromDB.getSongs().size());
        assertEquals(List.of(song1),playlist1FromDB.getSongs());

    }

}
