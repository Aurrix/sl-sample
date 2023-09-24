package com.aurrix.slsample.sml.models;

import com.aurrix.slsample.sml.enums.RequestFragmentType;

public record StateRequest<T>(
        RequestFragmentType type,
        String key,
        T data
) {
}
