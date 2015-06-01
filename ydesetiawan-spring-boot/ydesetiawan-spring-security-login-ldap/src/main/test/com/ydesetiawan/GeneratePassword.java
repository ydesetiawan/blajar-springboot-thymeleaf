/* Copyright (C) 2015 ASYX International B.V. All rights reserved. */
package com.ydesetiawan;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author edys
 * @version 1.0, May 29, 2015
 * @since 
 */
public class GeneratePassword {
    
    @Test
    public void tested(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.print(encoder.encode("asyx436"));
    }

}
