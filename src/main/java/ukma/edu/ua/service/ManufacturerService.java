package ukma.edu.ua.service;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.model.entity.Manufacturer;
import ukma.edu.ua.persistent.impl.ManufacturerDao;
import ukma.edu.ua.service.exceptions.NoEntityFoundException;

import java.util.List;

@RequiredArgsConstructor
public class ManufacturerService {
    private final ManufacturerDao manufacturerDao;

    public Manufacturer addManufacturer(Manufacturer manufacturer) {
        manufacturerDao.save(manufacturer);
        return manufacturer;
    }

    public List<Manufacturer> getAll() {
        return manufacturerDao.getAll();
    }

    public void deleteManufacturer(long id) throws NoEntityFoundException {
        Manufacturer manufacturer = manufacturerDao.findById(id)
                .orElseThrow(() -> new NoEntityFoundException(id));
        manufacturerDao.delete(manufacturer);
    }

    public void updateManufacturer(Manufacturer manufacturer) {
        manufacturerDao.update(manufacturer);
    }

    public Manufacturer getManufacturer(long id) throws NoEntityFoundException {
        return manufacturerDao.findById(id)
                .orElseThrow(() -> new NoEntityFoundException(id));
    }
}