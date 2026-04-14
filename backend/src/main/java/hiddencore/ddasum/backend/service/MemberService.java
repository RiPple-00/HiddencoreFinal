package hiddencore.ddasum.backend.service;

import hiddencore.ddasum.backend.domain.Users;
import hiddencore.ddasum.backend.repository.MemberRepository;
import hiddencore.ddasum.backend.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // ═══════════════════════════════════════════════════════════
    // 회원가입
    // ═══════════════════════════════════════════════════════════

    @Transactional
    public MemberDto.MemberResponse signUp(MemberDto.SignUpRequest request) {
        if (memberRepository.existsByLoginId(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
        if (memberRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 전화번호입니다.");
        }

        Users users = Users.builder()
                .loginId(request.getUserId())
                .name(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Users.UsersRole.GUARDIAN)
                .status(Users.UsersStatus.ACTIVE)
                .build();

        Users saved = memberRepository.save(users);
        return toMemberResponse(saved);
    }

    // ═══════════════════════════════════════════════════════════
    // 로그인
    // ═══════════════════════════════════════════════════════════

    public MemberDto.LoginResponse login(MemberDto.LoginRequest request) {
        Users users = memberRepository.findByLoginId(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), users.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return MemberDto.LoginResponse.builder()
                .token("token-" + UUID.randomUUID())
                .id(users.getUserId())
                .userId(users.getLoginId())
                .username(users.getName())
                .email(users.getEmail())
                .phone(users.getPhone())
                .role(users.getRole())
                .build();
    }

    // ═══════════════════════════════════════════════════════════
    // 사용자명 중복 체크 (회원가입 폼에서 사용)
    // ═══════════════════════════════════════════════════════════

    public boolean isUsernameAvailable(String username) {
        return !memberRepository.existsByName(username);
    }

    // ═══════════════════════════════════════════════════════════
    // 이메일 중복 체크 (회원가입 폼에서 사용)
    // ═══════════════════════════════════════════════════════════

    public boolean isEmailAvailable(String email) {
        return !memberRepository.existsByEmail(email);
    }

    // ═══════════════════════════════════════════════════════════
    // 회원 조회(사용자명으로)
    // ═══════════════════════════════════════════════════════════

    public MemberDto.MemberResponse getMember(Long id) {
        Users users = getUsersOrThrow(id);
        return toMemberResponse(users);
    }

    // ═══════════════════════════════════════════════════════════
    // 전체 회원 목록 조회
    // ═══════════════════════════════════════════════════════════

    public List<MemberDto.MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toMemberResponse)
                .toList();
    }

    // ═══════════════════════════════════════════════════════════
    // 회원 정보 수정
    // ═══════════════════════════════════════════════════════════

    @Transactional
    public MemberDto.MemberResponse updateMember(Long id, MemberDto.UpdateRequest request) {
        Users users = getUsersOrThrow(id);

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            users.setName(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(users.getEmail()) && memberRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
            }
            users.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            if (!request.getPhone().equals(users.getPhone()) && memberRepository.existsByPhone(request.getPhone())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 전화번호입니다.");
            }
            users.setPhone(request.getPhone());
        }

        return toMemberResponse(users);
    }

    // ═══════════════════════════════════════════════════════════
    // 회원 삭제
    // ═══════════════════════════════════════════════════════════

    @Transactional
    public void deleteMember(Long id) {
        Users users = getUsersOrThrow(id);
        users.setStatus(Users.UsersStatus.DELETED);
    }

    private Users getUsersOrThrow(Long id) {
        return memberRepository.findByUserId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."));
    }

    private MemberDto.MemberResponse toMemberResponse(Users users) {
        return MemberDto.MemberResponse.builder()
                .id(users.getUserId())
                .userId(users.getLoginId())
                .username(users.getName())
                .email(users.getEmail())
                .phone(users.getPhone())
                .role(users.getRole())
                .createdAt(users.getCreatedAt())
                .updatedAt(users.getUpdatedAt())
                .build();
    }
}
