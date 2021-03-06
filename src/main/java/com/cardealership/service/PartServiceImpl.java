package com.cardealership.service;

import com.cardealership.domain.entity.Part;
import com.cardealership.domain.model.service.cars.CarServiceModel;
import com.cardealership.domain.model.service.parts.PartServiceModel;
import com.cardealership.repository.PartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;

    private final ModelMapper modelMapper;

    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void craete(PartServiceModel partServiceModel) {
        Part part = this.modelMapper.map(partServiceModel, Part.class);
        this.partRepository.save(part);
    }

    @Override
    public List<PartServiceModel> findAll() {
        List<PartServiceModel> partServiceModels = new ArrayList<>();
        this.partRepository.findAll().forEach(part -> {
            PartServiceModel partServiceModel = this.modelMapper.map(part, PartServiceModel.class);
            partServiceModels.add(partServiceModel);
        });
        return partServiceModels;
    }

    @Override
    public PartServiceModel findById(Long id) {
        Part part = this.partRepository.findPartById(id);
        PartServiceModel partModel = this.modelMapper.map(part, PartServiceModel.class);
        Set<CarServiceModel> carServiceModels = new LinkedHashSet<>();
        part.getCars().forEach(carEntity -> {
            CarServiceModel carServiceModel = this.modelMapper.map(carEntity, CarServiceModel.class);
            carServiceModels.add(carServiceModel);
        });
        partModel.setCars(carServiceModels);
        return partModel;
    }

    @Override
    public void edit(PartServiceModel serviceModel) {
        Part part = this.partRepository.findById(serviceModel.getId()).orElse(null);
        part.setPrice(serviceModel.getPrice());
        this.partRepository.save(part);
    }
}