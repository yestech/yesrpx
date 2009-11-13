package org.yestech.rpx.objectmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.yestech.rpx.objectmodel.RPXStat.fromString;
import static org.yestech.rpx.objectmodel.RPXUtil.jsonArray;
import static org.yestech.rpx.objectmodel.RPXUtil.jsonString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MappingsResponse implements Serializable {

    private RPXStat stat;
    private Collection<String> identifiers = Collections.emptyList();

    public RPXStat getStat() {
        return stat;
    }

    public void setStat(RPXStat stat) {
        this.stat = stat;
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Collection<String> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingsResponse that = (MappingsResponse) o;

        if (identifiers != null ? !identifiers.equals(that.identifiers) : that.identifiers != null) return false;
        if (stat != that.stat) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stat != null ? stat.hashCode() : 0;
        result = 31 * result + (identifiers != null ? identifiers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MappingsResponse{" +
                "stat=" + stat +
                ", identifiers=" + identifiers +
                '}';
    }

    public static MappingsResponse fromJson(JSONObject jo) throws JSONException {
        MappingsResponse response = new MappingsResponse();
        response.stat = fromString(jo.getString("stat"));
        JSONArray array = jsonArray(jo, "identifiers");
        if (array != null && array.length() > 0) {
            response.identifiers = new ArrayList<String>(array.length());
            for (int i = 0, size = array.length(); i < size; i++) {
                String identifier = jsonString(array, i);
                response.identifiers.add(identifier);
            }
        }

        return response;
    }


}
