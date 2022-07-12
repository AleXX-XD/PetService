package PetServiceApp.services;

import PetServiceApp.models.User;
import PetServiceApp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
public class UserService implements UserDetailsService{

    public static final int MAX_FAILED_ATTEMPTS = 3;
    public static final long LOCK_TIME_DURATION = 5 * 60 * 1000;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public void save(User user) {
        user.setRoles(Collections.singleton(roleService.getRoleById(1)));
        userRepository.save(user);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public void increaseFailedAttempts(User user) {
        int failedAttempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(failedAttempt);
        if(failedAttempt == MAX_FAILED_ATTEMPTS) {
            user.lockedAccount();
        }
        userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        user.resetFailedAttempt();
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user!=null){
            return User.builder()
                    .setUsername(user.getUsername())
                    .setPassword(user.getPassword())
                    .setRoles(user.getRoles())
                    .setAccountNonLocked(user.isAccountNonLocked())
                    .build();
        }else{
            throw new UsernameNotFoundException("user not found");
        }
    }
}
