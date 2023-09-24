package com.aurrix.slsample.sml.models;

import com.aurrix.slsample.sml.enums.ResponseFragmentType;

public record StateResponse<T> (
        ResponseFragmentType type,
        String key,
        T data
) {
}
