package com.electronicssales.services.impls;


import java.util.Optional;

import javax.persistence.EntityExistsException;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.User;
import com.electronicssales.entities.UserInfo;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.repositories.UserRepository;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Lazy
@Service
public class DefaultUserService implements UserService {

    @Lazy
    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private Mapper<UserInfoResponse, User> userInfoResponseMapper;

    @Lazy
    @Autowired
    private Mapper<User, UserDto> userDtoToUserMapper;

    @Transactional
    @Override
    public User createUser(UserDto userDto, Role role) {
        if(existByUsername(userDto.getUsername())) {
            throw new EntityExistsException("User is already exists !");
        }

        User user = userDtoToUserMapper.mapping(userDto);
        user.setRole(role);
        return userRepository.persist(user);
    }

    @Transactional
    @Override
    public User updateUser(UserDto newUserDto) {
        User userPersisted = userRepository
            .findByIdAndFetchUserInfo(newUserDto.getId())
            .orElseThrow(() -> new EntityExistsException("User not found"));
        
        if(!userPersisted.getUsername().equals(newUserDto.getUsername())
            && userRepository.existsByUsername(newUserDto.getUsername()))
        {
            throw new EntityExistsException(
                new StringBuilder()
                .append(User.class.getSimpleName().toUpperCase())
                .append(" with USERNAME = \"")
                .append(newUserDto.getUsername())
                .append("\" is already exists !")
                .toString()
            );
        }

        User userTransient = userDtoToUserMapper.mapping(newUserDto);
        userTransient.getUserInfo().setId(userPersisted.getUserInfo().getId());
        userTransient.setRole(userPersisted.getRole());
        userRepository.merge(userTransient);
        return userTransient;
    }

    @Transactional
    @Override
    public UserInfoResponse getUserInfoByUsername(String username) {
        User user = userRepository
            .findByUsernameAndFetchUserInfo(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found !"));
        return userInfoResponseMapper.mapping(user);
    }

    @Transactional
    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    @Override
    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user =  userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found !"));
        return UserPrincipal.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Lazy
    @Component
    public class UserInfoMapper implements Mapper<UserInfo, UserDto> {


        @Override
        public UserInfo mapping(UserDto userDto) {
            UserInfo userInfo = new UserInfo();

            userInfo.setFirstname(userDto.getFirstname());
            userInfo.setLastname(userDto.getLastname());
            userInfo.setPhoneNumber(userDto.getPhoneNumber());
            userInfo.setEmail(userDto.getEmail());
            userInfo.setAddress(userDto.getAddress());
            userInfo.setBirthday(userDto.getBirthday());
            userInfo.setGender(userDto.isGender());
            return userInfo;
        }
    
        
    }

    @Lazy
    @Component
    class UserDtoMapper implements Mapper<User, UserDto> {

        @Lazy
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Lazy
        @Autowired
        private Mapper<UserInfo, UserDto> userInfoMapper;

        @Override
        public User mapping(UserDto dto) {
            User user = new User();
            user.setId(dto.getId());
            user.setActived(true);
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            if(dto.getAvartarId() > 0) {
                user.setAvartar(new Image(dto.getAvartarId()));
            }
            user.setUserInfo(userInfoMapper.mapping(dto));
            return user;
        }
        
    }

    @Lazy
    @Component
    class UserInfoResponseMapper implements Mapper<UserInfoResponse, User> {

        @Override
        public UserInfoResponse mapping(User user) {
            UserInfoResponse userInfoResponse = new UserInfoResponse();

            UserInfo userInfo = user.getUserInfo();

            userInfoResponse.setId(user.getId());
            userInfoResponse.setUsername(user.getUsername());
            userInfoResponse.setFirstname(userInfo.getFirstname());
            userInfoResponse.setLastname(userInfo.getLastname());
            userInfoResponse.setBirthday(userInfo.getBirthday());
            userInfoResponse.setGender(userInfo.isGender());
            userInfoResponse.setEmail(userInfo.getEmail());
            userInfoResponse.setPhoneNumber(userInfo.getPhoneNumber());
            userInfoResponse.setAddress(userInfo.getAddress());
            userInfoResponse.setAvartarId(user.getAvartar() != null ? user.getAvartar().getId() : 0);
            return userInfoResponse;
        }
        
    }
    
}