package feup.cmov.cafeteriaadmin.models;

import com.orm.SugarRecord;

public class Blacklist extends SugarRecord {
    public String id;

    public Blacklist(String id) {
        this.id = id;
    }

    public Blacklist() {
    }
}
