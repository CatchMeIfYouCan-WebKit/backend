package com.team.webkit.backend.api.adopt.dto;

import com.team.webkit.backend.api.member.entity.Member;
import lombok.Data;

@Data
public class MemberSimpleDto {
    private Integer id;
    private String nickname;
    private String phone;

    public static MemberSimpleDto from(Member member) {
        MemberSimpleDto dto = new MemberSimpleDto();
        dto.setId(member.getId());
        dto.setNickname(member.getNickname());
        dto.setPhone(member.getPhone());
        return dto;
    }
}
