package com.cydeo.service.impl;


import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;





    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;

        this.userMapper = userMapper;

    }

    @Override
    public List<UserDTO> listAllUsers() {
       List<User> userList=userRepository.findAll(Sort.by("firstName"));

       return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String userName) {

    return userMapper.convertToDto(userRepository.findByUserName(userName));
    }

    @Override
    public void save(UserDTO user) {

      userRepository.save(userMapper.convertToEntity(user));

    }


    @Override
    public void deleteByUserName(String userName) {

        userRepository.deleteByUserName(userName);
    }

    @Override
    public UserDTO update(UserDTO userDto) {

        //Update dto obj to entity
        User convertedUser= userMapper.convertToEntity(userDto);
        //Set the id and update user from Database
        convertedUser.setId(userRepository.findByUserName(userDto.getUserName()).getId());
        //save updated user and return

        return userMapper.convertToDto(userRepository.save(convertedUser));
    }

    @Override
    public void delete(String username) {
        User user=userRepository.findByUserName(username);
        user.setIsDeleted(true);
       userRepository.save(user);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {


       return userRepository.findByRoleDescriptionIgnoreCase(role)
                .stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
