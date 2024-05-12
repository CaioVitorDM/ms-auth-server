package ufrn.imd.br.msauthserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.imd.br.msauthserver.dto.ApiResponseDTO;
import ufrn.imd.br.msauthserver.dto.AuthRequest;
import ufrn.imd.br.msauthserver.dto.AuthResponse;
import ufrn.imd.br.msauthserver.service.AuthenticationService;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponseDTO<AuthResponse>> authenticate(
            @RequestBody AuthRequest request) {
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        true,
                        "Authentication completed successfully",
                        authenticationService.authenticate(request),
                        null));
    }



    /*@PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authenticationService.refreshAccessToken(request);
        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        true,
                        "Token refreshed successfully",
                        response,
                        null
                ));
    }*/
}
