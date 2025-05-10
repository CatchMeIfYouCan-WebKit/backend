package com.team.webkit.backend.api.missing.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class WitnessRequest {

    public String postType;
    public List<String> photoUrls;
    public LocalDateTime witnessDatetime;
    public String witnessLocation;
    public String detailDescription;
}