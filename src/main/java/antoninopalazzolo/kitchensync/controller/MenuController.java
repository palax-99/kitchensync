package antoninopalazzolo.kitchensync.controller;

import antoninopalazzolo.kitchensync.payload.MenuSezioneDTO;
import antoninopalazzolo.kitchensync.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // Il METRE vede il menu vivo — solo sezioni attive e piatti con ingredienti disponibili
    @GetMapping
    @PreAuthorize("hasAnyAuthority('METRE', 'SUPER_ADMIN')")
    public List<MenuSezioneDTO> getMenuVivo() {
        return menuService.getMenuVivo();
    }
}