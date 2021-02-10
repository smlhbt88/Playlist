package com.playlist.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Song> songs;

    public Playlist() {}

    public Playlist(String name, List<Song> songs) {
        this.name = name;
        this.songs = songs;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return name.equals(playlist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
