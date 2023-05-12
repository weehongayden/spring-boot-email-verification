package app.weehong.financeapi.filters;

import app.weehong.financeapi.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class EmailVerificationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isEmailVerify(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isEmailVerify(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization").substring(7);
        try {
            Jws<Claims> parsedToken = Jwts.parser().parseClaimsJws(jwtToken);
            Claims claims = parsedToken.getBody();

            // Access the claims to retrieve information
            String username = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
