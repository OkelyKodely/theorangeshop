package org.o7planning.springmvcshoppingcart.model;
 
public class SpecialCustomerInfo {
 
	private String username;
    private String password;
    private String name;
    private String address;
    private String email;
    private String phone;
    private boolean valid;
 
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getAddress() {
        return address;
    }
 
    public void setAddress(String address) {
        this.address = address;
    }
 
    public String getPhone() {
        return phone;
    }
 
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public boolean isValid() {
        return valid;
    }
 
    public void setValid(boolean valid) {
        this.valid = valid;
    }
 
}