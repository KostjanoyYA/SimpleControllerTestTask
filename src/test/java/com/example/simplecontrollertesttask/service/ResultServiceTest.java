package com.example.simplecontrollertesttask.service;

import com.example.simplecontrollertesttask.dto.ResponseDto;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResultServiceTest {

    @Mock
    private HmacUtils hmacUtils;

    @InjectMocks
    private ResultService resultService;

    private AutoCloseable closeMe;

    @BeforeEach
    void setUp() {
        closeMe = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeMe.close();
    }

    @Test
    void getResponse_whenCalled_returnsProperResponseDto() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("bParam", "valueB");
        map.add("aParam", "valueA");

        String expectedString = "aParam=valueA&bParam=valueB";
        String hashed = "hashed_" + expectedString;
        when(hmacUtils.hmacHex(expectedString)).thenReturn(hashed);

        ResponseDto response = resultService.getResponse(map);

        assertEquals("success", response.status());
        assertNotNull(response.result());
        assertEquals(1, response.result().size());
        assertEquals(hashed, response.result().get(0).signature());
        verify(hmacUtils).hmacHex(expectedString);
    }
}
