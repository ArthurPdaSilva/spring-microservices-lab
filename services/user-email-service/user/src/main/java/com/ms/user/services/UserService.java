package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Realizando injeção de dependência: Autowired ou via construtor
    final UserRepository userRepository;
    // O final é para dizer que a classe não pode ser reatribuída, ou seja, não pode ser substituída por outra classe. Isso é uma boa prática para garantir que a classe seja imutável e não possa ser alterada acidentalmente.
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    // Transactional: Garante que a operação seja atômica, ou seja, ou tudo é salvo ou nada é salvo
    @Transactional
    public UserModel save(UserModel userModel) {
        var user = userRepository.save(userModel);
        userProducer.publicMessageEmail(user);
        return user;
    }
}