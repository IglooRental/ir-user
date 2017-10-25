package si.uni_lj.fri.rso.ir_user.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.uni_lj.fri.rso.ir_property.models.Property;
import si.uni_lj.fri.rso.ir_user.models.User;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDatabase {
    private static String basePath = "http://localhost:8081";
    private static HttpClient httpClient = HttpClientBuilder.create().build();
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static List<User> users = new ArrayList<>(Arrays.asList(
            new User("0", "User Name 1", "user1@example.com", "password hash very secure"),
            new User("1", "User Name 2", "user2@example.com", "password hash very secure too"),
            new User("2", "User Name 2", "user2@example.com", "passwort veri stronk yes yses")
    ));

    private static List<Property> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Property.class));
    }

    public static List<User> getUsers() {
        // TODO: use the filtered hibernate thingy
        return users;
    }

    public static User getUser(String userId) {
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

    private static List<Property> getProperties(String userId) {
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
        }
        return new ArrayList<>();
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void deleteUser(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                users.remove(user);
                break;
            }
        }
    }
}
