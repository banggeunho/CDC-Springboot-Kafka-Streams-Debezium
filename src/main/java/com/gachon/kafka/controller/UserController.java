package com.gachon.kafka.controller;

import com.gachon.kafka.Dto.UserDto;
import com.gachon.kafka.srcDB.model.User;
import com.gachon.kafka.srcDB.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public User findById(@PathVariable int id){
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("")
    public User saveUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User userDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 id가 업습니다."));
        user.update(userDto.getName());
        return userRepository.save(user);}

    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return id;
    }

}
