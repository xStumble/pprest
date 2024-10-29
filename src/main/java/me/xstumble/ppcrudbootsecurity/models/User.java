package me.xstumble.ppcrudbootsecurity.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Username cannot be empty!")
    @Size(min = 4, max = 30, message = "Username must be between 6 and 30 characters!")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    @Min(value = 0, message = "Invalid age!")
    private int age;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email must be valid!")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    @Size(min = 4, message = "Password must be 4 or more characters!")
    private String password;

    @NotEmpty(message = "Roles cannot be empty!")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {

    }

    public User(String username, String firstName, String lastName, int age, String email, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public User(String username, String firstName, String lastName, int age, String email,
                String password, Set<Role> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getRolesString() {
        Set<String> set = roles.stream()
                .map(s -> s.getAuthority().replace("ROLE_", "")).collect(Collectors.toSet());
        return String.join(" ", set);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", firstName=" + firstName +
                ", lastName=" + lastName + ", age=" + age + ", email=" + email + ", password=" + password +
                ", roles=" + roles + "]";
    }
}
