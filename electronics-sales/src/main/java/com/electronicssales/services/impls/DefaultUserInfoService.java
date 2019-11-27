package com.electronicssales.services.impls;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.UserInfo;
import com.electronicssales.models.UserInfoProjections;
import com.electronicssales.models.responses.RequiredUserInfoDto;
import com.electronicssales.repositories.UserInfoRepository;
import com.electronicssales.services.UserInfoService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class DefaultUserInfoService implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Lazy
    @Autowired
    private Mapper<UserInfoProjections, UserInfo> userInfoProjectionsMapper;

    @Lazy
    @Autowired
    private Mapper<UserInfo, RequiredUserInfoDto> userInfoMapper;

    private Optional<UserInfoProjections> flatMapToUserInfoProjections(UserInfo userInfo) {
        Optional<UserInfo> userInfoOptional = Optional.ofNullable(userInfo);
        if(!userInfoOptional.isPresent()) {
            return Optional.empty();
        }

        return userInfoOptional
            .map(this.userInfoProjectionsMapper::mapping);
    }

    @Override
    public List<UserInfoProjections> findAll() {
        return this.userInfoRepository
            .findAllUserInfoProjections();
    }

    @Override
    public Optional<UserInfoProjections> findById(Long id) {
        return this.userInfoRepository
            .findById(id)
            .flatMap(this::flatMapToUserInfoProjections);
    }

    @Override
    public Optional<UserInfoProjections> findByIdOrUserId(Long idOrUserId) {
        return this.userInfoRepository
            .findByIdOrUserId(idOrUserId);
    }

    @Override
    public UserInfoProjections save(RequiredUserInfoDto userInfo) {
        return Optional.of(userInfo)
            .map(this.userInfoMapper::mapping)
            .map(this.userInfoRepository::save)
            .map(this.userInfoProjectionsMapper::mapping)
            .get();
    }

    @Override
    public void deleteById(Long id) {
        this.userInfoRepository.deleteById(id);
    }

    @Lazy
    @Component
    public class UserInfoProjectionsMapper implements Mapper<UserInfoProjections, UserInfo> {

        @Override
        public UserInfoProjections mapping(UserInfo userInfo) {
            return new UserInfoProjections(
                userInfo.getId(), 
                userInfo.getLastname(), 
                userInfo.getFirstname(),
                userInfo.getPhoneNumber(), 
                userInfo.getEmail(), 
                userInfo.getAddress(), 
                userInfo.isGender(),
                userInfo.getBirthday()
            );
        }

    }

    @Lazy
    @Component
    public class UserInfoMapper implements Mapper<UserInfo, RequiredUserInfoDto> {

        @Override
        public UserInfo mapping(RequiredUserInfoDto userInfoDto) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userInfoDto.getUserInfoId());
            userInfo.setLastname(userInfoDto.getLastname());
            userInfo.setFirstname(userInfoDto.getFirstname());
            userInfo.setBirthday(userInfoDto.getBirthday());
            userInfo.setGender(userInfoDto.isGender());
            userInfo.setEmail(userInfoDto.getEmail());
            userInfo.setAddress(userInfoDto.getAddress());
            userInfo.setPhoneNumber(userInfoDto.getPhoneNumber());
            return userInfo;
        }

    }
}