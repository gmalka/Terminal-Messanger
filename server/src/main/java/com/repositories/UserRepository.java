package com.repositories;

import com.model.User;

public interface UserRepository extends Repository<User>{
    User getUserByName(String name);
}
