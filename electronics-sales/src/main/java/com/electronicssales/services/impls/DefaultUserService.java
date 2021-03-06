package com.electronicssales.services.impls;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.User;
import com.electronicssales.entities.UserInfo;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.UserProjections;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.repositories.UserRepository;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User createUser(UserDto userDto, Role role) {
        if (existByUsername(userDto.getUsername())) {
            throw new EntityExistsException("User is already exists !");
        }

        User user = Optional.of(userDto).map(userDtoToUserMapper::mapping).get();
        user.setRole(role);
        return userRepository.persist(user);
    }

    @Transactional
    @Override
    public User updateUser(UserDto newUserDto) {
        User userPersisted = userRepository.findByIdAndFetchUserInfo(newUserDto.getId())
                .orElseThrow(() -> new EntityExistsException("User not found"));

        if (!userPersisted.getUsername().equals(newUserDto.getUsername())
                && userRepository.existsByUsername(newUserDto.getUsername())) {
            throw new EntityExistsException(
                    new StringBuilder().append(User.class.getSimpleName().toUpperCase()).append(" with USERNAME = \"")
                            .append(newUserDto.getUsername()).append("\" is already exists !").toString());
        }

        User userTransient = userDtoToUserMapper.mapping(newUserDto);
        if(!StringUtils.hasText(newUserDto.getPassword())) {
            userTransient.setPassword(userPersisted.getPassword());
        }
        userTransient.getUserInfo().setId(userPersisted.getUserInfo().getId());
        userTransient.setRole(userPersisted.getRole());
        userRepository.merge(userTransient);
        return userTransient;
    }

    @Transactional
    @Override
    public UserInfoResponse getUserInfoByUsername(String username) {
        User user = userRepository.findByUsernameAndFetchUserInfo(username)
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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found !"));

        return UserPrincipal.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<UserProjections> fetchEmployees(String search, Pageable pageable) {
        return this.userRepository.fetchAllEmployees(search, pageable);
    }

    @Transactional
    @Override
    public void updateActived(long userId, boolean actived) {
        userRepository.updateActived(userId, actived);
    }

    @Transactional
    @Override
    public void updatePasswordByUsername(String username, String password) {
        userRepository.updatePasswordByUserName(username, password);
    }

    @Override
    public boolean checkValidOldPassword(String username, String oldPassword) {
        String password = userRepository.getPasswordOf(username)
            .orElseThrow(EntityNotFoundException::new);
        return passwordEncoder.matches(oldPassword, password);
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
            System.out.println(dto);
            if(dto.getId() != null) {
                user.setId(dto.getId());
            }
            user.setActived(true);
            user.setUsername(dto.getUsername());
            if(Objects.nonNull(dto.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            Optional.ofNullable(dto.getAvartar())
                    .ifPresent(avartar -> user.setAvartar(Image.of(avartar)));
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
            userInfoResponse.setUserInfoId(user.getUserInfo().getId());
            userInfoResponse.setUsername(user.getUsername());
            userInfoResponse.setFirstname(userInfo.getFirstname());
            userInfoResponse.setLastname(userInfo.getLastname());
            userInfoResponse.setBirthday(userInfo.getBirthday());
            userInfoResponse.setGender(userInfo.isGender());
            userInfoResponse.setEmail(userInfo.getEmail());
            userInfoResponse.setPhoneNumber(userInfo.getPhoneNumber());
            userInfoResponse.setAddress(userInfo.getAddress());
            Optional.ofNullable(user.getAvartar())
                .ifPresent(avartar -> userInfoResponse.setAvartarId(avartar.getId()));
            userInfoResponse.setRole(user.getRole());
            return userInfoResponse;
        }

    }

}