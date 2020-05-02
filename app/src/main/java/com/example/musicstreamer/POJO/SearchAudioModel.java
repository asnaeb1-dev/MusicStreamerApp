package com.example.musicstreamer.POJO;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchAudioModel {

    @SerializedName("track")
    @Expose
    private Track track;

    public Track getTrack() {
        return track;
    }

        public class Track {

            @SerializedName("name")
            @Expose
            private String name;

            @SerializedName("artist")
            @Expose
            private Artist artist;
            @SerializedName("album")
            @Expose
            private Album album;
            @SerializedName("toptags")
            @Expose
            private Toptags toptags;
            @SerializedName("wiki")
            @Expose
            private Wiki wiki;

            public String getName() {
                return name;
            }

            public Artist getArtist() {
                return artist;
            }

            public Album getAlbum() {
                return album;
            }

            public Toptags getToptags() {
                return toptags;
            }

            public Wiki getWiki() {
                return wiki;
            }
        }

    public class Album {

        @SerializedName("artist")
        @Expose
        private String artist;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private List<Image> image = null;

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public List<Image> getImage() {
            return image;
        }

        public class Image {

            @SerializedName("#text")
            @Expose
            private String text;

            public String getText() {
                return text;
            }

        }

    }

    public class Artist {

        @SerializedName("name")
        @Expose
        private String name;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



    public class Tag {

        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

    }

    public class Toptags {

        @SerializedName("tag")
        @Expose
        private List<Tag> tag = null;

        public List<Tag> getTag() {
            return tag;
        }

    }

    public class Wiki {

        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("content")
        @Expose
        private String content;


        public String getSummary() {
            return summary;
        }

        public String getContent() {
            return content;
        }
    }
}