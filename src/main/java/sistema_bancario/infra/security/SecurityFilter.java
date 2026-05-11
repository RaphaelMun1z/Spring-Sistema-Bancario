package sistema_bancario.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import sistema_bancario.entities.users.User;
import sistema_bancario.repositories.UserRepository;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository<User> userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository<User> userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.contains("/actuator/health");
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            var token = this.recoverToken(request);

            if (token != null) {
                var email = tokenService.validateToken(token);

                if (email != null) {
                    UserDetails user = userRepository.findByEmail(email);

                    if (user != null) {
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        System.out.println("SecurityFilter: Utilizador não encontrado na Base de Dados para o email: " + email);
                    }
                } else {
                    System.out.println("SecurityFilter: O Token JWT recebido é nulo ou a validação falhou.");
                }
            }
        } catch (Exception e) {
            System.out.println("SecurityFilter: Erro inesperado ao processar o token -> " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
