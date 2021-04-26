package com.pineapple.authserver.service;

import com.pineapple.authserver.domain.Member;
import com.pineapple.authserver.dto.MemberDto;
import com.pineapple.authserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 전체 조회
     */
    public List<MemberDto> findMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDto> result = members.stream()
                .map(m -> new MemberDto(m))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증

        // 비밀번호 암호화
        String encodePassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodePassword);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getMemberId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}

