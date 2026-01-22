package org.smartlink.workflow.utils.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author 86158
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryKey {
    private String configType;
    private String tableName;
    private String queryRelation;
    private String joinConfig;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryKey)) {
            return false;
        }
        QueryKey queryKey = (QueryKey) o;
        return Objects.equals(configType, queryKey.configType) &&
               Objects.equals(tableName, queryKey.tableName) &&
               Objects.equals(queryRelation, queryKey.queryRelation) &&
               Objects.equals(joinConfig, queryKey.joinConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configType, tableName, queryRelation, joinConfig);
    }
}
