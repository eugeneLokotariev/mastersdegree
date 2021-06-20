package com.mastersessay.blockchain.accounting.service;

import com.mastersessay.blockchain.accounting.model.user.BlockchainAccountingUserDetails;
import com.mastersessay.blockchain.accounting.model.user.User;
import com.mastersessay.blockchain.accounting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Security.USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE;

@Service
public class BlockchainAccountingUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME_ERROR_MESSAGE
                        + " "
                        + username));

        return BlockchainAccountingUserDetails.build(user);
    }

    @Autowired
    public BlockchainAccountingUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
