package ui;

import user.Rol;
import user.User;

import java.util.ArrayList;
import java.util.List;

public class DataSeeds {

    public List<User> seedUsers(){
        List<User> seedUsers = new ArrayList<>();

        User user1 = new User(Rol.STUDENT,"12345", "Santiago Sierra", 19);
        User user2 = new User(Rol.PROFESSOR,"12346", "Santiago Herrera", 29);
        User user3 = new User(Rol.STUDENT,"12347", "Santiago Nadal", 39);
        seedUsers.add(user1);
        seedUsers.add(user2);
        seedUsers.add(user3);

        return seedUsers;
    }

}
