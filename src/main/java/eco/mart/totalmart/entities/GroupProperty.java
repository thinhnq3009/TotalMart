package eco.mart.totalmart.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupProperty {
   public String name;

    public Property info;

    public List<ProductProperty> propertiesValue;
}
