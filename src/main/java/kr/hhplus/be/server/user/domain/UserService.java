package kr.hhplus.be.server.user.domain;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void verify(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("유저"));
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("유저"));
    }
}
