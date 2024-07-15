package ufrn.imd.br.msauthserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufrn.imd.br.msauthserver.dto.ApiResponseDTO;
import ufrn.imd.br.msauthserver.dto.PatientDTO;
import ufrn.imd.br.msauthserver.model.Patient;
import ufrn.imd.br.msauthserver.service.PatientService;

@RestController
@RequestMapping("/v1/patients")
public class PatientController extends GenericController<Patient, PatientDTO, PatientService>  {
    /**
     * Constructs a GenericController instance with the provided service.
     *
     * @param service The service associated with the controller.
     */
    protected PatientController(PatientService service) {
        super(service);
    }




}
