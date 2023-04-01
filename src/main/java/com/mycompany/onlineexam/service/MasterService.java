package com.mycompany.onlineexam.service;

import com.mycompany.onlineexam.domain.Master;
import com.mycompany.onlineexam.service.dto.MasterDTO;

import java.util.List;

public interface MasterService {
    Master checkMasterLogin(String username, String password);

    Master createMaster(MasterDTO masterDto);

    void deleteMaster(String masterCode);

    Master updateMasterInfo(MasterDTO masterDto);

    Master getMasterByMasterCode(String masterCode);

    Master getMasterByUsername(String username);

    List<Master> getAllMasters();
}
