package mg.java.springbootrestapi.service.impl;


import lombok.AllArgsConstructor;
import mg.java.springbootrestapi.dto.UserDto;
import mg.java.springbootrestapi.entity.User;
import mg.java.springbootrestapi.exception.EmailAlreadyExistsException;
import mg.java.springbootrestapi.exception.ResourceNotFoundException;
import mg.java.springbootrestapi.mapper.AutoUserMapper;
import mg.java.springbootrestapi.mapper.UserMapper;
import mg.java.springbootrestapi.repository.UserRepository;
import mg.java.springbootrestapi.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
   // private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {

        // Convert UserDto into User JPA Entity
        //User user = UserMapper.mapToUser(userDto);

        //User user = modelMapper.map(userDto, User.class);

//        User user = AutoUserMapper.MAPPER.mapToUser(userDto);
//
//        User savedUser = userRepository.save(user);

        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if(optionalUser.isPresent()){
            throw new EmailAlreadyExistsException("Email Already Exists for User");
        }

        User user = AutoUserMapper.MAPPER.mapToUser(userDto);

        User savedUser = userRepository.save(user);


        // Convert User JPA entity to UserDto
        //UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);

        //UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);

        UserDto savedUserDto = AutoUserMapper.MAPPER.mapToUserDto(savedUser);

        return savedUserDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
       // Optional<User> optionalUser = userRepository.findById(userId);
       // User user = optionalUser.get();
        // return UserMapper.mapToUserDto(user);
        // return modelMapper.map(user, UserDto.class);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        return AutoUserMapper.MAPPER.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
       // return users.stream().map(UserMapper::mapToUserDto)
         //       .collect(Collectors.toList());


       // return users.stream().map((user) -> modelMapper.map(user, UserDto.class))
          //      .collect(Collectors.toList());

        return users.stream().map((user) -> AutoUserMapper.MAPPER.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(UserDto user) {
//        User existingUser = userRepository.findById(user.getId()).get();
//        existingUser.setFirstName(user.getFirstName());
//        existingUser.setLastName(user.getLastName());
//        existingUser.setEmail(user.getEmail());
//        User updatedUser = userRepository.save(existingUser);
        //return UserMapper.mapToUserDto(updatedUser);

        //return modelMapper.map(updatedUser, UserDto.class);

        User existingUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", user.getId())
        );

        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updatedUser = userRepository.save(existingUser);

        return AutoUserMapper.MAPPER.mapToUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );

        userRepository.deleteById(userId);
}
}