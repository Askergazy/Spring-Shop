package kz.askar.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;



    @Enumerated(EnumType.ORDINAL)
    private Role role;


    @NotEmpty(message = "Логин не должен быть пустым")
    @Size(min = 2,max = 100,message = "Логин должен быть от 2 до 100 символов длниной")
    String login;

    @NotEmpty(message = "Пароль не должен быть пустым")
    String password;


    @NotEmpty
    String name;

    @NotEmpty
    @Column(name = "last_name")
    String lastName;

    @Column(name = "registration_data")
    Timestamp registrationDate;

    @OneToMany(mappedBy = "user")

    private List<Review> reviews;


    @OneToMany(mappedBy = "user", orphanRemoval = true)

    private List<Order> orders = new ArrayList<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "role = " + role + ", " +
                "login = " + login + ", " +
                "password = " + password + ", " +
                "name = " + name + ", " +
                "lastName = " + lastName + ", " +
                "registrationDate = " + registrationDate + ")";
    }
}
