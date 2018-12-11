package hello.api.users.entity;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "vk", length = 80)
    private String vk;


    @Column(name = "email", length = 80)
    private String email;

    @Column(name = "password", length = 80)
    private String password;

    @Column(name = "identify")
    private boolean identify;

    @Column(name = "uid", unique = true)
    private UUID uid;

    public Integer getId() {
        return id;
    }


    public String getVk() {
        return vk;
    }


    public void setVk(String vk) {
        this.vk = vk;
    }


    public void setIdentify(boolean identify) {
        this.identify = identify;
    }

    public boolean getIdentify() {
        return identify;
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

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder()
                .append(id, user.id)
                .append(vk, user.vk)
                .append(email, user.email)
                .append(password, user.password)
                .append(identify, user.identify)
                .append(uid, user.uid)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(vk)
                .append(email)
                .append(password)
                .append(identify)
                .append(uid)
                .toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("vk", vk)
                .add("email", email)
                .add("password", password)
                .add("identify", identify)
                .add("uid", uid)
                .toString();
    }
}
