package com.team.webkit.backend.api.missing.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WitnessRequest {

    public String postType;
    public String photoUrl;
    public LocalDateTime witnessDatetime;
    public String witnessLocation;
    public String detailDescription;
}