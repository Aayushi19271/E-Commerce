package com.bootcamp.ECommerceApplication.security;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.MailSendFailedException;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;


public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            logger.debug("Authentication failed: password does not match stored value");

            User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
            Integer temp = user.getFalseAttemptCount();
            user.setFalseAttemptCount(++temp);
            if (temp == 3) {

                user.setLocked(true);

                    try {
                        smtpMailSender.send(user.getEmail(), "Account Locked!", "Your Account has been locked due to multiple false attempts. "
                                + "Please contact Admin (ecommerce476@gmail.com) to get back your account");
                    } catch (MessagingException e) {
                        throw new MailSendFailedException("Failed to Send Mail: " + user.getEmail());
                    }
            }
            userRepository.save(user);

            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        User user1 = userRepository.findByEmailIgnoreCase(user.getUsername());
        user1.setFalseAttemptCount(0);
        userRepository.save(user1);
        return super.createSuccessAuthentication(principal, authentication, user);
    }
}