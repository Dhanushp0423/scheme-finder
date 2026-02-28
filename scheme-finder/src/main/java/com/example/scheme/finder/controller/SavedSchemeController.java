package com.example.scheme.finder.controller;

import com.example.scheme.finder.dto.ApiResponse;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import com.example.scheme.finder.security.UserPrincipal;
import com.example.scheme.finder.service.SavedSchemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saved-schemes")
@RequiredArgsConstructor
@Tag(name = "Saved Schemes", description = "Bookmarked Schemes APIs")
public class SavedSchemeController {

    private final SavedSchemeService savedSchemeService;

    @PostMapping("/{schemeId}")
    @Operation(summary = "Save/bookmark a scheme")
    public ResponseEntity<ApiResponse<Void>> saveScheme(
            @PathVariable Long schemeId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        savedSchemeService.saveScheme(userPrincipal.getId(), schemeId);
        return ResponseEntity.ok(ApiResponse.success("Scheme saved", null));
    }

    @DeleteMapping("/{schemeId}")
    @Operation(summary = "Remove saved scheme")
    public ResponseEntity<ApiResponse<Void>> unsaveScheme(
            @PathVariable Long schemeId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        savedSchemeService.unsaveScheme(userPrincipal.getId(), schemeId);
        return ResponseEntity.ok(ApiResponse.success("Scheme removed from saved", null));
    }

    @GetMapping
    @Operation(summary = "Get all saved schemes for current user")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> getSavedSchemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(ApiResponse.success(
                savedSchemeService.getSavedSchemes(userPrincipal.getId(), PageRequest.of(page, size))));
    }
}
