package eco.mart.totalmart.repositories;

import eco.mart.totalmart.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}