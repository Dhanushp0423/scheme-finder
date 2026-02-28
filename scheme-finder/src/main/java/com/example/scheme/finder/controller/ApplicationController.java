package com.example.scheme.finder.controller;

import com.example.scheme.finder.dto.ApiResponse;
import com.example.scheme.finder.dto.ApplicationDto;
import com.example.scheme.finder.dto.PagedResponse;
import com.example.scheme.finder.security.UserPrincipal;
import com.example.scheme.finder.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Scheme Application APIs")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Apply for a scheme")
    public ResponseEntity<ApiResponse<ApplicationDto.ApplicationResponse>> applyForScheme(
            @RequestBody ApplicationDto.CreateApplicationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted successfully",
                        applicationService.applyForScheme(userPrincipal.getId(), request)));
    }

    @GetMapping("/my")
    @Operation(summary = "Get current user's applications")
    public ResponseEntity<ApiResponse<PagedResponse<ApplicationDto.ApplicationResponse>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(ApiResponse.success(
                applicationService.getUserApplications(userPrincipal.getId(), PageRequest.of(page, size))));
    }

    @PutMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw an application")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        applicationService.withdrawApplication(userPrincipal.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn", null));
    }

    // Admin endpoints
    @GetMapping("/scheme/{schemeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get all applications for a scheme (Admin only)")
    public ResponseEntity<ApiResponse<PagedResponse<ApplicationDto.ApplicationResponse>>> getSchemeApplications(
            @PathVariable Long schemeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                applicationService.getSchemeApplications(schemeId, PageRequest.of(page, size))));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @Operation(summary = "Update application status (Admin only)")
    public ResponseEntity<ApiResponse<ApplicationDto.ApplicationResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody ApplicationDto.UpdateApplicationStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Status updated",
                applicationService.updateApplicationStatus(id, request)));
    }
}