package project.recipemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.recipemanager.model.ActivationCode;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
}
