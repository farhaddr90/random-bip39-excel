package com.dorri.model;

public record Result(
        Data data,
        boolean success,
        String message
) {
    public record Data(
            String fileInfo
    ) {
    }
}
