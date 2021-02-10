package com.playlist.ServiceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.repositories.PlaylistRepository;
import com.playlist.services.PlaylistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private PlaylistService playlistService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createPlaylist() {

        Playlist playlist = new Playlist("playlist1",null);
        PlaylistDto playlistDto = new PlaylistDto("playlist1", null);

        when(playlistRepository.findByName(playlistDto.getName())).thenReturn(null);
        when(playlistRepository.save(playlist)).thenReturn(playlist);

         String response = playlistService.createPlaylist(playlistDto).getBody();

        verify(playlistRepository,times(1)).save(playlist);

        assertEquals("successful",response);

    }

    @Test
    public void createPlaylistWithExistingName() {

        Playlist playlist = new Playlist("playlist1",null);
        PlaylistDto playlistDto = new PlaylistDto("playlist1", null);

        when(playlistRepository.findByName(playlistDto.getName())).thenReturn(playlist);

        String response = playlistService.createPlaylist(playlistDto).getBody();

        verify(playlistRepository,times(1)).findByName(playlistDto.getName());

        assertEquals("unsuccessful",response);

    }

}
