package org.flow.gateway.service.users.persistence;

import lombok.RequiredArgsConstructor;
import org.flow.gateway.dto.users.UsersDto;
import org.flow.gateway.entity.UsersEntity;
import org.flow.gateway.mapper.UsersMapper;
import org.flow.gateway.repository.UsersRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UsersService {

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Transactional
    public UsersDto save(UsersDto usersDto){
        UsersEntity usersEntity = usersMapper.toEntity(usersDto);
        usersRepository.save(usersEntity);
        return usersMapper.toDto(usersEntity);
    }

    public UsersDto findByEmail(String email){
        return usersMapper.toDto(usersRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }

}
