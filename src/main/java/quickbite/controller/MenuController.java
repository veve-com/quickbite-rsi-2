package quickbite.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickbite.controller.dto.MenuInfo;
import quickbite.repository.MenuRepository;
import java.util.List;

@Service
public class MenuController {
    private final MenuRepository menuRepo;

    public MenuController(MenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    @Transactional(readOnly = true)
    public List<MenuInfo> daftarMenu() {
        return menuRepo.findAllByOrderByIdMenuAsc().stream()
                .map(m -> new MenuInfo(m.getIdMenu(), m.getNamaMenu(),
                        m.getHarga(), m.getStok()))
                .toList();
    }
}