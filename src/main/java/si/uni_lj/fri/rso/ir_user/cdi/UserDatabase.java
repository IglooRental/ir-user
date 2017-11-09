package si.uni_lj.fri.rso.ir_user.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.uni_lj.fri.rso.ir_user.models.dependencies.Property;
import si.uni_lj.fri.rso.ir_user.models.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestScoped
public class UserDatabase {
    // property-catalogue-service is the kumuluzee application name as defined in the yaml file
    // this also MUST be in a bean (see @requestscoped above) for it to work
    @Inject
    @DiscoverService("property-catalogue-service")
    private String basePath;

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<User> users = new ArrayList<>(Arrays.asList(
            new User("0", "User Name 1", "user1@example.com", "password hash very secure"),
            new User("1", "User Name 2", "user2@example.com", "password hash very secure too"),
            new User("2", "User Name 2", "user2@example.com", "passwort veri stronk yes yses")
    ));

    private List<Property> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Property.class));
    }

    public List<User> getUsers() {
        // TODO: use the filtered hibernate thingy
        return users;
    }

    public User getUser(String userId) {
        User user = null;
        for (User u : users) {
            if (u.getId().equals(userId))
                user = u;
        }
        if (user == null) {
            return null;
        }

        user.setProperties(getProperties(user.getId()));
        return user;
    }

    private List<Property> getProperties(String userId) {
        if (basePath != null) {
            try {
                HttpGet request = new HttpGet(basePath + "/v1/properties?where=ownerId:EQ:" + userId);
                HttpResponse response = httpClient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        return getObjects(EntityUtils.toString(entity));
                    }
                } else {
                    String msg = "Remote server '" + basePath + "' has responded with status " + status + ".";
                    throw new InternalServerErrorException(msg);
                }

            } catch (IOException e) {
                String msg = e.getClass().getName() + " occurred: " + e.getMessage();
                throw new InternalServerErrorException(msg);
            }
        } else {
            // service not available placeholder
            System.err.println("base path is null");
        }
        return new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void deleteUser(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                users.remove(user);
                break;
            }
        }
    }
}
