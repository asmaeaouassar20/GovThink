package com.algostyle.backend.controller.fileexcel;

import com.algostyle.backend.model.entity.FichierExcel;
import com.algostyle.backend.repository.FilePathRepository;
import com.algostyle.backend.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins={"https://govthink.netlify.app","http://localhost:4200"})
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private FilePathRepository filePathRepository;


    // Endpoint GET pour récupérer les données Excel sous forme de liste de Map (clé/valeur)
    @GetMapping("/dashboard/{id}")
    public ResponseEntity<List<Map<String, String>>> getExcelData(@PathVariable Long id){
        try{
            // Récupérer le chemin du fichier Excel en fonction de l'identifiant passé dans l'URL
            String filePath = filePathRepository.findById(id)
                    .orElseThrow(   ()-> new RuntimeException("Fichier non trouvé") )
                    .getChemin();

            // Utiliser le service pour lire les données du fichier Excel
            List<Map<String, String>> data= excelService.readExcelFile(filePath);

            // Afficher le contenu du fichier dans la console
            for(Map<String, String> map:data){
                for(Map.Entry<String, String> entry : map.entrySet()){
                    System.out.println("Key: "+entry.getKey()+ " -value: "+entry.getValue());
                }
                System.out.println("---------------------------------------");
            }

            return ResponseEntity.ok(data);

        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    @GetMapping
    public ResponseEntity<List<FichierExcel>>  getAllFiles(){
        return ResponseEntity.ok(this.excelService.getAllFiles());
    }



    @GetMapping("/{id}")
    public ResponseEntity<FichierExcel> getFileById(@PathVariable Long id ){
        return ResponseEntity.ok(this.excelService.getFileById(id));
    }
}
