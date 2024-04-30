package com.example.simplecontrollertesttask.controller;


import com.example.simplecontrollertesttask.dto.ResponseDto;
import com.example.simplecontrollertesttask.service.ResultService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@Validated
@RequestMapping(path = "/result")
public class ResultController {

    private final ResultService resultService;

    @PostMapping(path = "/{operationId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseDto getResult(
            @PathVariable @NotNull @Positive Long operationId,
            @RequestBody @NotEmpty MultiValueMap<String, String> map
    ) {
        log.info("Processing the request with operationId={}", operationId);
        return resultService.getResponse(map);
    }
}
