package com.electronicssales.services.impls;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.User;
import com.electronicssales.exceptions.UserExistsException;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfo;
import com.electronicssales.models.types.Role;
import com.electronicssales.repositories.UserRepository;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DefaultUserService implements UserService {

    @Autowired UserRepository userRepository;

    @Autowired
    private Mapper<UserInfo, User> userInfoMapper;

    @Autowired
    private Mapper<User, UserDto> userDtoToUserMapper;

    @Override
    public User createUser(UserDto userDto, Role role) {
        if(existByUsername(userDto.getUsername())) {
            throw new UserExistsException(UserExistsException.Field.USERNAME);
        }

        if(existsByEmail(userDto.getEmail())) {
            throw new UserExistsException(UserExistsException.Field.EMAIL);
        }        

        if(existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new UserExistsException(UserExistsException.Field.PHONE_NUMBER);
        }

        User user = userDtoToUserMapper.map(userDto);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public UserInfo getUserInfoByUsername(String username) {
        return userInfoMapper.map(userRepository.findByUsername(username));
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if(user == null) {
            throw new UsernameNotFoundException("User not found !");
        }
        return UserPrincipal.of(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Component
    public static class UserDtoMapper implements Mapper<User, UserDto> {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public User map(UserDto dto) {
            User user = new User();

            user.setActived(dto.isActived());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setAddress(dto.getAddress());
            user.setAvartar(dto.getAvartarId() > 0 ? new Image(dto.getAvartarId()) : null);
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmail(dto.getEmail());
            user.setGender(dto.isGender());
            user.setBirthday(dto.getBirthday());

            return user;
        }
        
    }

    @Component
    public static class UserInfoMapper implements Mapper<UserInfo, User> {

        @Override
        public UserInfo map(User user) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setFirstname(user.getFirstname());
            userInfo.setLastname(user.getLastname());
            userInfo.setBirthday(user.getBirthday());
            userInfo.setGender(user.isGender());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhoneNumber(user.getPhoneNumber());
            userInfo.setAddress(user.getAddress());
            userInfo.setAvartarId(user.getAvartar() != null ? user.getAvartar().getId() : 0);

            return userInfo;
        }
        
    }
    
}