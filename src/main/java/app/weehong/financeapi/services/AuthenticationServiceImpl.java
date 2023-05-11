package app.weehong.financeapi.services;

import app.weehong.financeapi.dtos.requests.UserRequestDto;
import app.weehong.financeapi.entities.User;
import app.weehong.financeapi.entities.VerificationToken;
import app.weehong.financeapi.repositories.UserRepository;
import app.weehong.financeapi.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void register(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.username());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    @Override
    public boolean verify(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        return verificationToken.filter(user ->
                        !user.getUser().getIsVerified())
                .stream()
                .findFirst()
                .map(user -> {
                    User holder = user.getUser();
                    holder.setIsVerified(true);
                    userRepository.save(holder);
                    return true;
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void createVerificationToken(UserRequestDto request, String verificationToken) {
        Optional<User> user = userRepository.findByEmail(request.email());

        if (!user.isPresent()) {
            throw new RuntimeException();
        }

        VerificationToken token = new VerificationToken();
        token.setToken(verificationToken);
        token.setUser(user.get());

        verificationTokenRepository.save(token);
    }
}
