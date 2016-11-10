package feup.cmov.cafeteriaadmin.models;

import com.orm.SugarRecord;

public class Blacklist extends SugarRecord {
    public String name;

    public Blacklist() {
    }

    public Blacklist(String name) {
        this.name = name;
    }

}
