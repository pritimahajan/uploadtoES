package com.javatab.springelasticdemo.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatab.springelasticdemo.model.DataIndexResponse;
import com.javatab.springelasticdemo.model.Meta;
import com.javatab.springelasticdemo.model.Text;
import com.javatab.springelasticdemo.model.Video;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIService
{

    public static final String INDEX = "video_index";
    private final ObjectMapper objectMapper;
    private final RestHighLevelClient client;
    @Value("${directory}")
    private String directory;

    private List<String> listOfFiles() throws URISyntaxException, IOException
    {
        Path pathBase = Paths.get(ClassLoader.getSystemResource("data").toURI());

        Files.walk(pathBase)
                .filter(Files::isRegularFile)
                .forEach(f -> System.out.println(pathBase.relativize(Paths.get(f.toUri()))));

        List<String> files = new ArrayList<String>();

        File filesList[] = pathBase.toFile().listFiles();
        if (filesList.length == 0)
        {
            return files;
        }

        for (File file : filesList)
        {
            if (file.getAbsolutePath().endsWith(".json"))
            {
                files.add(file.getAbsolutePath());
            }
        }

        return files;
    }

    public Meta convertToJson() throws URISyntaxException, IOException
    {

        Meta meta = new Meta();
        Path pathBase = Paths.get(ClassLoader.getSystemResource("txtFiles").toURI());
        Files.walk(pathBase)
                .filter(Files::isRegularFile)
                .forEach(f -> System.out.println(pathBase.relativize(Paths.get(f.toUri()))));

        List<String> files = new ArrayList<String>();

        for (File file : pathBase.toFile().listFiles())
        {
            if (file.getAbsolutePath().endsWith(".txt"))
            {
                files.add(file.getAbsolutePath());
            }
        }

        try
        {
            for (String file : files)
            {
                Map<String, String> data = new HashMap<String, String>();
                ;
                meta.setDomain("blah");
                meta.setId("blah");
                meta.setSource("blah");
                meta.setUri("blah");
                meta.setId("blah");
                meta.setTitle("blah");


                FileReader fr = new FileReader(file);
                BufferedReader reader;
                String line;
                int i = 0;
                String previousKey = null;
                try
                {
                    reader = new BufferedReader(fr);

                    while ((line = reader.readLine()) != null)
                    {
                        if (i % 2 == 0)
                            previousKey = line.toUpperCase(Locale.ROOT);
                        else
                            data.put(previousKey, line);
                        i++;
                    }

                    reader.close();
                } catch (Exception e)
                {
                    System.out.println("exception converting file  ===> " + e.getMessage());
                    e.printStackTrace();
                }
                meta.setData(data);
            }
        } catch (Exception e)
        {
            System.out.println("exception converting file  ===> " + e.getMessage());
        }
        return meta;

    }

    public DataIndexResponse upload() throws JsonParseException, JsonMappingException, IOException, URISyntaxException
    {

        ObjectMapper mapper = new ObjectMapper();
        List<String> files = this.listOfFiles();
        if (files.size() == 0)
        {
            return new DataIndexResponse("No json files are available");
        }

        try
        {
            for (String file : files)
            {
                Meta meta = mapper.readValue(new File(file), Meta.class);
                Video video = new Video(meta);

                IndexRequest request = new IndexRequest(INDEX);
                request.id(video.getId());
                request.source(objectMapper.writeValueAsString(video), XContentType.JSON);

                client.index(request, RequestOptions.DEFAULT);

                List<String> list = meta.getData().entrySet().stream()
                        .map((entry) -> entry.getKey() + "==" + entry.getValue())
                        .collect(Collectors.toList());

                BulkRequest bulkRequest = new BulkRequest();

                for (String data : list)
                {
                    Text txt = new Text(data, video.getId());
                    IndexRequest indexRequest = new IndexRequest(INDEX);
                    indexRequest.routing(video.getId());
                    indexRequest.source(objectMapper.writeValueAsString(txt), XContentType.JSON);
                    bulkRequest.add(indexRequest);
                }

                client.bulk(bulkRequest, RequestOptions.DEFAULT);
            }
        } catch (Exception e)
        {
            return new DataIndexResponse("Error while indexing: " + e.getMessage());
        }

        return new DataIndexResponse("Successfully indexed data");
    }


}
