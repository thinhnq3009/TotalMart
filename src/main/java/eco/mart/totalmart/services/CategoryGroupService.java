package eco.mart.totalmart.services;

import eco.mart.totalmart.entities.CategoryGroup;
import eco.mart.totalmart.handler.UploadHandler;
import eco.mart.totalmart.module.ResponseObject;
import eco.mart.totalmart.repositories.CategoryGroupRepository;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class CategoryGroupService {

    @Autowired
    CategoryGroupRepository groupRepository;

    @Autowired
    ServletContext servletContext;

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
        return groupRepository.findAll();
    }

    public List<CategoryGroup> findByIsDeletedTrue() {
        return groupRepository.findByIsDeletedTrue();
    }

    public List<CategoryGroup> findByIdOrName(String slug, String name) {
        return groupRepository.findByIdOrName(slug, name);
    }

    public Optional<CategoryGroup> findByName(String name) {
        return groupRepository.findByName(name);
    }

    public CategoryGroup insert(String id, String name) {
        return groupRepository.save(new CategoryGroup(id, name));

    }


    public Optional<CategoryGroup> restore(String id) {
        Optional<CategoryGroup> group = groupRepository.findById(id);
        if (group.isPresent()) {
            group.get().setDeleted(false);
            return Optional.of(groupRepository.save(group.get()));
        }
        return Optional.empty();

    }

    public List<CategoryGroup> findAllNotDeleted() {
        return groupRepository.findAllByIsDeletedFalse();
    }
}
