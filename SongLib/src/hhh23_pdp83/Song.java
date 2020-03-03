//Author: Hemang Hirpara
//Author: Poojan Patel
package hhh23_pdp83;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Comparator;

public class Song implements Comparable<Song> {
    private final SimpleStringProperty SongTitle = new SimpleStringProperty("");
    private final SimpleStringProperty SongArtist = new SimpleStringProperty("");
    private final SimpleStringProperty SongYear = new SimpleStringProperty("");
    private final SimpleStringProperty SongAlbum = new SimpleStringProperty("");

    public Song() {
        this("", "");
    }

    public Song(String title, String artist) {
        setSongTitle(title);
        setSongArtist(artist);
    }

    public Song(String title, String artist, String album, String year) {
        setSongTitle(title);
        setSongArtist(artist);
        setSongYear(year);
        setSongAlbum(album);
    }
    public StringProperty songTitleProperty() { return SongTitle; }
    public StringProperty songArtistProperty() { return SongArtist; }
    public String getSongTitle() {
        return SongTitle.get();
    }

    public void setSongTitle(String title) {
        SongTitle.set(title);
    }

    public String getSongArtist() {
        return SongArtist.get();
    }

    public void setSongArtist(String artist) {
        SongArtist.set(artist);
    }

    public String getSongYear() {
        return SongYear.get();
    }

    public void setSongYear(String year) {
        SongYear.set(year);
    }

    public String getSongAlbum() {
        return SongAlbum.get();
    }

    public void setSongAlbum(String album) {
        SongAlbum.set(album);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Song s = (Song) o;
        return this.SongTitle.get().toLowerCase().equals(s.getSongTitle().toLowerCase()) &&
                this.SongArtist.get().toLowerCase().equals(s.getSongArtist().toLowerCase());
    }


    @Override
    public String toString()
    {
        return this.getSongTitle() + " by " + this.getSongArtist();
    }

    @Override
    public int compareTo(Song o) {
        int compare = getSongTitle().toLowerCase().compareTo(o.getSongTitle().toLowerCase());
        if(compare == 0)
        {
            compare = getSongArtist().toLowerCase().compareTo(o.getSongArtist().toLowerCase());
        }
        return compare;
    }
}
