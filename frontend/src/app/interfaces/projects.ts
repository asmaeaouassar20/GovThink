export interface Project {
    id? : number;
    titre : string; // obligatoire
    description : string;  // obligatoire
    imageUrl ? : string;    // optionnel
    lienProjet : string;    // obligatoire
    fileUrl ? : string;     // optionnel
}
