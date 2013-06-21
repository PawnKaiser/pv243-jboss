package cz.muni.fi.pv243.et.security;


import org.jboss.ejb3.annotation.Clustered;
import org.picketlink.Identity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Clustered
@RequestScoped
@Named
public class LogoutBean {

    private static final long serialVersionUID = 1L;

    @Inject
    private Identity identity;


    public String logout() {
        if (this.identity.isLoggedIn()) {
            this.identity.logout();
        }

        return "/login.xhtml";
    }
}
