package tech.projects.barberhub.service;

import org.springframework.stereotype.Service;
import tech.projects.barberhub.constants.barbershop.BarbershopConstants;
import tech.projects.barberhub.dto.barbershop.BarbershopDTO;
import tech.projects.barberhub.dto.barbershop.CreateBarbershopDTO;
import tech.projects.barberhub.exceptions.barbershop.BarbershopNotFoundException;
import tech.projects.barberhub.mappers.barbershop.BarbershopMapper;
import tech.projects.barberhub.model.barbershop.Barbershop;
import tech.projects.barberhub.model.catalog.Catalog;
import tech.projects.barberhub.repository.BarbershopRepository;
import tech.projects.barberhub.service.interfac.BarbershopCatalogService;
import tech.projects.barberhub.service.interfac.BarbershopService;
import tech.projects.barberhub.service.interfac.CatalogService;

import java.util.List;

@Service
public class BarbershopServiceImpl implements BarbershopService {
    private final BarbershopRepository barbershopRepository;
    private final BarbershopCatalogService barberShopCatalogService;
    private final CatalogService catalogService;

    BarbershopMapper barbershopMapper = new BarbershopMapper();

    public BarbershopServiceImpl(BarbershopRepository barbershopRepository, BarbershopCatalogService barberShopCatalogService, CatalogService catalogService) {
        this.barbershopRepository = barbershopRepository;
        this.barberShopCatalogService = barberShopCatalogService;
        this.catalogService = catalogService;
    }

    public List<BarbershopDTO> getBarberShops() {
        List<Barbershop> barbershopList = barbershopRepository.findAll();
        return barbershopList.stream().map(
                barbershop -> barbershopMapper.toDto(barbershop)
        ).toList();
    }

    public BarbershopDTO findBarbershopById(String id) {
        Barbershop barbershop = barbershopRepository.findById(id).orElseThrow(() -> new BarbershopNotFoundException(BarbershopConstants.NOT_FOUND));
        return barbershopMapper.toDto(barbershop);
    }

    public String createBarberShop(CreateBarbershopDTO dto) {
        Barbershop barberShop = barbershopMapper.toEntity(dto);
        barbershopRepository.save(barberShop);
        return barberShop.getId();
    }

    public void assignServiceToBarbershop(String barbershopId, String serviceId) {
        Barbershop barbershop = barbershopRepository.findById(barbershopId).orElseThrow(() -> new BarbershopNotFoundException(BarbershopConstants.NOT_FOUND));
        Catalog catalog = catalogService.findServiceById(serviceId);
        barberShopCatalogService.addServiceToBarbershop(barbershop, catalog);
    }

    private boolean verifyBarbershopExistence(String barbershopId) {
        return barbershopRepository.findById(barbershopId).isPresent();
    }
}