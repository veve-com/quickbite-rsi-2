package quickbite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quickbite.entity.Pesanan;
import java.util.List;

public interface PesananRepository extends JpaRepository<Pesanan, String> {
    List<Pesanan> findAllByOrderByTanggalDesc();
}