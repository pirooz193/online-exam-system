package com.mycompany.onlineexam.web.rest;

import com.mycompany.onlineexam.domain.Master;
import com.mycompany.onlineexam.domain.constants.Constants;
import com.mycompany.onlineexam.service.MasterService;
import com.mycompany.onlineexam.service.dto.MasterDTO;
import com.mycompany.onlineexam.web.errors.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MasterResource {

    private final Logger logger = LogManager.getLogger(MasterResource.class);

    private final MasterService masterService;

    public MasterResource(MasterService masterService) {
        this.masterService = masterService;
    }

    /**
     * Request to check Master existence
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/master/login")
    public ResponseEntity<Master> loginMaster(@RequestParam String username, @RequestParam String password) {
        logger.info("Request to login Master with username:{} , and password :{}", username, password);
        Master master = masterService.checkMasterLogin(username, password);
        if (master == null) throw new NotFoundException(Constants.MASTER_CODE);
        return ResponseEntity.ok(master);
    }

    /**
     * Request to create a new Master
     *
     * @param masterDto
     * @return
     */
    @PostMapping("/admin/create-master")
    public ResponseEntity<Master> createMaster(@RequestBody MasterDTO masterDto) {
        logger.info("Request to create a new Master :{}", masterDto);
        Master master = masterService.createMaster(masterDto);
        if (master == null) throw new NotFoundException(Constants.MASTER_CODE);
        return ResponseEntity.created(URI.create("/create")).body(master);
    }

    @GetMapping("/admin/all-teachers")
    public ResponseEntity<List<Master>> getAllTeachers() {
        logger.info("Request to get all masters");
        List<Master> allMasters = masterService.getAllMasters();
        return ResponseEntity.ok().body(allMasters);
    }

    /**
     * Request to delete Master by master code .
     *
     * @param masterCode
     * @return
     */
    @DeleteMapping("/admin/delete-master/{masterCode}}")
    public void deleteMaster(@PathVariable String masterCode) {
        logger.info("Request to delete Master with master code : {}", masterCode);
        masterService.deleteMaster(masterCode);
    }

    @PutMapping("/master/update-info")
    public ResponseEntity<Master> updateMasterInfo(@RequestBody MasterDTO masterDto) {
        logger.info("Request to update a master:{}", masterDto);
        Master master = masterService.updateMasterInfo(masterDto);
        return ResponseEntity.ok(master);
    }

}
