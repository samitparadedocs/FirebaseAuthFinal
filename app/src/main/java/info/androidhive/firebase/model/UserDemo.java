package info.androidhive.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Samit
 */
@IgnoreExtraProperties
public class UserDemo {

    public String username;
    public String user_type;
    public String email;
    public String contact_number;
    public String address;

    public UserDemo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserDemo(String username, String email) {
        this.username = username;
        this.email = email;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("usernmae", username);
        result.put("email", email);

        return result;
    }
}
