package com.electronicssales.services.impls;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.User;
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
    @Transactional
    public User saveUser(UserDto userDto) {
        return userRepository
            .save(userDtoToUserMapper.map(userDto));
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found !");
        }
        return UserPrincipal.of(user);
    }

    @Component
    class UserDtoMapper implements Mapper<User, UserDto> {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public User map(UserDto dto) {
            User user = new User();

            user.setActived(dto.isActived());
            user.setFirstName(dto.getFirstname());
            user.setLastName(dto.getLastname());
            user.setId(dto.getId());
            user.setUsername(dto.getUsername());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setAddress(dto.getAddress());
            user.setAvartar(dto.getAvartartId() > 0 ? new Image(dto.getAvartartId()) : null);
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmail(dto.getEmail());
            user.setGender(dto.isGender());
            user.setRole(Role.of(dto.getRoleName()));
            user.setBirthday(dto.getBirthday());

            return user;
        }
        
    }

    @Component
    class UserInfoMapper implements Mapper<UserInfo, User> {

        @Override
        public UserInfo map(User user) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setFirstname(user.getFirstName());
            userInfo.setLastname(user.getLastName());
            userInfo.setBirthday(user.getBirthday());
            userInfo.setGender(user.isGender());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhoneNumber(user.getPhoneNumber());
            userInfo.setAddress(user.getAddress());
            userInfo.setAvartartId(user.getAvartar() != null ? user.getAvartar().getId() : 0);

            return userInfo;
        }
        
    }
    
}