package com.example.controller;

import com.example.form.SourceData;
import com.example.service.BlockService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.util.List;

@Controller("/simple")
public class PuckController {

    @Inject
    private BlockService blockService;

    @Get("/get/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<List<String>> getBlocks(@PathVariable("id") Long id, String key) {
        return HttpResponse.ok(blockService.getDataByLinkedBlocksId(id, key));
    }

    @Post("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<Long> createBlocks(@Valid @Body SourceData sourceData) {
        return HttpResponse.created(blockService.createLinkedBlocks(sourceData));
    }
}
