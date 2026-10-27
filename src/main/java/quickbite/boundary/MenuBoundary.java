package quickbite.boundary;

import org.springframework.web.bind.annotation.*;
import quickbite.controller.MenuController;
import quickbite.controller.dto.MenuInfo;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuBoundary {
    private final MenuController menuController;

    public MenuBoundary(MenuController menuController) {
        this.menuController = menuController;
    }

    @GetMapping
    public List<MenuInfo> daftar() {
        return menuController.daftarMenu();
    }
}