package ru.ssau.tk.phoenix.ooplabs.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.phoenix.ooplabs.dto.UserRequest;
import ru.ssau.tk.phoenix.ooplabs.dto.UserResponse;
import ru.ssau.tk.phoenix.ooplabs.service.UserService;

@RestController
@RequestMapping(path = "mathhub/api/users")
public class UserController {
    private final Logger logger;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
        this.logger = LogManager.getLogger();
    }

    @GetMapping(value = {"{id}", ""})
    public UserResponse find(@PathVariable(required = false) Long id,
                             @RequestParam(required = false) String username) {
        logger.info("Поиск пользователя id={}, username={}", id, username);

        if (id != null) return userService.find(id);
        else if (username != null && !username.isEmpty()) return userService.find(username);

        logger.error("Неверные критерии для поиска");
        throw new IllegalArgumentException("Неверные критерии для поиска");
    }

    @GetMapping("auth")
    public boolean auth(@RequestParam String username,
                        @RequestParam String password){
        return userService.auth(username, password);
    }

    @PostMapping
    public UserResponse create(@RequestBody UserRequest user){
        return userService.save(user);
    }

    @PutMapping(path = "{id}")
    public UserResponse update(@PathVariable Long id,
                               @RequestParam String password){
        return userService.update(id, password);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
