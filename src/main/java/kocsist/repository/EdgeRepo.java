package kocsist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.Edge;
@Repository
public interface EdgeRepo extends CrudRepository<Edge, Long> {

}
