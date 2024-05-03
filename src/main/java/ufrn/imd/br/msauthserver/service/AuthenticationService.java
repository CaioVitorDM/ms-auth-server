package ufrn.imd.br.msauthserver.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufrn.imd.br.msauthserver.dto.AuthRequest;
import ufrn.imd.br.msauthserver.dto.AuthResponse;
import ufrn.imd.br.msauthserver.model.User;
import ufrn.imd.br.msauthserver.repository.UserRepository;
import ufrn.imd.br.msauthserver.utils.exception.ResourceNotFoundException;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(), request.password()
                )
        );

        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
