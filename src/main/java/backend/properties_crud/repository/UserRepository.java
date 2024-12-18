package backend.properties_crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.properties_crud.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    User findByEmail(String email);
}