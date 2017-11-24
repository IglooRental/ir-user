package si.uni_lj.fri.rso.ir_user.api;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import com.kumuluz.ee.logs.cdi.Log;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@RegisterService
@ApplicationPath("v1")
public class UserApplication extends Application {
}
