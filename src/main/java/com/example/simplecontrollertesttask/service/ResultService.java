package com.example.simplecontrollertesttask.service;

import com.example.simplecontrollertesttask.dto.ResponseDto;
import com.example.simplecontrollertesttask.dto.ResultDto;
import com.example.simplecontrollertesttask.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResultService {
    private final HmacUtils hmacUtils;

    @Autowired
    public ResultService(HmacUtils hmacUtils) {
        this.hmacUtils = hmacUtils;
    }

    public ResponseDto getResponse(MultiValueMap<String, String> map) {
        String sortedString = map.entrySet().stream()
                .filter(e -> e.getKey() != null && !e.getKey().isBlank())
                .sorted(Map.Entry.comparingByKey())
                .flatMap(e -> e.getValue().stream().map(s -> Map.entry(e.getKey(), s)))
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue())) // TODO if UrlEncoding is not necessary, use .map(e -> e.getKey() + "=" + e.getValue()) instead
                .collect(Collectors.joining("&"));
        log.debug("Original string: {}", sortedString);

        String hash = hmacUtils.hmacHex(sortedString);
        log.debug("Hash: {}", hash);

        return new ResponseDto(Status.SUCCESS.name().toLowerCase(Locale.ROOT), List.of(new ResultDto(hash)));
    }

    private String encode(String original) {
        return URLEncoder.encode(original, StandardCharsets.UTF_8);
    }
}
