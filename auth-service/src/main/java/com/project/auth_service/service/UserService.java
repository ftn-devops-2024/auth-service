package com.project.auth_service.service;

import com.project.auth_service.dto.*;
import com.project.auth_service.exceptions.UnauthorizedException;
import com.project.auth_service.model.Role;
import com.project.auth_service.model.User;
import com.project.auth_service.repository.UserRepository;
import com.project.auth_service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private HostService hostService;

    @Autowired
    private GuestService guestService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username);
    }

    public boolean activate(String username) {
        User user = findByEmail(username);
        if(user == null || user.isEnabled()){
            return false;
        }
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    public UserDTO register(RegisterUserDto dto)  {

        if (dto.getRole().equalsIgnoreCase("host")){
            return hostService.save(dto).toDto();
        } else if (dto.getRole().equalsIgnoreCase("guest")){
            return guestService.save(dto).toDto();
        } else {
            return null;
        }
    }

    public LoginResponse login(LoginDto dto){

        User user = userRepository.findUserByEmail(dto.getMail());

        if (user !=null){

            String token = null;
            if (new BCryptPasswordEncoder().matches(dto.getPassword(), user.getPassword())) {
                token = tokenUtils.generateToken(user.getUsername(), user.getRole());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(token);
                loginResponse.setRole(user.getRole());
                loginResponse.setUsername(user.getUsername());
                return loginResponse;
            }
        }
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setToken(null);
        return loginResponse;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

//    public void sendPin(String username) throws MessagingException {
//        String pin = generatePin(6);
//        User user = findByEmail(username);
//        user.setLoginPin(passwordEncoder.encode(pin));
//        save(user);
//        mailService.sendPinInfo(user, pin);
//    }

    public static String generatePin(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkPin(String username, String pin) {
        User user = findByEmail(username);
        System.out.println(passwordEncoder.encode(pin));
        System.out.println(user.getLoginPin());
        return passwordEncoder.matches(pin, user.getLoginPin());
    }

    public User authorize(String token){
        String username = tokenUtils.getUsernameFromToken(token);
        User user = findByEmail(username);
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }
        return user;
    }

//    public void incrementLoginFailedAttempts(String email) {
//        User user = findByEmail(email);
//        if (user != null){
//            user.setFailedLoginAttempts(user.getFailedLoginAttempts()+1);
//            if (user.getFailedLoginAttempts() == 3){
//                String message = "ERROR - Nalog korisnika sa email adresom " + email + " je zakljucan";
//                loggerService.logError(message);
//                user.setLocked(true);
//                unlockUser(user);
//            }
//            save(user);
//        }
//    }

//    private void unlockUser(User user) {
//        Timer timer = new Timer();
//        CompletableFuture.runAsync(() -> timer.schedule(new TimerTask() {
//                public void run() {
//                    user.setLocked(false);
//                    user.setFailedLoginAttempts(0);
//                    save(user);
//                    System.out.println("30 minutes have passed!");
//                    loggerService.logInfo("INFO - Nalog korisnika sa email adresom " + user.getEmail() + " is otkljucan");
//                }
//            }, 30 * 60 * 1000L));
//
//    }

//    public List<Client> getAllClients(){
//        List<User> allUsers = this.userRepository.findAll();
//        List<Client> allClients = new ArrayList<>();
//
//        for(User u: allUsers){
//            if(u instanceof Client){
//                allClients.add((Client)u);
//            }
//        }
//
//        return allClients;
//    }

}
