//package sequence.sequence_member.member.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import sequence.sequence_member.member.dto.CustomUserDetails;
//import sequence.sequence_member.member.entity.MemberEntity;
//import sequence.sequence_member.member.repository.MemberRepository;
//
//import java.util.Optional;
//
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return memberRepository.findByUsername(username)
//                .map(CustomUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//    }
//}

package sequence.sequence_member.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sequence.sequence_member.member.dto.CustomUserDetails;
import sequence.sequence_member.member.entity.MemberEntity;
import sequence.sequence_member.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 🔥 Fix: Ensure that we never return null, instead throw an exception
//        return memberRepository.findByUsername(username)
//                .map(CustomUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 사용자 조회 시도: " + username); // 디버깅 로그

        return memberRepository.findByUsername(username)
                .map(user -> {
                    System.out.println("✅ 사용자 찾음: " + user.getUsername()); // 디버깅 로그
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> {
                    System.out.println("❌ 사용자 없음: " + username); // 디버깅 로그
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }

}
