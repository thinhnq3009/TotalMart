package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.repositories.CategoryGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryGroupService {

    @Autowired
    CategoryGroupRepository groupRepository;

    public List<CategoryGroup> getTop3PublicGroups() {
        return groupRepository.findTopThreeCategoriesByTotalSold()
                .stream()
                .map(item -> (CategoryGroup) item[0])
                .collect(Collectors.toList());
    }

    public Optional<CategoryGroup> findById(String id) {
        return groupRepository.findById(id);
    }

    public List<CategoryGroup> findAll() {
      return  groupRepository.findAll();
    }
}
