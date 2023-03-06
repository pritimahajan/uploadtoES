package com.javatab.springelasticdemo.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Text implements Serializable {
    private String textAuto;
    private String text;
    private Routing video_join;

    public Text(String text, String parent) {
        this.text = text;
        this.textAuto = text;
        this.video_join = new Routing(parent);
    }
}
