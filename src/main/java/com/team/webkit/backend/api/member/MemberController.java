package com.team.webkit.backend.api.member;

import com.team.webkit.backend.support.MspUtil;
import com.team.webkit.backend.support.annotation.MSP;
import com.team.webkit.backend.support.protocol.MspResult;
import com.team.webkit.backend.support.protocol.MspStatus;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@MSP
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<MspResult> join(@RequestBody Member member) {
        MspResult result;

        Map<String, String> body = new LinkedHashMap<>();

        if (member.getLoginId().isEmpty() || member.getPassword().isEmpty() || member.getPhone()
            .isEmpty() || member.getNickname().isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else if (!isValidLoginId(member.getLoginId())) {
            body.put("rsltCode", "2001");
            body.put("rsltMsg", "아이디 형식이 올바르지 않습니다.");
        } else if (!isValidPassword(member.getPassword())) {
            body.put("rsltCode", "2001");
            body.put("rsltMsg", "비밀번호 형식이 올바르지 않습니다.");
        } else {
            String resultCode = memberService.join(member);

            if (resultCode.equals("0000")) {
                body.put("rsltCode", resultCode);
                body.put("rsltMsg", "회원가입 성공");
            } else if (resultCode.equals("2002")) {
                body.put("rsltCode", resultCode);
                body.put("rsltMsg", "이메일 중복");
            }

        }

        result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 회원탈퇴
    @PostMapping("/out")
    public ResponseEntity<MspResult> out(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            boolean isDelete = memberService.out(loginId);

            if (isDelete) {
                body.put("rsltCode", "0000");
                body.put("rsltMsg", "회원탈퇴 성공");
            } else {
                body.put("rsltCode", "2003");
                body.put("rsltMsg", "존재하지 않는 회원입니다.");
            }
        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<MspResult> update(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");
        String nickname = request.get("nickname");
        String phone = request.get("phone");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            boolean isUpdate = memberService.update(loginId, password, nickname, phone);

            if (isUpdate) {
                body.put("rsltCode", "0000");
                body.put("rsltMsg", "회원정보 수정 성공");
            } else {
                body.put("rsltCode", "2003");
                body.put("rsltMsg", "존재하지 않는 회원입니다.");
            }
        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MspResult> login(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty() || password.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            String resultCode = memberService.login(loginId, password);
            body.put("rsltCode", resultCode);

            if (resultCode.equals("0000")) {
                body.put("rsltMsg", "로그인 성공");
            } else if (resultCode.equals("2001")) {
                body.put("rsltMsg", "아이디 혹은 비밀번호가 맞지 않습니다.");
            }

        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<MspResult> logout(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");

        String resultCode = memberService.logout(loginId);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("rsltCode", resultCode);
        body.put("rsltMsg", "로그아웃 성공");

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 아이디 중복 검사
    @PostMapping("/duplicate")
    public ResponseEntity<MspResult> duplicate(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            String resultCode = memberService.duplicate(loginId);

            body.put("rsltCode", resultCode);

            if (resultCode.equals("0000")) {
                body.put("rsltMsg", "이메일 중복 아님");
            } else if (resultCode.equals("2002")) {
                body.put("rsltMsg", "이메일 중복");
            }

        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 회원정보 조회
    @PostMapping("/info")
    public ResponseEntity<MspResult> info(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            Member member = memberService.info(loginId);

            if (member != null) {
                body.put("rsltCode", "0000");
                body.put("rsltMsg", "회원이 존재합니다.");
                body.put("loginId", member.getLoginId());
                body.put("nickname", member.getNickname());
                body.put("phone", member.getPhone());
            } else {
                body.put("rsltCode", "2003");
                body.put("rsltMsg", "회원이 존재하지 않습니다.");
            }
        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 개인정보 확인 (존재 여부)
    @PostMapping("/find")
    public ResponseEntity<MspResult> find(@RequestBody Member member) {
        MspResult result;

        Map<String, String> body = new LinkedHashMap<>();

        if (member.getLoginId().isEmpty() || member.getPhone()
            .isEmpty() || member.getNickname().isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            boolean isExist = memberService.find(member.getLoginId(), member.getPhone(),
                member.getNickname());

            if (isExist) {
                body.put("rsltCode", "0000");
                body.put("rsltMsg", "회원이 존재합니다.");
                body.put("existYn", "Y");
            } else {
                body.put("rsltCode", "2003");
                body.put("rsltMsg", "회원이 존재하지 않습니다.");
                body.put("existYn", "N");
            }

        }

        result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<MspResult> findId(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String loginId = memberService.findId(phone);

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId == null) {
            body.put("rsltCode", "2003");
            body.put("rsltMsg", "등록된 전화번호가 없습니다.");
        } else {
            body.put("rsltCode", "0000");
            body.put("rsltMsg", "아이디 찾기 성공");
            body.put("loginId", loginId);
        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);
        return ResponseEntity.ok(result);
    }

    // 비밀번호 확인
    @PostMapping("/chkPwd")
    public ResponseEntity<MspResult> chkPwd(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty() || password.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else {
            String resultCode = memberService.chkPwd(loginId, password);

            body.put("rsltCode", resultCode);

            if (resultCode.equals("0000")) {
                body.put("rsltMsg", "비밀번호 일치");
            } else if (resultCode.equals("2001")) {
                body.put("rsltMsg", "비밀번호 불일치");
            } else if (resultCode.equals("2003")) {
                body.put("rsltMsg", "회원이 존재하지 않습니다.");
            }

        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);
        return ResponseEntity.ok(result);
    }

    // 비밀번호 수정
    @PostMapping("/password")
    public ResponseEntity<MspResult> password(@RequestBody Map<String, String> request) {
        String loginId = request.get("loginId");
        String password = request.get("password");

        Map<String, String> body = new LinkedHashMap<>();

        if (loginId.isEmpty() || password.isEmpty()) {
            body.put("rsltCode", "1001");
            body.put("rsltMsg", "필수값 누락");
        } else if (!isValidPassword(password)) {
            body.put("rsltCode", "1002");
            body.put("rsltMsg", "비밀번호 형식이 올바르지 않습니다.");
        } else {
            boolean isUpdate = memberService.password(loginId, password);

            if (isUpdate) {
                body.put("rsltCode", "0000");
                body.put("rsltMsg", "비밀번호 수정 성공");
            } else {
                body.put("rsltCode", "2003");
                body.put("rsltMsg", "존재하지 않는 회원입니다.");
            }
        }

        MspResult result = MspUtil.makeResult(MspStatus.OK, body);

        return ResponseEntity.ok(result);
    }

    // 아이디 유효성 (총 5자 이상)
    private boolean isValidLoginId(String loginId) {
        return loginId != null && loginId.length() >= 5 && !loginId.contains(" ");
    }

    // 비밀번호 유효성 (영문, 숫자, 특수문자 각각 1개 이상 + 총 8자 이상)
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).{8,}$";

        return password.matches(regex);
    }

}
