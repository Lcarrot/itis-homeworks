package com.example.controller;

import com.example.form.SourceData;
import com.example.form.SourceDataWithArbiter;
import com.example.service.BlockService;
import com.example.service.BlockWithArbiterService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import javax.validation.Valid;
import java.util.List;

@Controller("/arbiter")
public class PuckControllerWithArbiter {

    @Inject
    private BlockWithArbiterService blockService;

    @Post("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<Long> createBlocks(@Valid @Body SourceDataWithArbiter sourceData) {
        return HttpResponse.created(blockService.createLinkedBlocks(sourceData));
    }

    @Get("/get/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    HttpResponse<List<String>> getBlocks(@PathVariable("id") Long id) {
        return HttpResponse.ok(blockService.getDataByLinkedBlocksId(id));
    }
}
