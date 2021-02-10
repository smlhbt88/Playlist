package com.playlist.services;

import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.repositories.PlaylistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    PlaylistRepository playlistRepository;

    ModelMapper modelMapper = new ModelMapper();

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public ResponseEntity<String> createPlaylist(PlaylistDto playlistDto) {
        if(playlistDto.getName() == null) {
            return new ResponseEntity<>("name is required", HttpStatus.BAD_REQUEST);
        }
        Playlist existingPlaylist = playlistRepository.findByName(playlistDto.getName());
        if(existingPlaylist != null)
        {
            return new ResponseEntity<>("unsuccessful", HttpStatus.BAD_REQUEST);
        }

            Playlist playlist = modelMapper.map(playlistDto, Playlist.class);
            Playlist result = playlistRepository.save(playlist);
            if (result != null) {
                return new ResponseEntity<>("successful", HttpStatus.CREATED);
            }

        return new ResponseEntity<>("unsuccessful", HttpStatus.BAD_REQUEST);

    }
}
