package com.bootcamp.ECommerceApplication.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email(message = "The Email ID is not valid or already exist")
    @NotEmpty(message = "Email Id is a mandatory field")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "First Name is a mandatory field")
    private String first_name;
    private String middle_name;

    @NotEmpty(message = "Last Name is a mandatory field")
    private String last_name;

    @NotEmpty(message = "Password is a mandatory field")
    @Length(min = 8,max = 15,message = "The Length of the password should be between 8 to 15 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$",
            message = "The Password should be 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;


    private Boolean is_deleted;
    private Boolean is_active;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private List<Role> roles;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Address> addresses;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        addresses.stream().forEach(e -> e.setUser(this));
        this.addresses = addresses;
    }

    public void addAddress(Address address)
    {
        if(address != null){
            if(addresses == null){
                addresses = new ArrayList<>();
            }
            address.setUser(this);
            addresses.add(address);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", password='" + password + '\'' +
                ", is_deleted=" + is_deleted +
                ", is_active=" + is_active +
                ", roles=" + roles +
                ", addresses=" + addresses +
                '}';
    }
}
