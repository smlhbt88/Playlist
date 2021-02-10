package com.playlist.ServiceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.models.Song;
import com.playlist.models.SongDto;
import com.playlist.repositories.PlaylistRepository;
import com.playlist.services.PlaylistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void createPlaylistWithoutName() {

        PlaylistDto playlistDto = new PlaylistDto();

        String response = playlistService.createPlaylist(playlistDto).getBody();

        assertEquals("name is required",response);

    }

    @Test
    public void addSongToPlayList(){
        SongDto songdto  = new SongDto("Song 2");
        Song songEntity1  = new Song("Song 1");
        Song songEntity2  = new Song("Song 2");
        List<Song> songList = new ArrayList<>();
        songList.add(songEntity1);
        Playlist playlistEntity = new Playlist("playlist1", songList);
        List<Song> songList2 = new ArrayList<>();
        songList2.add(songEntity1);
        songList2.add(songEntity2);
        Playlist playlistEntity2 = new Playlist("playlist1", songList2 );

        when(playlistRepository.findByName("playlist1")).thenReturn(playlistEntity);
        when(playlistRepository.save(playlistEntity)).thenReturn(playlistEntity2);

       playlistService.addSongToPlayList(songdto, "playlist1");
        verify(playlistRepository, times(1)).save(playlistEntity);
    }

}
