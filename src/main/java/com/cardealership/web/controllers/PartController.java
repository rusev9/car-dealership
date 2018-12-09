package com.cardealership.web.controllers;

import com.cardealership.domain.model.binding.parts.CreatePartBindingModel;
import com.cardealership.domain.model.service.parts.PartServiceModel;
import com.cardealership.domain.model.view.suppliers.SupplierForCreatingPartModel;
import com.cardealership.service.PartService;
import com.cardealership.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@Controller
@RequestMapping("/parts")
public class PartController extends BaseController {

    private final SupplierService supplierService;

    private final PartService partService;

    private final ModelMapper modelMapper;

    public PartController(SupplierService supplierService, PartService partService, ModelMapper modelMapper) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createPart(@ModelAttribute CreatePartBindingModel createPartBindingModel) {
        Set<SupplierForCreatingPartModel> suppliers = this.supplierService.findAllSuppliers();
        return super.view("/views/parts/create", suppliers);
    }

    @PostMapping("create")
    public ModelAndView confirmCreatePart(@ModelAttribute CreatePartBindingModel createPartBindingModel) {
        PartServiceModel partServiceModel = this.modelMapper.map(createPartBindingModel, PartServiceModel.class);
        partServiceModel.setId(Long.parseLong(createPartBindingModel.getSupplierId()));
        this.partService.createPart(partServiceModel);
        return super.redirect("/");
    }
}