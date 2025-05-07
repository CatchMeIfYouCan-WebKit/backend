package com.team.webkit.backend.api.map.DTO;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MapPostResponse {

    private String postType;          // missing / witness
    private String missingLocation;
    private String photoUrl;
    private LocalDateTime missingDatetime;
    private String breed;             // pet에서
    private String coatColor;         // pet에서
}
