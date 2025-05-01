package com.team.webkit.backend.api.missing;

import com.team.webkit.backend.support.annotation.MSP;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MSP
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MissingController {

    private final MissingService missingService;

}
