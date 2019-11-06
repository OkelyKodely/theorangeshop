package org.o7planning.springmvcshoppingcart.authentication;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import org.o7planning.springmvcshoppingcart.dao.AccountDAO;
import org.o7planning.springmvcshoppingcart.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
@Service
public class MyDBAuthenticationService implements UserDetailsService {
 
    @Autowired
    private AccountDAO accountDAO;
 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountDAO.findAccount(username);
        System.out.println("Account= " + account);
 
        if (account == null) {
            throw new UsernameNotFoundException("User " //
                    + username + " was not found in the database");
        }
 
        // EMPLOYEE,MANAGER,..
        String role = account.getUserRole();
 
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
 
        // ROLE_EMPLOYEE, ROLE_MANAGER
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
 
        grantList.add(authority);
 
        boolean enabled = account.isActive();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
 
        UserDetails userDetails = (UserDetails) new User(account.getUserName(), //
                account.getPassword(), enabled, accountNonExpired, //
                credentialsNonExpired, accountNonLocked, grantList);

        if(userDetails == null || 1==1) {
    		String hostName = "localhost:3320";
    		String dbName = "mydb";
    		String userName = "root";
    		String passWord = "";

    		Connection conn = null;
    		
    		Statement st1 = null;
    		
  			try {
        		Class.forName("com.mysql.jdbc.Driver");
        		String url = "jdbc:mysql://" + hostName + "/" + dbName;

        		conn = DriverManager.getConnection(url, userName, passWord);
        
        		st1 = conn.createStatement();
        		ResultSet rs = st1.executeQuery("select * from accounts where user_name = '"+account.getUserName()+"' and password = '"+account.getPassword()+"'");

        		
        		
  			} catch(Exception e) {
  				e.printStackTrace();
    	    }
	
        }
        
        return userDetails;
    }
 
}