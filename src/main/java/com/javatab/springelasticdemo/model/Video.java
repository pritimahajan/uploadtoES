package com.javatab.springelasticdemo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Video implements Serializable {
    public String domain;
    public String source;
    public String uri;
    public String id;
    public String title;
    public String video_join = "video";
    
    public Video(Meta meta) {
        this.domain = meta.getDomain();
        this.source = meta.getSource();
        this.uri = meta.getUri();
        this.id = meta.getId();
        this.title = meta.getTitle();
    }
}
