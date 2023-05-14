package eco.mart.totalmart.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GroupProperty {
   public String name;

    public Property info;

    public List<ProductProperty> propertiesValue;
}
