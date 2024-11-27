package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();


    public User(String email, String login, String name, LocalDate localDate) {// подумать про друзей
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = localDate;
    }

    public User(int id, String email, String login, String name, LocalDate localDate) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = localDate;
    }

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }


}
