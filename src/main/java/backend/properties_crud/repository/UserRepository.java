package backend.properties_crud.repository;

import backend.properties_crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /*
  impicitely given methods by spring data jpa are

  save(User user) : works for both insertion and updation of users
  */

  @Query("SELECT u FROM User u WHERE u.email = :email")
  User findByEmail(@Param("email") String email);

  void deleteByEmail(String email);
}
