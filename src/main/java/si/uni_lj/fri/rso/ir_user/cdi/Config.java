package si.uni_lj.fri.rso.ir_user.cdi;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-config")
public class Config {

    @ConfigValue(watch = true)
    private Boolean endpointEnabled;

    public Boolean getEndpointEnabled() {
        return endpointEnabled;
    }

    public void setEndpointEnabled(Boolean endpointEnabled) {
        this.endpointEnabled = endpointEnabled;
    }
}
