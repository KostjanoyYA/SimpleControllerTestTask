package com.example.simplecontrollertesttask.dto;

import java.util.List;

public record ResponseDto(
        String status,
        List<ResultDto> result
) {}

