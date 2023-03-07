package com.javatab.springelasticdemo.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.javatab.springelasticdemo.model.DataIndexResponse;
import com.javatab.springelasticdemo.model.Meta;
import com.javatab.springelasticdemo.service.APIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api")
public class APIController
{

    private final APIService movieService;


    @GetMapping(value = "/upload")
    public DataIndexResponse uploadDocument() throws JsonParseException, JsonMappingException, IOException, URISyntaxException
    {
        return movieService.upload();
    }

    @GetMapping(value = "/convert")
    public Meta convertDocument() throws JsonParseException, JsonMappingException, IOException, URISyntaxException
    {
        return movieService.convertToJson();
    }

}
