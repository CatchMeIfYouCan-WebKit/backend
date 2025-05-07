package com.team.webkit.backend.api.missing.dto;

import java.time.LocalDateTime;

public class MissingRequest {

    public Integer petId;
    public String postType;
    public String photoUrl;
    public LocalDateTime missingDatetime;
    public String missingLocation;
    public String detailDescription;
}
