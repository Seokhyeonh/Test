package org.example.spartaboard.service;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.spartaboard.dto.ProfileModifyRequestDto;
import org.example.spartaboard.dto.ProfileRequestDto;
import org.example.spartaboard.dto.ProfileResponseDto;
import org.example.spartaboard.entity.User;
import org.example.spartaboard.entity.UserStatus;
import org.example.spartaboard.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserRepository userRepository;

    //프로필 조회 //추후 security 작업 후 UserDetails 로 변경하여 재작성 할 것
    public ResponseEntity<ProfileResponseDto> showProfile(ProfileRequestDto requestDto) {

        //requestDto 의 UserId 로 DB 에서 일치하는 (조회 할) 유저 찾기
        String requestUserId = requestDto.getUserId();
        User requestUser = userRepository.findByUserId(requestUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"잘못된 접근입니다.")
        );

        //Request 의 user 가 탈퇴(INACTIVE) 인지 아닌지(ACTIVE) 확인 //회원탈퇴시 상태만 변경하기 때문에 확인을 거치는게 맞으나, 요구사항에 없기때문에 하지 않아도 되긴 함
        boolean activeUser = userRepository.existsUserByUserIdAndStatus(requestUserId, UserStatus.ACTIVE);
        if (!activeUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new ProfileResponseDto(requestUser));
    }

    //본인 프로필 수정(username,introduce)
    @Transactional
    public ResponseEntity<ProfileResponseDto> updateProfile(ProfileModifyRequestDto modifyRequestDto, String userId) {
        //빈 dto 라면 "수정할 내역 없음"
        nullCheck(modifyRequestDto);
        User authorizeUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"잘못된 접근입니다.")
        );
        writePasswordCheck(modifyRequestDto, authorizeUser);

        //Dto 필드 입력값을 체크하고, 입력값이 있는 경우 null 반영되지 않도록 하며 업데이트(비밀번호 포함)
        authorizeUser.update(modifyRequestDto);
        return ResponseEntity.ok(new ProfileResponseDto(authorizeUser));
    }


    //old 와 new 패스워드 모두 입력 되었는지 확인하는 메서드
    private void writePasswordCheck(ProfileModifyRequestDto modifyRequestDto, User user) {

        String newPW = modifyRequestDto.getNewPassword();
        String oldPW = modifyRequestDto.getOldPassword();
        String originalPW = user.getPassword();

        if (newPW != null && oldPW == null) {
            throw new IllegalArgumentException("현재 비밀번호를 입력하지 않으셨습니다.");
        }
        if (newPW == null && oldPW != null) {
            throw new IllegalArgumentException("새 비밀번호를 입력하지 않으셨습니다.");
        }
        if (newPW != null && oldPW != null) {
            passwordCheck(newPW, oldPW, originalPW);
        }
    }

    //비밀번호 형식이 올비른지 확인 //기존 password 확인하는 메서드
    private void passwordCheck(String newPW, String oldPW, String originalPW) {
        //확인용 비밀번호가 original 비밀번호와 일치하지 않은 경우 -> Exception
        if (!oldPW.equals(originalPW)) {
            throw new IllegalArgumentException("현재 비밀번호를 잘못 입력하셨습니다.");
        }
        // 현재 비밀번호와 동일한 비밀번호로 수정하는 경우
        if(newPW.equals(originalPW)) {
            throw new IllegalArgumentException("이미 사용중인 비밀번호 입니다.");
        }
    }

    //null check 메서드
    private void nullCheck(ProfileModifyRequestDto dto) {
        boolean fieldIsNull = true;
        try {
            Field[] fields = dto.getClass().getDeclaredFields();
            for (Field field : fields) {
                log.warn(field.getName() + " : " );
                field.setAccessible(true);
                Object value = field.get(dto);
                if (value == null) {
                    log.warn("null입니다.");
                } else {
                    log.warn(value.toString());
                    fieldIsNull = false;
                }
            }
        } catch (Exception e) {
            log.warn("Null Check 중 error 발생");
        }
        //모든 field 가 null 인 경우 사용자에게 알리기(exception 처리)
        if (fieldIsNull) {
            throw new NoResultException("수정할 내용이 없습니다.");
        }
    }
}
