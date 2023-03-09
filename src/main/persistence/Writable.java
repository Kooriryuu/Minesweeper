package persistence;

import org.json.JSONObject;

public interface Writable {

    //EFFECTS: return the object as a JSON object
    JSONObject toJson();
}
