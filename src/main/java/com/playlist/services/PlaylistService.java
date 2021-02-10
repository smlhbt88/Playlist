package com.playlist.services;

import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.repositories.PlaylistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    PlaylistRepository playlistRepository;

    ModelMapper modelMapper = new ModelMapper();

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public String createPlaylist(PlaylistDto playlistDto) {
        Playlist playlist = modelMapper.map(playlistDto,Playlist.class);

        Playlist result = playlistRepository.save(playlist);
        if(result!=null) {
            return "successful";
        }
        return null;
    }
}
