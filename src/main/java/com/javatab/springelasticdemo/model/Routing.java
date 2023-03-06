package com.javatab.springelasticdemo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Routing implements Serializable {
    private String name = "description";
    private String parent;

    public Routing(String parent) {
        this.parent = parent;
    }
}
