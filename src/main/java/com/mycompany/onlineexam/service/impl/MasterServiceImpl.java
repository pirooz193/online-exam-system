package com.mycompany.onlineexam.service.impl;

import com.mycompany.onlineexam.domain.Master;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.repository.MasterRepository;
import com.mycompany.onlineexam.service.MasterService;
import com.mycompany.onlineexam.service.UserService;
import com.mycompany.onlineexam.service.dto.MasterDTO;
import com.mycompany.onlineexam.service.mapper.MasterMapper;
import com.mycompany.onlineexam.web.model.ApiUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.mycompany.onlineexam.config.enumuration.ApplicationUserRoles.ROLE_MASTER;

@Service
@Transactional
public class MasterServiceImpl implements MasterService {

    @Value(value = "${application.constants.master-code-number}")
    private Integer NUMBER_OF_MASTER_CODE;
    private final Logger logger = LogManager.getLogger(MasterServiceImpl.class);

    private final MasterRepository masterRepository;
    private final UserService userService ;
    private final PasswordEncoder passwordEncoder;
    private final MasterMapper masterMapper;

    public MasterServiceImpl(MasterRepository masterRepository, UserService userService, PasswordEncoder passwordEncoder, MasterMapper masterMapper) {
        this.masterRepository = masterRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.masterMapper = masterMapper;
    }

    @Override
    public Master checkMasterLogin(String username, String password) {
        logger.debug("Request to check  Master login with username :{} , and password :{}", username, password);
        return masterRepository.getMasterByUsernameAndPassword(username, password);
    }

    @Override
    public Master createMaster(MasterDTO masterDto) {
        Master master = masterMapper.toEntity(masterDto);
        master.setPassword(passwordEncoder.encode(master.getPhoneNumber()));
        master.setMasterCode(ApiUtil.generateRandomCode(Constants.MASTER_CODE, NUMBER_OF_MASTER_CODE));
        Master savedMaster = masterRepository.save(master);
        userService.addRoleToUser(savedMaster.getUsername(), ROLE_MASTER.name());
        return savedMaster;
    }

    @Override
    public void deleteMaster(String masterCode) {
        logger.debug("Request to delete Master with masterCode :{}", masterCode);
        masterRepository.deleteMasterByMasterCode(masterCode);
    }

    @Override
    public Master updateMasterInfo(MasterDTO masterDto) {
        logger.debug("Request to update master info :{}", masterDto);
        Master master = masterRepository.findById(masterDto.getId()).get();
        if (masterDto.getUsername() != null) master.setUsername(masterDto.getUsername());
        if (masterDto.getPassword() != null) master.setPassword(passwordEncoder.encode(masterDto.getPassword()));
        if (masterDto.getPhoneNumber() != null) master.setPhoneNumber(masterDto.getPhoneNumber());
        return masterRepository.save(master);
    }

    @Override
    public Master getMasterByMasterCode(String masterCode) {
        logger.debug("request to get master by  master-code:{}", masterCode);
        return masterRepository.getMasterByMasterCode(masterCode);
    }

    @Override
    public Master getMasterByUsername(String username) {
        return masterRepository.findMasterByUsername(username);
    }

    @Override
    public List<Master> getAllMasters() {
        logger.debug("Request to get all masters in service layer.");
        return masterRepository.findAll();
    }
}
