package si.uni_lj.fri.rso.ir_user.cdi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.uni_lj.fri.rso.ir_user.models.User;
import si.uni_lj.fri.rso.ir_user.models.dependencies.Property;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class UserDatabase {
    // user-catalogue-service is the kumuluzee application name as defined in the yaml file
    // this also MUST be in a bean (see @requestscoped above) for it to work
    @Inject
    @DiscoverService("property-catalogue-service")
    private String basePath;

    private Logger log = LogManager.getLogger(UserDatabase.class.getName());

    @Inject
    private EntityManager em;

    private HttpClient httpClient = HttpClientBuilder.create().build();
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Property> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Property.class));
    }

    public List<User> getUsers() {
        TypedQuery<User> query = em.createNamedQuery("User.getAll", User.class);
        return query.getResultList();
    }

    public List<User> getUsersFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0).build();
        return JPAUtils.queryEntities(em, User.class, queryParameters);
    }

    public User getUser(String userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new NotFoundException();
        }
        user.setProperties(getProperties(user.getId()));
        return user;
    }

    private List<Property> getProperties(String userId) {
        if (basePath != null) {
            try {
                HttpGet request = new HttpGet(basePath + "/v1/properties/filtered?where=ownerId:EQ:" + userId);
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
            log.error("base path is null");
        }
        return new ArrayList<>();
    }

    public User createUser(User user) {
        try {
            beginTx();
            em.persist(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return user;
    }

    public User putUser(String userId, User user) {
        User p = em.find(User.class, userId);
        if (p == null) {
            return null;
        }
        try {
            beginTx();
            user.setId(p.getId());
            user = em.merge(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }
        return user;
    }

    public boolean deleteUser(String userId) {
        User p = em.find(User.class, userId);
        if (p != null) {
            try {
                beginTx();
                em.remove(p);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else {
            return false;
        }
        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
