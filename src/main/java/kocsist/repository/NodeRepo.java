package kocsist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.Node;
@Repository
public interface NodeRepo extends CrudRepository<Node, Integer> {

}
