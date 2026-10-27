package quickbite.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record UbahAlamatRequest(@NotBlank String alamat) { }