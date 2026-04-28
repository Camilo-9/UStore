
package edu.unimagdalena.uStore.security.web;

import edu.unimagdalena.uStore.security.domine.AppUser;
import edu.unimagdalena.uStore.security.domine.Role;
import edu.unimagdalena.uStore.security.repo.AppUserRepository;
import edu.unimagdalena.uStore.security.dto.AuthDtos.*;
import edu.unimagdalena.uStore.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController{
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                          AuthenticationManager authenticationManager, JwtService jwtService){
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerClient(@Valid
                                                       @RequestBody
                                                       RegisterClientRequest req){
        return register(req.email(), req.password(), Set.of(Role.ROLE_CLIENT));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid
                                                      @RequestBody
                                                      RegisterAdminRequest req){
        return register(req.email(), req.password(), Set.of(Role.ROLE_ADMIN));
    }

    @PostMapping("/register/coordinator")
    public ResponseEntity<AuthResponse> registerCoordinator(@Valid
                                                            @RequestBody
                                                            RegisterCoordinatorRequest req){
        return register(req.email(), req.password(), Set.of(Role.ROLE_COORDINATOR));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid
                                              @RequestBody
                                              LoginRequest req){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(),
                                           req.password()));
        var user = appUserRepository.findByEmailIgnoreCase(req.email()).orElseThrow();
        var principal = User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                            .build();
        var token = jwtService.generateToken(principal, Map.of("roles", user.getRoles()));

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds()));
    }

    private ResponseEntity<AuthResponse> register(String userName, String password, Set<Role> roles){
        if(appUserRepository.existsByUserNameIgnoreCase(userName))
            return ResponseEntity.badRequest().build();

        AppUser user = new AppUser();
        user.setUserName(userName);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(roles);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        appUserRepository.save(user);

        var principal = User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(roles.stream().map(Enum::name).toArray(String[]::new))
                            .build();

        var token = jwtService.generateToken(principal, Map.of("roles", roles));

        return ResponseEntity.ok(new AuthResponse(token, "Bearer",
                                 jwtService.getExpirationSeconds()));
    }
}
