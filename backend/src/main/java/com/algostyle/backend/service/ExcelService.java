package com.algostyle.backend.service;

import com.algostyle.backend.model.entity.FichierExcel;
import com.algostyle.backend.repository.FilePathRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    @Autowired
    private FilePathRepository filePathRepository;


    // Méthode qui prend un chemin de fichier Excel en entrée et retourne les données lues
    public List<Map<String, String>> readExcelFile(String filePath) throws IOException{
        File file = new File(filePath);    // Créer un objet File à partir du chemin donné
        Workbook workbook = WorkbookFactory.create(file);   // Utilisation de la classe WorkbookFactory (Apache POI) pour ouvrir le fichier

        Sheet sheet=workbook.getSheetAt(0); // Récupérer la première feuille (index 0)

        // Liste finale contenant les lignes du fichier, sous forme de Map ( entête -> valeur )
        List< Map<String, String> > data = new ArrayList<>();

        /**
         * --  Exemple de format de la liste à retourner à partir du fichier --
         *      [
         *          { "Nom":"Ali" , "âge":"22"  ,  "Ville":"rabat" },
         *          { "Nom":"Asmae" , "âge":"25"  ,  "Ville":"Casa" }
         *      ]
         *
         */


        Row headerRow = sheet.getRow(0); // Lecture de la première ligne (en-tête)
        List<String> headers = new ArrayList<>();
        for(Cell cell:headerRow){   // Récupérer chaque cellule de l'en-tête pour extraire les noms de colonnes
            headers.add(cell.getStringCellValue()); // ex: "Nom", "Age", "Ville"
        }


        // --- Lire les données ---
        // On commence à la ligne 1 (car 0-> en-tête)
        for(int i=1; i<=sheet.getLastRowNum(); i++){
            Row row=sheet.getRow(i);
            Map<String, String> rowData = new HashMap<>();   // Une ligne = une Map (colonne -> valeur)

            // On parcourt chaque colonne de l'en-tête
            for(int j=0; j<headers.size(); j++){
                // On récupère la cellule correspondante (si elle est vide, on crée une cellule vide)
                Cell cell=row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                // On détecte le type de la cellule et on convertit sa valeur en String
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.put(headers.get(j), cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.put(headers.get(j), String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        rowData.put(headers.get(j), String.valueOf(cell.getBooleanCellValue()));
                        break;
                    default:
                        rowData.put(headers.get(j), "");
                }
            }

            // On ajoute la ligne traitée à la liste finale
            data.add(rowData);
            }

        // On ferme le classeur Excel pour libérer les ressources
        workbook.close();

        // On retourne les données lues
        return data;
        }







    public List<FichierExcel> getAllFiles(){
        return this.filePathRepository.findAll();
    }



    public FichierExcel getFileById(Long id){
        return this.filePathRepository.findById(id).orElseThrow(()-> new RuntimeException("file id "+ id + "not found"));
    }
}
