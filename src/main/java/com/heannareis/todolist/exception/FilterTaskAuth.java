package com.heannareis.todolist.exception;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.heannareis.todolist.repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{
    @Autowired
    private IUserRepository userRepository;
    @Override
    protected void  doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")){

            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecoded);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);
            if (user == null){
                response.sendError(401, "Usuário não encontrado");
            }
            else{
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVerify.verified){
//                    System.out.println("Authorization");
//                    System.out.println(username);
//                    System.out.println(password);
                    request.setAttribute("idUser", user.getId());
                    filterchain.doFilter(request, response);
                }
                else{
                    response.sendError(401, "Sem acesso!");
                }
            }
        }else{
            filterchain.doFilter(request, response);
        }
    }
}
