package org.flow.gateway.service.usersessions.persistence;

import org.flow.gateway.dto.usersessions.UserSessionsDto;
import org.flow.gateway.entity.UserSessionsEntity;
import org.flow.gateway.mapper.UserSessionsMapper;
import org.flow.gateway.mapper.UsersMapper;
import org.flow.gateway.repository.UserSessionsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserSessionsService {

	private final UserSessionsRepository userSessionsRepository;
	private final UserSessionsMapper userSessionsMapper;
	private final UsersMapper usersMapper;
	private final TransactionalOperator transactionalOperator;

	public Mono<UserSessionsDto> save(UserSessionsDto userSessionsDto) {
		UserSessionsEntity userSessionsEntity = userSessionsMapper.toEntity(userSessionsDto);
		return userSessionsRepository.save(userSessionsEntity)
			.map(userSessionsMapper::toDto)
			.as(transactionalOperator::transactional);
	}

	public Mono<UserSessionsEntity> getSessionByAccessToken(String accessToken) {
		return userSessionsRepository.findByAccessToken(accessToken)
			.filter(UserSessionsEntity::isUseYn);  // useYn 필터링 추가
	}

	// public UserSessionsDto findByUserId(UsersDto usersDto){
	//     UsersEntity usersEntity = usersMapper.toEntity(usersDto);
	//     UserSessionsEntity userSessionsEntity = userSessionsRepository.findByUserId(usersEntity.getUserId())
	//         .orElseThrow(() -> new EntityNotFoundException("UserSession not found with user"));
	//     return userSessionsMapper.toDto(userSessionsEntity);
	// }
	//
	// public UserSessionsDto existsByUserId(UsersDto usersDto){
	//     Optional<UserSessionsEntity> userSessionsEntity = userSessionsRepository.findByUserId(usersDto.getUserId());
	//     return userSessionsEntity.map(userSessionsMapper::toDto).orElse(UserSessionsDto.builder().build());
	// }

}
