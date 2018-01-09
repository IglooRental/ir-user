package si.uni_lj.fri.rso.ir_user.models;

import org.eclipse.persistence.annotations.UuidGenerator;
import si.uni_lj.fri.rso.ir_user.models.dependencies.Property;
import si.uni_lj.fri.rso.ir_user.models.dependencies.Review;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@NamedQueries(value = {
        @NamedQuery(name = "User.getAll", query = "SELECT u FROM users u")
})
@UuidGenerator(name = "idGenerator")
public class User {
    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    private String name;

    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Transient
    private List<Property> properties;

    @Transient
    private List<Review> reviewsSubmitted;

    public User() {}

    public User(String id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Review> getReviewsSubmitted() {
        return reviewsSubmitted;
    }

    public void setReviewsSubmitted(List<Review> reviewsSubmitted) {
        this.reviewsSubmitted = reviewsSubmitted;
    }
}
