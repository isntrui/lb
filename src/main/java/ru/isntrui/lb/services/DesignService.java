package ru.isntrui.lb.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isntrui.lb.models.Design;
import ru.isntrui.lb.models.User;
import ru.isntrui.lb.repositories.DesignRepository;

import java.util.List;

@Service
public class DesignService {

    @Autowired
    private DesignRepository designRepository;

    @Transactional
    public void approveDesign(Long designId, boolean isApproved) {
        designRepository.approveDesign(designId, isApproved);
    }

    public List<Design> getAllDesigns() {
        return designRepository.findAll();
    }

    @Transactional
    public void deleteDesign(Long designId) {
        designRepository.deleteById(designId);
    }

    public void createDesign(Design design) {
        designRepository.save(design);
    }

    public List<Design> getDesignsForWave(Long waveId) {
        return designRepository.findByWaveId(waveId);
    }

    public Design getDesignById(Long id) {
        return designRepository.findById(id).orElse(null);
    }

    public List<Design> getDesignsByUser(User user) {
        return designRepository.getAllByCreatedBy(user);
    }
}