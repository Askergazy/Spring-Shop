package kz.askar.shop.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    @PreAuthorize("hasRole(ROLE.ADMIN)")
    public void doAdminStuff(){
        System.out.println("Only Admin here");
    }

}
