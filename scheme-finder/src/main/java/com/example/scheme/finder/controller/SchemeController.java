package com.example.scheme.finder.controller;

import com.example.scheme.finder.dto.ApiResponse;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.dto.SchemeDto;
import com.example.scheme.finder.security.UserPrincipal;
import com.example.scheme.finder.service.SchemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schemes")
@RequiredArgsConstructor
@Tag(name = "Schemes", description = "Government Scheme APIs")
public class SchemeController {

    private final SchemeService schemeService;

    @GetMapping
    @Operation(summary = "Get all active schemes with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> getAllSchemes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;

        return ResponseEntity.ok(ApiResponse.success(schemeService.getAllSchemes(pageable, userId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get scheme details by ID")
    public ResponseEntity<ApiResponse<SchemeDto.SchemeResponse>> getSchemeById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.getSchemeById(id, userId)));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured schemes")
    public ResponseEntity<ApiResponse<List<SchemeDto.SchemeSummary>>> getFeaturedSchemes(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.getFeaturedSchemes(userId)));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get schemes by category")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> getSchemesByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.getSchemesByCategory(categoryId, pageable, userId)));
    }

    @GetMapping("/state/{stateName}")
    @Operation(summary = "Get schemes by state")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> getSchemesByState(
            @PathVariable String stateName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.getSchemesByState(stateName, pageable, userId)));
    }

//    @GetMapping("/search")
//    @Operation(summary = "Search schemes by keyword")
//    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> searchSchemes(
//            @RequestParam String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "12") int size,
//            @AuthenticationPrincipal UserPrincipal userPrincipal) {
//        Pageable pageable = PageRequest.of(page, size);
//        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
//        return ResponseEntity.ok(ApiResponse.success(schemeService.searchSchemes(keyword, pageable, userId)));
//    }

    @GetMapping("/search")
    @Operation(summary = "Search schemes by keyword")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> searchSchemes(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        // If keyword is empty, return all schemes instead
        if (keyword.trim().isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Long userId = userPrincipal != null ? userPrincipal.getId() : null;
            return ResponseEntity.ok(ApiResponse.success(schemeService.getAllSchemes(pageable, userId)));
        }

        Pageable pageable = PageRequest.of(page, size);
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.searchSchemes(keyword, pageable, userId)));
    }

    @PostMapping("/eligible")
    @Operation(summary = "Get eligible schemes based on user profile")
    public ResponseEntity<ApiResponse<PagedResponse<SchemeDto.SchemeSummary>>> getEligibleSchemes(
            @RequestBody SchemeDto.EligibilityFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Pageable pageable = PageRequest.of(page, size);
        Long userId = userPrincipal != null ? userPrincipal.getId() : null;
        return ResponseEntity.ok(ApiResponse.success(schemeService.getEligibleSchemes(filter, pageable, userId)));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get platform statistics")
    public ResponseEntity<ApiResponse<SchemeDto.SchemeStats>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(schemeService.getStats()));
    }

    // Admin only endpoints
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Create a new scheme (Admin only)")
    public ResponseEntity<ApiResponse<SchemeDto.SchemeResponse>> createScheme(
            @Valid @RequestBody SchemeDto.CreateSchemeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Scheme created successfully", schemeService.createScheme(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update a scheme (Admin only)")
    public ResponseEntity<ApiResponse<SchemeDto.SchemeResponse>> updateScheme(
            @PathVariable Long id,
            @Valid @RequestBody SchemeDto.CreateSchemeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Scheme updated successfully", schemeService.updateScheme(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Delete a scheme (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteScheme(@PathVariable Long id) {
        schemeService.deleteScheme(id);
        return ResponseEntity.ok(ApiResponse.success("Scheme deleted successfully", null));
    }
}
