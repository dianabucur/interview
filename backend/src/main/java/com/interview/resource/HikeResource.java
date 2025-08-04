package com.interview.resource;

import com.interview.business.HikeService;
import com.interview.business.dto.HikeDto;
import com.interview.business.dto.HikeListingDto;
import com.interview.business.dto.UpsertHikeDto;
import com.interview.business.exception.ErrorResponse;
import com.interview.config.resolver.CurrentUser;
import com.interview.data.entity.User;
import com.interview.data.enumeration.TrailDifficulty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api/hikes", produces = MediaType.APPLICATION_JSON_VALUE)
public class HikeResource {

    private final HikeService hikeService;

    @Operation(summary = "Get all public hikes, paginated",
            description = "The hikes must be flagged as public by users in order to be retrieved by other users." +
                    "The result can be filtered by trail difficulty levels and searchText filters by trail name and location." +
                    "The sortBy parameter needs to have the structure: 'fieldName,ASC/DESC'. " +
                    "e.g. 'modifiedOn,ASC' sorts asceding by the last edited field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK, hikes returned",
                    content = @Content(schema = @Schema(implementation = HikeListingDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request, invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    ResponseEntity<Page<HikeListingDto>> getAllHikes(@Parameter(description = "Free-text search applied to trail name and location")
                                                     @RequestParam(required = false) String searchText,
                                                     @RequestParam(required = false) Set<TrailDifficulty> difficulties,
                                                     @Parameter(
                                                             description = "Sort format: 'fieldName,ASC/DESC'. " +
                                                                     "Supported fields include: 'date', 'durationHours', 'modifiedOn'. " +
                                                                     "Defaults to no sorting if not provided.",
                                                             example = "date,DESC"
                                                     )
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hikeService.getAllPublicHikes(difficulties, searchText, sortBy, page, size));
    }

    @Operation(summary = "Get hike by id, together with extensive trail information",
            description = "The hike must belong to the authenticated user. " +
                    "The trail information is fetched both from the database, as well as from a third party application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK, hike returned",
                    content = @Content(schema = @Schema(implementation = HikeListingDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not found, hike not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{id}")
    ResponseEntity<HikeDto> getHikeById(@PathVariable("id") Long id, @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseEntity.ok(hikeService.getHike(id, user));
    }

    @Operation(summary = "Create a new hike",
            description = "Creates a new hike entry for the authenticated user. Requires an existing trailId and date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "OK, hike created",
                    content = @Content(schema = @Schema(implementation = HikeListingDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not found, trail not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    ResponseEntity<HikeDto> createHike(@Valid @RequestBody UpsertHikeDto upsertHikeDto,
                                       @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hikeService.createHike(upsertHikeDto, user));
    }

    @Operation(summary = "Updates an existing hike",
            description = "Updates an existing hike entry belonging to the authenticated user. Requires an existing trailId and date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK, hike updated",
                    content = @Content(schema = @Schema(implementation = HikeListingDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not found, hike or trail not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping("/{hikeId}")
    ResponseEntity<HikeDto> updateHike(@PathVariable Long hikeId, @Valid @RequestBody UpsertHikeDto hikeDto,
                                       @Parameter(hidden = true) @CurrentUser User user) {
        return ResponseEntity.ok(hikeService.updateHike(hikeId, hikeDto, user));
    }

    @Operation(summary = "Delete an existing hike entry",
            description = "The hike that is deleted must belong to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "OK, hike deleted",
                    content = @Content(schema = @Schema(implementation = HikeListingDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized, invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "Not found, hike not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteHike(@PathVariable Long id, @Parameter(hidden = true) @CurrentUser User user) {
        hikeService.deleteHike(id, user);
        return ResponseEntity.ok().build();
    }
}
