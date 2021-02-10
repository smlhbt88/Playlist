package com.playlist.controllers;

import com.playlist.models.Playlist;
import com.playlist.models.PlaylistDto;
import com.playlist.models.SongDto;
import com.playlist.services.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class PlaylistController {

    PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("playlist")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createPlaylist(@RequestBody PlaylistDto playlistDto) {
       return playlistService.createPlaylist(playlistDto);
    }

    @PatchMapping("playlist/{playlistName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSongToPlayList(@RequestBody SongDto songDto, @PathVariable String playlistName){
        playlistService.addSongToPlayList(songDto, playlistName);
    }

    @DeleteMapping("playlist/{id}")
    public void deleteSongFromPlaylist(@RequestBody SongDto songDto, @PathVariable Long id) {
        playlistService.deleteSongFromPlaylist(songDto, id);
    }
}
