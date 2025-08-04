package com.interview.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.interview.business.client.TrailInfoResponse;
import com.interview.business.dto.HikeDto;
import com.interview.business.dto.HikeListingDto;
import com.interview.business.dto.UpsertHikeDto;
import com.interview.business.exception.ErrorResponse;
import com.interview.data.entity.Hike;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import utility.PageResponse;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.interview.data.enumeration.TrailDifficulty.ADVANCED;
import static com.interview.data.enumeration.TrailDifficulty.EXPERT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@AutoConfigureWireMock(port = 0)
@Transactional
@ActiveProfiles("test")
class HikeResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestEntityManager entityManager;

    private final ObjectMapper objectMapper;
    private static final String USERNAME = "diana";
    private static final String PASSWORD = "dianapassword";
    private static final String TRAIL_LOCATION = "Arkansas, USA";

    {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void getAllHikes() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/hikes")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andReturn();

        List<HikeListingDto> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<PageResponse<HikeListingDto>>()
                {
                }).getContent();

        assertEquals(6, result.size());
        assertEquals(1L, result.getFirst().id());
        assertEquals("Diana Bucur-Dranca", result.getFirst().userDisplayName());
        assertEquals(LocalDate.of(2024, 6, 15), result.getFirst().date());
        assertEquals(5.5,  result.getFirst().durationHours());
        assertEquals(TRAIL_LOCATION, result.getFirst().trailLocation());
        assertEquals(42.0, result.getFirst().distanceKm());
        assertEquals(ADVANCED, result.getFirst().difficulty());
    }

    @Test
    void getAllHikes_badCredentials() throws Exception {
        mockMvc.perform(
                        get("/api/hikes")
                                .with(httpBasic("test", "test"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void getAllHikes_filteredAndSorted() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/hikes")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .param("sortBy", "date,ASC")
                                .param("searchText", "usa")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andReturn();

        List<HikeListingDto> result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<PageResponse<HikeListingDto>>()
                {
                }).getContent();

        assertEquals(3, result.size());
        assertEquals(10L, result.getLast().id());
        assertEquals("David Stan", result.getLast().userDisplayName());
        assertEquals(LocalDate.of(2024, 7, 28), result.getLast().date());
        assertEquals(8.0,  result.getLast().durationHours());
        assertEquals("Kalalau Trail", result.getLast().trailName());
        assertEquals(35.0, result.getLast().distanceKm());
        assertEquals(EXPERT, result.getLast().difficulty());
    }

    @Test
    void getHikeById() throws Exception {
        mockGetTrailAdditionalInfo();

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/hikes/1")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andReturn();

        HikeDto result = mapToType(mvcResult, HikeDto.class);
        assertEquals(1L, result.id());
        assertEquals(LocalDate.of(2024, 6, 15), result.date());
        assertEquals(5.5,  result.durationHours());
        assertEquals("Sunny",  result.weather());
        assertEquals(TRAIL_LOCATION, result.trail().location());
        assertEquals(42.0, result.trail().distanceKm());
        assertEquals(ADVANCED, result.trail().difficulty());
        assertEquals("35.2048883", result.trail().latitude());
        assertEquals(8, result.trail().placeRank());
    }

    @Test
    void getHikeById_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/hikes/11")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isNotFound())
                .andReturn();

        ErrorResponse result = mapToType(mvcResult, ErrorResponse.class);
        assertEquals("Hike with id 11 not found", result.getMessages().getFirst());
    }

    @Test
    void createHike() throws Exception {
        UpsertHikeDto hikeDto = createUpsertHikeDto(1L);

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/hikes")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(hikeDto)))
                .andExpectAll(status().isCreated())
                .andReturn();

        HikeDto result = mapToType(mvcResult, HikeDto.class);
        assertNotNull(result.id());
        assertEquals(hikeDto.date(), result.date());
        assertEquals(hikeDto.durationHours(),  result.durationHours());
        assertEquals(hikeDto.weather(),  result.weather());
        assertEquals(TRAIL_LOCATION, result.trail().location());
        assertEquals(42.0, result.trail().distanceKm());
        assertEquals(ADVANCED, result.trail().difficulty());
    }

    @Test
    void updateHike() throws Exception {
        mockGetTrailAdditionalInfo();
        UpsertHikeDto hikeDto = createUpsertHikeDto(1L);

        MvcResult mvcResult = mockMvc.perform(
                        put("/api/hikes/1")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(hikeDto)))
                .andExpectAll(status().isOk())
                .andReturn();

        HikeDto result = mapToType(mvcResult, HikeDto.class);
        assertNotNull(result.id());
        assertEquals(hikeDto.date(), result.date());
        assertEquals(hikeDto.durationHours(),  result.durationHours());
        assertEquals(hikeDto.weather(),  result.weather());
        assertEquals(TRAIL_LOCATION, result.trail().location());
        assertEquals(42.0, result.trail().distanceKm());
        assertEquals(ADVANCED, result.trail().difficulty());
        assertEquals(42.0, result.trail().distanceKm());
        assertEquals(ADVANCED, result.trail().difficulty());
    }

    @Test
    void updateHike_badRequest() throws Exception {
        UpsertHikeDto hikeDto = createUpsertHikeDto(null);

        MvcResult mvcResult = mockMvc.perform(
                        put("/api/hikes/1")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(hikeDto)))
                .andExpectAll(status().isBadRequest())
                .andReturn();

        ErrorResponse result = mapToType(mvcResult, ErrorResponse.class);
        assertEquals("Trail is required", result.getMessages().getFirst());
    }

    @Test
    void deleteHike() throws Exception {
        mockMvc.perform(
                        delete("/api/hikes/1")
                                .with(httpBasic(USERNAME, PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(status().isOk())
                .andReturn();

        assertNull(entityManager.find(Hike.class, 1L));
    }

    private void mockGetTrailAdditionalInfo() throws Exception
    {
        WireMock.stubFor(WireMock
                .get(WireMock.urlPathEqualTo("/search"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper
                                .writeValueAsString(new TrailInfoResponse("44.8446069", "15.4806998", 8)))));
    }

    private <T> T mapToType(MvcResult mvcResult, Class<T> clazz)
            throws JsonProcessingException, UnsupportedEncodingException
    {
        return objectMapper.readValue(mvcResult.getResponse().getContentAsString(), clazz);
    }

    private UpsertHikeDto createUpsertHikeDto(Long traildId) {
        return new UpsertHikeDto(LocalDate.of(2025, 8, 3), 2.5, "Sunny",
                "Lovely escape in nature", false, traildId);
    }
}