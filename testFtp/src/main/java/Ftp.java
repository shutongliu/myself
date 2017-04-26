import com.test.LoadProperties;
import com.test.util.Login;

import java.util.Properties;

/**
 * Created by liust on 2017/3/24.
 */
public class Ftp {
    public static void main(String[] args) {
        Properties properties = LoadProperties.getProperties();
        Login.login(properties);
    }
}
