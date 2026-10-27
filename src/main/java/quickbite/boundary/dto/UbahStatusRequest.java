package quickbite.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record UbahStatusRequest(@NotBlank String status) { }