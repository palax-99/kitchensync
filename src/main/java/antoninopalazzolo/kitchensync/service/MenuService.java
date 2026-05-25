package antoninopalazzolo.kitchensync.service;

import antoninopalazzolo.kitchensync.entity.Categoria;
import antoninopalazzolo.kitchensync.entity.Piatto;
import antoninopalazzolo.kitchensync.entity.PiattoIngrediente;
import antoninopalazzolo.kitchensync.entity.Sezione;
import antoninopalazzolo.kitchensync.payload.MenuCategoriaDTO;
import antoninopalazzolo.kitchensync.payload.MenuPiattoDTO;
import antoninopalazzolo.kitchensync.payload.MenuSezioneDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    private final SezioneService sezioneService;
    private final CategoriaService categoriaService;
    private final PiattoService piattoService;
    private final PiattoIngredienteService piattoIngredienteService;

    public MenuService(SezioneService sezioneService,
                       CategoriaService categoriaService,
                       PiattoService piattoService,
                       PiattoIngredienteService piattoIngredienteService) {
        this.sezioneService = sezioneService;
        this.categoriaService = categoriaService;
        this.piattoService = piattoService;
        this.piattoIngredienteService = piattoIngredienteService;
    }

    // Costruisco il menu vivo — solo sezioni attive, solo piatti con tutti gli ingredienti disponibili
    public List<MenuSezioneDTO> getMenuVivo() {
        List<Sezione> sezioniAttive = sezioneService.trovaTutteAttive();
        List<MenuSezioneDTO> menu = new ArrayList<>();

        for (Sezione sezione : sezioniAttive) {
            List<MenuCategoriaDTO> categorieDTO = new ArrayList<>();
            List<Categoria> categorie = categoriaService.findBySezione(sezione);

            for (Categoria categoria : categorie) {
                List<MenuPiattoDTO> piattiDisponibili = new ArrayList<>();
                List<Piatto> piatti = piattoService.findByCategoria(categoria);

                for (Piatto piatto : piatti) {
                    // Se tutti gli ingredienti sono disponibili, aggiungo il piatto al menu
                    if (isPiattoDisponibile(piatto)) {
                        piattiDisponibili.add(new MenuPiattoDTO(
                                piatto.getId(),
                                piatto.getNome(),
                                piatto.getDescrizione(),
                                piatto.getPrezzo(),
                                piatto.isPersonalizzabile(),
                                piatto.getImmagineUrl()
                        ));
                    }
                }

                // Aggiungo la categoria solo se ha almeno un piatto disponibile
                if (!piattiDisponibili.isEmpty()) {
                    categorieDTO.add(new MenuCategoriaDTO(
                            categoria.getId(),
                            categoria.getNome(),
                            piattiDisponibili
                    ));
                }
            }

            // Aggiungo la sezione solo se ha almeno una categoria con piatti disponibili
            if (!categorieDTO.isEmpty()) {
                menu.add(new MenuSezioneDTO(
                        sezione.getId(),
                        sezione.getNome(),
                        categorieDTO
                ));
            }
        }

        return menu;
    }

    // Se anche un solo ingrediente è false, il piatto non appare nel menu
    private boolean isPiattoDisponibile(Piatto piatto) {
        List<PiattoIngrediente> ingredienti = piattoIngredienteService.trovaPerId(piatto.getId());
        return ingredienti.stream().allMatch(pi -> pi.getIngrediente().isDisponibile());
    }
}