package dev.carrascon.bresca.security;

import dev.carrascon.bresca.security.GoogleTokenVerifier;
import dev.carrascon.bresca.user.User;
import dev.carrascon.bresca.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {
    @Autowired
    private UserRepository userRepository;

    @Value("${google.client-id}")
    private String googleClientId;

    @PostMapping("/google")
    public ResponseEntity<User> authenticateWithGoogle(@RequestBody String idTokenString) {
        Payload payload = GoogleTokenVerifier.verify(idTokenString, googleClientId);
        if (payload == null) {
            return ResponseEntity.badRequest().build();
        }

        String googleId = payload.getSubject();
        String name = (String) payload.get("name");
        String email = payload.getEmail();

        User user = userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setName(name);
            newUser.setEmail(email);
            return userRepository.save(newUser);
        });

        return ResponseEntity.ok(user);
    }
}
