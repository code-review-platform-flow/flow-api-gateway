package org.flow.gateway.service.users.persistence;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.mapper.UsersMapper;
import org.flow.gateway.repository.UsersRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public Mono<UsersDto> findByEmail(String email) {
        return usersRepository.findByEmail(email)
            .map(usersMapper::toDto)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)));
    }
}
