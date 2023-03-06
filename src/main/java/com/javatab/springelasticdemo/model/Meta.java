package com.javatab.springelasticdemo.model;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Meta implements Serializable {
    private String domain;
    private String source;
    private String uri;
    private String id;
    private String title;
    private Map<String, String> data;
}
