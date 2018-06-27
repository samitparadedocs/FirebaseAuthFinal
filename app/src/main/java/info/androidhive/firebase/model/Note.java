package info.androidhive.firebase.model;

/**
 * Created by Samit
 */
public class Note {
    private String uid;
    private String title;

    private String decription;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
       return uid;
    }
}
